package com.fatur_atir_cahya.nyucidimana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.service.AuthInterface;
import com.fatur_atir_cahya.nyucidimana.database.LaundromatManager;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerDashboardActivity extends AppCompatActivity {

    SessionManager sessionManager;
    LaundromatManager laundromatManager;
    AuthInterface authInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_dashboard);

        authInterface = ApiClient.getApiClient().create(AuthInterface.class);
        sessionManager = new SessionManager(this);
        laundromatManager = new LaundromatManager(this);

        Fragment firstFragment = new OwnerHomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.owner_fragment_container, firstFragment).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.owner_bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                switch (item.getItemId()) {
                    case R.id.owner_home:
                        selected = new OwnerHomeFragment();
                        break;

                    case R.id.owner_list:
                        selected = new OwnerTransactionFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.owner_fragment_container, selected).commit();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.owner_top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.owner_logout_menu) {
            Call<JsonObject> callLogout = authInterface.logout("Bearer " + sessionManager.getToken());

            callLogout.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(response.code() == 200) {
                        sessionManager.clearUser();
                        laundromatManager.clearLaundromat();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Logout berhasil", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {

                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}