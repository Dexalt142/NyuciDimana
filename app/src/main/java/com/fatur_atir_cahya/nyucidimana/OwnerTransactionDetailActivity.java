package com.fatur_atir_cahya.nyucidimana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.Transaction;
import com.fatur_atir_cahya.nyucidimana.api.service.LaundromatInterface;
import com.fatur_atir_cahya.nyucidimana.api.service.TransactionInterface;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerTransactionDetailActivity extends AppCompatActivity {

    SessionManager sessionManager;
    TransactionInterface transactionInterface;

    Transaction transaction;
    TextView code, startDate, endDate, price, weight, userName, userPhone;
    LinearLayout userContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_transaction_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        code = findViewById(R.id.owner_detail_code);
        startDate = findViewById(R.id.owner_detail_start_date);
        endDate = findViewById(R.id.owner_detail_end_date);
        price = findViewById(R.id.owner_detail_price);
        weight = findViewById(R.id.owner_detail_weight);
        userName = findViewById(R.id.owner_detail_username);
        userPhone = findViewById(R.id.owner_detail_phone);
        userContainer = findViewById(R.id.owner_detail_user);

        sessionManager = new SessionManager(this);
        transactionInterface = ApiClient.getApiClient().create(TransactionInterface.class);

        transaction = getIntent().getParcelableExtra("transaction");

        code.setText(transaction.getTransactionCode());
        startDate.setText(transaction.getStartDate());
        endDate.setText(transaction.getEndDate() != null ? transaction.getEndDate() : "-");
        price.setText(String.valueOf(transaction.getPrice()));
        weight.setText(transaction.getWeight() + " Kg");

        Call<JsonObject> callTransaction = transactionInterface.getTransaction("Bearer " + sessionManager.getToken(), transaction.getId());

        callTransaction.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.code() == 200) {
                    JsonObject responseBody = response.body().getAsJsonObject("data");
                    if(!responseBody.get("user").isJsonNull()) {
                        JsonObject user = responseBody.getAsJsonObject("user");
                        String name = user.get("name").getAsString();
                        String phone = user.get("phone_number").getAsString();

                        userContainer.setVisibility(View.VISIBLE);
                        userName.setText(name);
                        userPhone.setText(phone);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}