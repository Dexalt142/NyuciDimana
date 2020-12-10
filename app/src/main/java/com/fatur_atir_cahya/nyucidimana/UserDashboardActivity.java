package com.fatur_atir_cahya.nyucidimana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.fatur_atir_cahya.nyucidimana.database.SessionManager;

public class UserDashboardActivity extends AppCompatActivity {

    TextView name, email, role, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        name = findViewById(R.id.dash_name);
        email = findViewById(R.id.dash_email);
        role = findViewById(R.id.dash_role);
        token = findViewById(R.id.dash_token);

        SessionManager sessionManager = new SessionManager(this);

        name.setText(sessionManager.getName());
        email.setText(sessionManager.getEmail());
        role.setText(sessionManager.getRole());
        token.setText(sessionManager.getToken());
    }
}