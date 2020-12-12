package com.fatur_atir_cahya.nyucidimana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.Laundromat;
import com.fatur_atir_cahya.nyucidimana.api.service.LaundromatInterface;
import com.fatur_atir_cahya.nyucidimana.database.LaundromatManager;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterLaundromatActivity extends AppCompatActivity {

    Button registerButton;
    EditText nameField, addressField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_laundromat);

        getSupportActionBar().hide();

        registerButton = findViewById(R.id.laundromat_register_button);
        nameField = findViewById(R.id.laundromat_name);
        addressField = findViewById(R.id.laundromat_address);

        SessionManager sessionManager = new SessionManager(this);
        LaundromatManager laundromatManager = new LaundromatManager(this);

        LaundromatInterface laundromatInterface = ApiClient.getApiClient().create(LaundromatInterface.class);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameField.getText().toString();
                String address = addressField.getText().toString();
                String latitude = "-6.921881";
                String longitude = "107.607355";

                Laundromat laundromat = new Laundromat(name, address, latitude, longitude);

                Call<JsonObject> createLaundromat = laundromatInterface.createLaundromat("Bearer " + sessionManager.getToken(), laundromat);
                createLaundromat.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        int responseCode = response.code();
                        if(responseCode == 200) {
                            Call<JsonObject> getLaundromat = laundromatInterface.getLaundromat("Bearer " + sessionManager.getToken());
                            getLaundromat.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> laundromatResponse) {
                                    int laundromatResponseCode = laundromatResponse.code();
                                    if(laundromatResponseCode == 200) {
                                        JsonObject laundromat = laundromatResponse.body().getAsJsonObject("data");
                                        String laundromatId = laundromat.get("id").getAsString();
                                        String laundromatName = laundromat.get("name").getAsString();
                                        String laundromatAddress = laundromat.get("address").getAsString();
                                        String laundromatLatitude = laundromat.get("latitude").getAsString();
                                        String laundromatLongitude = laundromat.get("longitude").getAsString();

                                        laundromatManager.saveLaundromat(laundromatId, laundromatName, laundromatAddress, laundromatLatitude, laundromatLongitude);

                                        startActivity(new Intent(getApplicationContext(), OwnerDashboardActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                }
                            });

                            Toast.makeText(getApplicationContext(), "Tempat laundry berhasil didaftarkan", Toast.LENGTH_LONG).show();
                        } else if(responseCode == 400) {
                            Toast.makeText(getApplicationContext(), "Tempat laundry gagal didaftarkan", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });
    }

}