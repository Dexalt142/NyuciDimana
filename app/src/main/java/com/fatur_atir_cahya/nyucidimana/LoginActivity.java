package com.fatur_atir_cahya.nyucidimana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.Login;
import com.fatur_atir_cahya.nyucidimana.api.service.AuthInterface;
import com.fatur_atir_cahya.nyucidimana.api.service.LaundromatInterface;
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button button;
    TextView toRegister;
    EditText emailField;
    EditText passwordField;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        AuthInterface authInterface = ApiClient.getApiClient().create(AuthInterface.class);
        SessionManager sessionManager = new SessionManager(this);

        button = findViewById(R.id.login_button);
        emailField = findViewById(R.id.login_email);
        passwordField = findViewById(R.id.login_password);
        toRegister = findViewById(R.id.to_register_button);

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                Login login = new Login(email, password);
                disableForm();

                Call<JsonObject> callLogin = authInterface.login(login);
                callLogin.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        int responseCode = response.code();
                        if(responseCode == 200) {
                            token = response.body().getAsJsonObject("data").get("token").getAsString();

                            Call<JsonObject> callUser = authInterface.user("Bearer " + token);
                            callUser.enqueue(new Callback<JsonObject>() {
                                @Override
                                public void onResponse(Call<JsonObject> call, Response<JsonObject> userResponse) {
                                    int userResponseCode = userResponse.code();
                                    if(userResponseCode == 200) {
                                        JsonObject data = userResponse.body().getAsJsonObject("data");
                                        String userName = data.get("name").getAsString();
                                        String userEmail = data.get("email").getAsString();
                                        String userRole = data.get("role").getAsString();

                                        sessionManager.saveUser(userName, userEmail, userRole, token);

                                        if(userRole.equals("0")) {
                                            startActivity(new Intent(getApplicationContext(), UserDashboardActivity.class));
                                            finish();
                                        } else if(userRole.equals("1")) {
                                            LaundromatInterface laundromatInterface = ApiClient.getApiClient().create(LaundromatInterface.class);
                                            Call<JsonObject> getLaundromat = laundromatInterface.getLaundromat("Bearer " + token);

                                            getLaundromat.enqueue(new Callback<JsonObject>() {
                                                @Override
                                                public void onResponse(Call<JsonObject> call, Response<JsonObject> laundromatResponse) {
                                                    int laundromatResponseCode = laundromatResponse.code();

                                                    if(laundromatResponseCode == 200) {
                                                        startActivity(new Intent(getApplicationContext(), UserDashboardActivity.class));
                                                        finish();
                                                    } else if(laundromatResponseCode == 404) {
                                                        startActivity(new Intent(getApplicationContext(), RegisterLaundromatActivity.class));
                                                        finish();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<JsonObject> call, Throwable t) {

                                                }
                                            });
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<JsonObject> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Email atau Password salah", Toast.LENGTH_LONG).show();
                                    enableForm();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Email atau Password salah", Toast.LENGTH_LONG).show();
                            enableForm();
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.i("API_ERROR", t.getLocalizedMessage());
                        enableForm();
                    }
                });
            }
        });
    }

    public void enableForm() {
        button.setText(R.string.login);
        button.setEnabled(true);
        emailField.setEnabled(true);
        passwordField.setEnabled(true);
    }

    public void disableForm() {
        button.setText(R.string.login_loading);
        button.setEnabled(false);
        emailField.setEnabled(false);
        passwordField.setEnabled(false);
    }
}