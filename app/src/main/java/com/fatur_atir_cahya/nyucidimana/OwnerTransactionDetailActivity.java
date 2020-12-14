package com.fatur_atir_cahya.nyucidimana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.Transaction;
import com.fatur_atir_cahya.nyucidimana.api.model.UpdateTransaction;
import com.fatur_atir_cahya.nyucidimana.api.service.LaundromatInterface;
import com.fatur_atir_cahya.nyucidimana.api.service.TransactionInterface;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerTransactionDetailActivity extends AppCompatActivity {

    SessionManager sessionManager;
    TransactionInterface transactionInterface;

    Transaction transaction;
    TextView code, startDate, endDate, price, weight, userName, userPhone, status;
    LinearLayout userContainer, buttonContainer;
    Button actionButton;
    ImageView qrCode;

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
        buttonContainer = findViewById(R.id.button_container);
        actionButton = findViewById(R.id.transaction_action);
        status = findViewById(R.id.owner_detail_status);
        qrCode = findViewById(R.id.owner_detail_qr);

        sessionManager = new SessionManager(this);
        transactionInterface = ApiClient.getApiClient().create(TransactionInterface.class);

        transaction = getIntent().getParcelableExtra("transaction");

        code.setText(transaction.getTransactionCode());
        startDate.setText(transaction.getStartDate());
        endDate.setText(transaction.getEndDate() != null ? transaction.getEndDate() : "-");
        price.setText(transaction.getPrice());
        weight.setText(transaction.getWeight());

        String qrContent = transaction.getId() + "_" + transaction.getTransactionCode();
        String qrBase64 = Base64.encodeToString(qrContent.getBytes(), Base64.DEFAULT);
        qrCode.setImageBitmap(generateQrCode(qrBase64));

        String tStatus = transaction.getStatus();
        if(tStatus.equals("0")) {
            status.setText("Pending");
            status.setBackground(getResources().getDrawable(R.drawable.status_pending));
            status.setTextColor(getResources().getColor(R.color.colorWhite));

            actionButton.setText(R.string.proses);
            actionButton.setBackground(getResources().getDrawable(R.drawable.button_primary));
        } else if(tStatus.equals("1")) {
            status.setText("Diproses");
            status.setBackground(getResources().getDrawable(R.drawable.status_progress));
            status.setTextColor(getResources().getColor(R.color.colorWhite));

            actionButton.setText(R.string.selesai);
            actionButton.setBackground(getResources().getDrawable(R.drawable.button_success));
        } else if(tStatus.equals("2")) {
            status.setText("Selesai");
            status.setBackground(getResources().getDrawable(R.drawable.status_done));
            status.setTextColor(getResources().getColor(R.color.colorWhite));

            buttonContainer.setVisibility(View.GONE);
        }

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

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateStatus = "";
                if(transaction.getStatus().equals("0")) {
                    updateStatus = "process";
                } else if(transaction.getStatus().equals("1")) {
                    updateStatus = "done";
                }
                UpdateTransaction updateTransaction = new UpdateTransaction(transaction.getId(), transaction.getTransactionCode(), updateStatus);
                Call<JsonObject> callUpdate = transactionInterface.updateTransaction("Bearer " + sessionManager.getToken(), updateTransaction);

                callUpdate.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if(response.code() == 200) {
                            String successMessage = "";
                            if(transaction.getStatus().equals("0")) {
                                successMessage = "Transaksi diproses";
                            } else if(transaction.getStatus().equals("1")) {
                                successMessage = "Transaksi selesai";
                            }
                            Toast.makeText(view.getContext(), successMessage, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Gagal mengganti status", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Gagal mengganti status", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public Bitmap generateQrCode(String content) {
        int width = 200;
        int height = 200;
        QRCodeWriter qrWriter = new QRCodeWriter();
        Bitmap qr = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        try {
            BitMatrix bitMatrix = qrWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

            for(int i = 0; i < width; i++) {
                for(int y = 0; y < height; y++) {
                    qr.setPixel(i, y, bitMatrix.get(i, y) ? Color.BLACK : Color.WHITE);
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return qr;
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