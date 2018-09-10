package com.yourapp.name;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.yourapp.name.models.UserLoginModel;
import com.yourapp.name.models.UserLoginResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseClass {

    private RelativeLayout mLoadingView;
    private EditText mEmailView;
    private EditText mPasswordView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        thread_run = false;

        mLoadingView = findViewById(R.id.loading);
        mEmailView = findViewById(R.id.email);
        mPasswordView = findViewById(R.id.password);
        Button mButtonLogin = findViewById(R.id.email_sign_in_button);
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Alert.Show(LoginActivity.this, "Username and password are must not be empty.", "Login error", "Ok");
                } else if (!isEmailValid(email)) {
                    Alert.Show(LoginActivity.this, "Please enter a valid e-mail address.", "Login error", "Ok");
                } else if (!isPasswordValid(password)) {
                    Alert.Show(LoginActivity.this, "Please enter a valid password.", "Login error", "Ok");
                } else {
                    mLoadingView.setVisibility(View.VISIBLE);
                    UserLoginModel userloginmodel = new UserLoginModel(email, password);

                    Call<UserLoginResponseModel> call = apiInterface.userLogin(userloginmodel);
                    call.enqueue(new Callback<UserLoginResponseModel>() {
                        @Override
                        public void onResponse(Call<UserLoginResponseModel> call, Response<UserLoginResponseModel> response) {
                            UserLoginResponseModel responseResult = response.body();
                            if (responseResult != null && responseResult.result == 1) {
                                //Login success
                                SaveLoginInfo(responseResult.usertype, email, password);
                                //Redirect
                                if(responseResult.usertype==1){
                                    RedirectUser();
                                }
                                else{
                                    RedirectCustomer();
                                }
                            } else {
                                //Login failed
                                mLoadingView.setVisibility(View.GONE);
                                Alert.Show(LoginActivity.this, "User information not found. Please make sure you have entered informations correctly.", "Login error", "Ok");
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLoginResponseModel> call, Throwable t) {
                            mLoadingView.setVisibility(View.GONE);
                            Alert.Show(LoginActivity.this, "Unable to login: " + t.getMessage(), "Login error", "Ok");
                            call.cancel();
                        }
                    });
                }
            }
        });
    }

}
