package com.fatur_atir_cahya.nyucidimana;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.service.LaundromatInterface;
import com.fatur_atir_cahya.nyucidimana.database.LaundromatManager;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerHomeFragment extends Fragment {

    SessionManager sessionManager;
    LaundromatManager laundromatManager;
    LaundromatInterface laundromatInterface;
    TextView laundromatName, revenue, totalTransactions, tPending, tProg, tDone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(getActivity());
        laundromatManager = new LaundromatManager(getActivity());
        laundromatInterface = ApiClient.getApiClient().create(LaundromatInterface.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_owner_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        laundromatName = view.findViewById(R.id.home_laundromat_name);
        revenue = view.findViewById(R.id.home_revenue);
        totalTransactions = view.findViewById(R.id.home_total_transactions);
        tPending = view.findViewById(R.id.home_total_transaction_pending);
        tProg = view.findViewById(R.id.home_total_transaction_progress);
        tDone = view.findViewById(R.id.home_total_transaction_done);

        laundromatName.setText(laundromatManager.getName());
        updateStatistics();
    }

    private void updateStatistics() {
        Call<JsonObject> callStatistics = laundromatInterface.getStatistics("Bearer " + sessionManager.getToken());
        callStatistics.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200) {
                    JsonObject laundromat = response.body().getAsJsonObject("data");

                    String revenueStats = laundromat.get("revenue").getAsString();
                    String transactionsStats = laundromat.get("total_transactions").getAsString();
                    String transactionPending = laundromat.get("transaction_pending").getAsString();
                    String transactionProgress = laundromat.get("transaction_progress").getAsString();
                    String transactionDone = laundromat.get("transaction_done").getAsString();

                    revenue.setText(revenueStats);
                    totalTransactions.setText(transactionsStats);
                    tPending.setText(transactionPending);
                    tProg.setText(transactionProgress);
                    tDone.setText(transactionDone);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}