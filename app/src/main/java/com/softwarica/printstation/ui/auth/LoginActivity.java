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

    private PrefManager prefManager;

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
            case R.id.signUpButton: {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                //VIBRATION IN USE
                vibrator.vibrate(50);
                break;
            }
            case R.id.loginButton: {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if (validate(email, password)) loginUser(email, password);
                //VIBRATION IN USE
                vibrator.vibrate(50);
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

    private void loginUser(String email, String password) {
        progressDialog.show();
        API.service().login(email, password).enqueue(new Callback<ApiResponse<LoginResponse>>() {

            @Override
            public void onResponse(@NonNull Call<ApiResponse<LoginResponse>> call, @NonNull Response<ApiResponse<LoginResponse>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && null != response.body()){

                    // save the token locally
                    ((PrintStationApplication)getApplication()).getPrefManager().setToken(response.body().getData().getToken());

                    // notify the user
                    ((PrintStationApplication)getApplication())
                            .getNotificationUtil()
                            .showNotification("Login", "You have been successfully logged in to PrintStation Nepal");

                    // navigate to dashboard
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Either email or password is incorrect", Toast.LENGTH_SHORT).show();
                }

            }


            @Override
            public void onFailure(@NonNull Call<ApiResponse<LoginResponse>> call, @NonNull Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "Either email or password is incorrect", Toast.LENGTH_SHORT).show();
                editTextEmail.requestFocus();
            }
        });

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
