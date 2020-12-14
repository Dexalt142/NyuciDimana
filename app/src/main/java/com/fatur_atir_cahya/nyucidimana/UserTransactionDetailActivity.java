package com.fatur_atir_cahya.nyucidimana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.Transaction;
import com.fatur_atir_cahya.nyucidimana.api.service.LaundromatInterface;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;

public class UserTransactionDetailActivity extends AppCompatActivity {

    SessionManager sessionManager;
    LaundromatInterface laundromatInterface;

    Transaction transaction;
    TextView code, startDate, endDate, price, weight, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_transaction_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionManager = new SessionManager(this);
        laundromatInterface = ApiClient.getApiClient().create(LaundromatInterface.class);

        code = findViewById(R.id.user_detail_code);
        startDate = findViewById(R.id.user_detail_start_date);
        endDate = findViewById(R.id.user_detail_end_date);
        price = findViewById(R.id.user_detail_price);
        weight = findViewById(R.id.user_detail_weight);
        status = findViewById(R.id.user_detail_status);

        transaction = getIntent().getParcelableExtra("transaction");

        code.setText(transaction.getTransactionCode());
        startDate.setText(transaction.getStartDate());
        endDate.setText(transaction.getEndDate() != null ? transaction.getEndDate() : "-");
        price.setText(String.valueOf(transaction.getPrice()));
        weight.setText(transaction.getWeight() + " Kg");

        String tStatus = transaction.getStatus();
        if(tStatus.equals("0")) {
            status.setText("Pending");
            status.setBackground(getResources().getDrawable(R.drawable.status_pending));
            status.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if(tStatus.equals("1")) {
            status.setText("Diproses");
            status.setBackground(getResources().getDrawable(R.drawable.status_progress));
            status.setTextColor(getResources().getColor(R.color.colorWhite));
        } else if(tStatus.equals("2")) {
            status.setText("Selesai");
            status.setBackground(getResources().getDrawable(R.drawable.status_done));
            status.setTextColor(getResources().getColor(R.color.colorWhite));
        }
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