package com.softwarica.printstation.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.softwarica.printstation.PrintStationApplication;
import com.softwarica.printstation.api.response.ApiResponse;
import com.softwarica.printstation.api.response.LoginResponse;
import com.softwarica.printstation.storage.PrefManager;
import com.softwarica.printstation.ui.dashboard.DashboardActivity;
import com.softwarica.printstation.R;
import com.softwarica.printstation.api.API;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {



    private TextInputEditText editTextEmail;
    private TextInputEditText editTextPassword;
    private Button buttonLogin;
    private Button btnSignUp;
    Vibrator vibrator;

    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        initUI();
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.loginButton: {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();


                break;
            }
        }
    }


    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.loginButton);
        btnSignUp = findViewById(R.id.signUpButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");

        btnSignUp.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }


    private boolean validate(String email, String password) {

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Enter a email");
            editTextEmail.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Invalid email format");
            editTextEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return false;
        }

        return true;
    }
}
