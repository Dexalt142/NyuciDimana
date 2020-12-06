package com.fatur_atir_cahya.nyucidimana;

import androidx.appcompat.app.AppCompatActivity;

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
import com.fatur_atir_cahya.nyucidimana.database.SessionManager;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button button;
    EditText emailField;
    EditText passwordField;

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
                            String token = response.body().getAsJsonObject("data").get("token").toString();
                            sessionManager.saveUser("email@user.com", "role", token);
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