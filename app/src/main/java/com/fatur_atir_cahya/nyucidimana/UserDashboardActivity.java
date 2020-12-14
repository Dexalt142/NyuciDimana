package com.fatur_atir_cahya.nyucidimana;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.AttachTransaction;
import com.fatur_atir_cahya.nyucidimana.api.service.AuthInterface;
import com.fatur_atir_cahya.nyucidimana.api.service.TransactionInterface;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDashboardActivity extends AppCompatActivity {

    SessionManager sessionManager;
    TransactionInterface transactionInterface;
    AuthInterface authInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        authInterface = ApiClient.getApiClient().create(AuthInterface.class);
        transactionInterface = ApiClient.getApiClient().create(TransactionInterface.class);
        sessionManager = new SessionManager(this);

        Fragment firstFragment = new UserMapsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment_container, firstFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.user_bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                switch (item.getItemId()) {
                    case R.id.user_home:
                        selected = new UserMapsFragment();
                        break;

                    case R.id.user_list:
                        selected = new UserTransactionFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.user_fragment_container, selected).commit();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.user_logout_menu) {
            Call<JsonObject> callLogout = authInterface.logout("Bearer " + sessionManager.getToken());

            callLogout.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.code() == 200) {
                        sessionManager.clearUser();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Logout berhasil", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        } else if(item.getItemId() == R.id.user_scan_menu) {
            scanQrCode();
        }

        return super.onOptionsItemSelected(item);
    }

    private void scanQrCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureQrActivity.class);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() != null) {
                String rawContent = new String(Base64.decode(result.getContents().getBytes(), Base64.DEFAULT), StandardCharsets.UTF_8);
                String[] qrContent = rawContent.split("_");
                if(qrContent.length == 2) {
                    AttachTransaction attachTransaction = new AttachTransaction(qrContent[0], qrContent[1]);
                    Call<JsonObject> callAttach = transactionInterface.attachTransaction("Bearer " + sessionManager.getToken(), attachTransaction);

                    callAttach.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if(response.code() == 200) {
                                Toast.makeText(UserDashboardActivity.this, "Berhasil menambahkan transaksi", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(UserDashboardActivity.this, "Gagal menambahkan transaksi", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Toast.makeText(UserDashboardActivity.this, "Gagal menambahkan transaksi", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(this, "Qr code tidak valid", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}