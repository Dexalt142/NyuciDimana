package com.fatur_atir_cahya.nyucidimana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.CreateTransaction;
import com.fatur_atir_cahya.nyucidimana.api.service.TransactionInterface;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerCreateTransactionActivity extends AppCompatActivity {

    EditText priceField, weightField;
    Button createButton;

    SessionManager sessionManager;
    TransactionInterface transactionInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_create_transaction);

        getSupportActionBar().hide();

        weightField = findViewById(R.id.create_transaction_weight);
        priceField = findViewById(R.id.create_transaction_price);
        createButton = findViewById(R.id.create_transaction_button);

        sessionManager = new SessionManager(this);
        transactionInterface = ApiClient.getApiClient().create(TransactionInterface.class);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = weightField.getText().toString();
                String price = priceField.getText().toString();

                if(!price.isEmpty() && !weight.isEmpty()) {
                    CreateTransaction createTransaction = new CreateTransaction(weight, price);

                    Call<JsonObject> callCreateTransaction = transactionInterface.createTransaction("Bearer " + sessionManager.getToken(), createTransaction);
                    disableForm();
                    callCreateTransaction.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if(response.code() == 200) {
                                Toast.makeText(view.getContext(), "Berhasil membuat transaksi", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            enableForm();
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            enableForm();
                        }
                    });
                } else {
                    Toast.makeText(view.getContext(), "Gagal membuat transaksi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void enableForm() {
        createButton.setText(R.string.buat_transaksi);
        createButton.setEnabled(true);
        priceField.setEnabled(true);
        weightField.setEnabled(true);
    }

    public void disableForm() {
        createButton.setText(R.string.login_loading);
        createButton.setEnabled(false);
        priceField.setEnabled(false);
        weightField.setEnabled(false);
    }

}