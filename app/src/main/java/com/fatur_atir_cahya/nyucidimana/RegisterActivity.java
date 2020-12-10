package com.fatur_atir_cahya.nyucidimana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fatur_atir_cahya.nyucidimana.api.ApiClient;
import com.fatur_atir_cahya.nyucidimana.api.model.Register;
import com.fatur_atir_cahya.nyucidimana.api.service.AuthInterface;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button registerButton;
    TextView toLogin;
    EditText nameField, emailField, passwordField, phoneField;
    CheckBox registerAsOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        toLogin = findViewById(R.id.to_login_button);
        registerButton = findViewById(R.id.register_button);
        nameField = findViewById(R.id.register_name);
        emailField = findViewById(R.id.register_email);
        passwordField = findViewById(R.id.register_password);
        phoneField = findViewById(R.id.register_phone);
        registerAsOwner = findViewById(R.id.register_owner);

        AuthInterface authInterface = ApiClient.getApiClient().create(AuthInterface.class);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String phone = phoneField.getText().toString();
                String role = "0";
                if(registerAsOwner.isChecked()) {
                    role = "1";
                }

                Register register = new Register(name, email, password, phone, role);
                disableForm();

                Call<JsonObject> callRegister = authInterface.register(register);
                callRegister.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        int responseCode = response.code();
                        if(responseCode == 200) {
                            Toast.makeText(getApplicationContext(), "Register berhasil", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                JSONObject errorObject = new JSONObject(response.errorBody().string());
                                Log.i("API_RESPONSE", errorObject.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), "Register gagal", Toast.LENGTH_LONG).show();
                        }

                        enableForm();
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }
                });
            }
        });

    }

    public void enableForm() {
        registerButton.setText(R.string.register);
        registerButton.setEnabled(true);
        nameField.setEnabled(true);
        emailField.setEnabled(true);
        passwordField.setEnabled(true);
        phoneField.setEnabled(true);
        registerAsOwner.setEnabled(true);
    }

    public void disableForm() {
        registerButton.setText(R.string.register_loading);
        registerButton.setEnabled(false);
        nameField.setEnabled(false);
        emailField.setEnabled(false);
        passwordField.setEnabled(false);
        phoneField.setEnabled(false);
        registerAsOwner.setEnabled(false);
    }
}