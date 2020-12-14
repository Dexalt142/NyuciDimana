package com.fatur_atir_cahya.nyucidimana;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fatur_atir_cahya.nyucidimana.adapter.TransactionAdapter;
import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.Transaction;
import com.fatur_atir_cahya.nyucidimana.api.service.TransactionInterface;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserTransactionFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Transaction> transactionList = new ArrayList<>();
    TransactionAdapter transactionAdapter;
    SessionManager sessionManager;
    TransactionInterface transactionInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_transaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(getActivity());
        transactionInterface = ApiClient.getApiClient().create(TransactionInterface.class);

        recyclerView = view.findViewById(R.id.user_transaction_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getTransactions();
    }

    private void getTransactions() {
        Call<JsonObject> callTransaction = transactionInterface.getUserTransactions("Bearer " + sessionManager.getToken());

        callTransaction.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200) {
                    JsonArray transactions = response.body().getAsJsonArray("data");
                    getTransactionFromJsonArray(transactions);
                    transactionAdapter = new TransactionAdapter(transactionList);
                    recyclerView.setAdapter(transactionAdapter);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

    }

    private void getTransactionFromJsonArray(JsonArray transactionArray) {
        for(int i = 0; i < transactionArray.size(); i++) {
            JsonObject el = transactionArray.get(i).getAsJsonObject();
            Transaction transaction = new Transaction(el);
            transactionList.add(transaction);
        }
    }
}