package com.softwarica.printstation.ui.auth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.softwarica.printstation.R;
import com.softwarica.printstation.api.API;
import com.softwarica.printstation.api.response.ApiResponse;
import com.softwarica.printstation.api.response.ErrorResponse;
import com.softwarica.printstation.api.response.SignUpResponse;
import com.softwarica.printstation.entity.UserEntity;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText editTextFirstName, editTextLastName, editTextAddress, editPhone, editTextEmail, editTextPassword, editTextConfirmPassword;
    private TextInputLayout editTextFirstNameTIL, editTextLastNameTIL, editTextAddressTIL, editTextPhoneTIL, editTextEmailTIL, editTextPasswordTIL, editTextConfirmPasswordTIL;


    private ImageView userProfileImageView;
    private MaterialButton signUpButton;
    private TextView loginLink;

    private Toolbar toolbar;
    private ProgressDialog progressDialog;

    private String profileImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initUI();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.loginLink: {
                finish();
                //startActivity(new Intent(this, LoginActivity.class));
                break;
            }
            case R.id.btnSignUp: {
                String firstName = editTextFirstName.getText().toString();
                String lastName = editTextLastName.getText().toString();
                String address = editTextAddress.getText().toString();
                String phone = editPhone.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();

                UserEntity user = new UserEntity();
                user.setProfileImage(encodeImageToBase64(profileImagePath));
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setPhone(phone);
                user.setAddress(address);
                user.setEmail(email);
                user.setPassword(password);
                user.setConfirmPassword(confirmPassword);
                registerUser(user);
                break;
            }
            case R.id.profileImage: {
                if (askPermission()) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, 1);
                }
            }
        }
    }

    public boolean askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return false;
        }
        return true;
    }


    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),
                uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    private String encodeImageToBase64(String path) {
        try {
            InputStream inputStream = new FileInputStream(path);
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int progress = 0;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                progress += buffer.length;
            }
            return "data:image/" + getFileExtension(path) + ";base64," + Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    private String getFileExtension(String name) {
        return android.webkit.MimeTypeMap.getFileExtensionFromUrl(name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Please select an image ", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri = data.getData();
                userProfileImageView.setImageURI(uri);
                profileImagePath = getRealPathFromUri(uri);
            }
        } else {
            askPermission();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");

        userProfileImageView = findViewById(R.id.profileImage);

        editTextFirstName = findViewById(R.id.firstName);
        editTextLastName = findViewById(R.id.lastName);
        editTextAddress = findViewById(R.id.address);
        editPhone = findViewById(R.id.phone);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.confirmPassword);

        editTextFirstNameTIL = findViewById(R.id.firstNameTIL);
        editTextLastNameTIL = findViewById(R.id.lastNameTIL);
        editTextAddressTIL = findViewById(R.id.addressTIL);
        editTextPhoneTIL = findViewById(R.id.phoneTIL);
        editTextEmailTIL = findViewById(R.id.emailTIL);
        editTextPasswordTIL = findViewById(R.id.passwordTIL);
        editTextConfirmPasswordTIL = findViewById(R.id.confirmPasswordTIL);

        signUpButton = findViewById(R.id.btnSignUp);
        loginLink = findViewById(R.id.loginLink);


        userProfileImageView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        loginLink.setOnClickListener(this);
    }

    private void resetUI() {
        editTextFirstName.setText(null);
        editTextLastName.setText(null);
        editTextAddress.setText(null);
        editPhone.setText(null);
        editTextEmail.setText(null);
        editTextPassword.setText(null);
        editTextConfirmPassword.setText(null);
    }

    private boolean validate(UserEntity userEntity) {
        if (TextUtils.isEmpty(userEntity.getProfileImage())) {
            Toast.makeText(this, "Profile image is required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(userEntity.getFirstName())) {
            editTextFirstNameTIL.setError("Enter a first name");
            editTextFirstNameTIL.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(userEntity.getLastName())) {
            editTextLastNameTIL.setError("Enter a last name");
            editTextLastNameTIL.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(userEntity.getAddress())) {
            editTextAddressTIL.setError("Enter your address");
            editTextAddressTIL.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(userEntity.getPhone())) {
            editTextPhoneTIL.setError("Enter your phone number");
            editPhone.requestFocus();
            return false;
        }

        if (userEntity.getPhone().length() != 10) {
            editTextPhoneTIL.setError("Phone number is invalid");
            editPhone.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(userEntity.getEmail())) {
            editTextEmailTIL.setError("Enter a email");
            editTextEmailTIL.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userEntity.getEmail()).matches()) {
            editTextEmailTIL.setError("Invalid email format");
            editTextEmailTIL.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(userEntity.getPassword())) {
            editTextPasswordTIL.setError("Enter a password");
            editTextPasswordTIL.requestFocus();
            return false;
        }

        return true;
    }

    private void registerUser(UserEntity userEntity) {
        if (!validate(userEntity)) return;

        progressDialog.show();
        API.service().registerUser(userEntity).enqueue(new Callback<ApiResponse<List<ErrorResponse>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<ErrorResponse>>> call, @NonNull Response<ApiResponse<List<ErrorResponse>>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    resetUI();
                    SignUpActivity.this.finish();
                } else {
                    Toast.makeText(SignUpActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<ErrorResponse>>> call, @NonNull Throwable t) {
                Toast.makeText(SignUpActivity.this, "Internal server error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
