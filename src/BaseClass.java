package com.yourapp.name;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.yourapp.name.api.APIClient;
import com.yourapp.name.api.APIInterface;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint("Registered")
public class BaseClass extends AppCompatActivity {

    public BaseClass(){
        apiInterface = APIClient.getClient().create(APIInterface.class);
    }
    public APIInterface apiInterface;
    public static int toast_length = Toast.LENGTH_SHORT;
    protected boolean thread_run = true;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isEmailValid(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    protected boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }
    protected void SaveLoginInfo(int usertype, String email, String password) {
        SharedPreferences sp = getSharedPreferences("LoginInfo", MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.putString("utyp", String.valueOf(usertype));
        Ed.putString("emil", email);
        Ed.putString("pswd", password);
        Ed.apply();
    }

    protected String GetSavedLoginUserType() {
        SharedPreferences sp1 = this.getSharedPreferences("LoginInfo", MODE_PRIVATE);
        String utyp = sp1.getString("utyp", null);
        return utyp != null ? utyp : "0";
    }

    protected String GetSavedLoginEmail() {
        SharedPreferences sp1 = this.getSharedPreferences("LoginInfo", MODE_PRIVATE);
        String emil = sp1.getString("emil", null);
        return emil != null ? emil : "";
    }

    protected String GetSavedLoginPassword() {
        SharedPreferences sp1 = this.getSharedPreferences("LoginInfo", MODE_PRIVATE);
        String pswd = sp1.getString("pswd", null);
        return pswd != null ? pswd : "";
    }

    protected void LogOut(Context ctx){

        thread_run = false;
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ctx);
        builder1.setMessage("Are you sure want to logout?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        SaveLoginInfo(0,"","");
                        RedirectLogin();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    protected void SetToolbar(Toolbar toolbar){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorAccent));
        toolbar.setSubtitle(R.string.app_url);
        toolbar.setLogo(R.drawable.logo_about2);
    }

    protected void RedirectLogin(){
        Intent LoginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(LoginIntent);
        finish();
    }

    protected void RedirectUser(){
        Intent UserIntent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(UserIntent);
        finish();
    }

    protected void RedirectCustomer(){
        Intent CustomerIntent = new Intent(getApplicationContext(), CustomerActivity.class);
        startActivity(CustomerIntent);
        finish();
    }
    protected void RedirectCustomerPassword(){
        Intent CustomerPasswordIntent = new Intent(getApplicationContext(), CustomerPasswordActivity.class);
        startActivity(CustomerPasswordIntent);
        finish();
    }

    public static String PhoneNumberClear(String number){
        return number.replace(" ", "").replace("(", "").replace(")", "");
    }

    public void ThreadToast(final String text){
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), text, toast_length).show();
            }
        });
    }
}
