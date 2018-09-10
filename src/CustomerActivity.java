package com.yourapp.name;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yourapp.name.models.CustomerInfoModel;
import com.yourapp.name.models.CustomerInfoResponseModel;
import com.yourapp.name.models.DeviceDetailModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerActivity extends BaseClass {
    private TextView mCustomerNameSurname;
    private TextView mCustomerEMail;
    private TextView mCustomerPhone;
    private TextView mCustomerAddress;
    private TextView mCustomerAddress2;
    private TextView mCustomerLatitude;
    private TextView mCustomerLongitude;
    private Button mLocationButton;
    private List<DeviceDetailModel> deviceListRecycler;
    private RecyclerView mCustomerDevicesRecycler;

    private int savedUserType;
    private String savedEmail;
    private String savedPassword;

    private Thread thread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        Toolbar toolbar = findViewById(R.id.toolbar);
        SetToolbar(toolbar);

        mCustomerNameSurname = findViewById(R.id.customerNameSurname);
        mCustomerEMail = findViewById(R.id.customerEMail);
        mCustomerPhone = findViewById(R.id.customerPhone);
        mCustomerAddress = findViewById(R.id.customerAddress);
        mCustomerAddress2 = findViewById(R.id.customerAddress2);
        mCustomerLatitude = findViewById(R.id.customerLatitude);
        mCustomerLongitude = findViewById(R.id.customerLongitude);
        mLocationButton=findViewById(R.id.location_button);
        mCustomerDevicesRecycler = findViewById(R.id.customerDevicesRecycler);


        String savedUserTypeStr = GetSavedLoginUserType();
        savedUserType = savedUserTypeStr != null ? Integer.valueOf(savedUserTypeStr) : 0;
        savedEmail = GetSavedLoginEmail();
        savedPassword = GetSavedLoginPassword();


        //Retrieve data every 15 seconds
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i =0;
                while (thread_run){
                    UpdateData();

                    try {
                        Thread.sleep(15000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }
        });
        thread.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        thread_run = true;
        if (!thread.isAlive()) {
            thread.start();
        }
    }

    @Override
    public void onBackPressed() {

        if(thread.isAlive()){
            thread.interrupt();
        }
        thread_run = false;
        finish();
    }

    private void UpdateData(){
        if (savedUserType > 0 && !savedEmail.isEmpty() && !savedPassword.isEmpty()) {

            CustomerInfoModel customerInfoModel = new CustomerInfoModel(savedUserType, savedEmail, savedPassword);

            Call<CustomerInfoResponseModel> call = apiInterface.customerInfo(customerInfoModel);
            call.enqueue(new Callback<CustomerInfoResponseModel>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<CustomerInfoResponseModel> call, Response<CustomerInfoResponseModel> response) {
                    CustomerInfoResponseModel responseResult = response.body();
                    if (responseResult != null && responseResult.result == 1) {
                        //Login success
                        mCustomerNameSurname.setText(responseResult.customer.firstname + " " + responseResult.customer.lastname);
                        mCustomerEMail.setText(responseResult.customer.email);
                        String customerPhone = responseResult.customer.phone1;
                        if (responseResult.customer.phone2 != null && responseResult.customer.phone2!="") {
                            customerPhone+=" - "+responseResult.customer.phone2;
                        }
                        mCustomerPhone.setText(customerPhone);
                        String customerAddress = responseResult.customer.address1;
                        if (responseResult.customer.address2!= null && responseResult.customer.address2!="") {
                            customerAddress+=" "+responseResult.customer.address2;
                        }
                        mCustomerAddress.setText(customerAddress);
                        mCustomerAddress2.setText(responseResult.customer.city + "/" + responseResult.customer.town);
                        if (responseResult.customer.latitude != null && responseResult.customer.latitude!=""
                                &&responseResult.customer.longitude != null && responseResult.customer.longitude!=""){
                            mCustomerLatitude.setText(responseResult.customer.latitude);
                            mCustomerLongitude.setText(responseResult.customer.longitude);
                            mLocationButton.setVisibility(View.VISIBLE);

                        }
                        /*Get devices list*/
                        deviceListRecycler = new ArrayList<>();
                        int listLength = responseResult.devices.size();
                        if(listLength>0) {
                            for (int i = 0; i < listLength; i++) {
                                deviceListRecycler.add(responseResult.devices.get(i));
                            }
                            DeviceAdapter recyclerAdapter = new DeviceAdapter(deviceListRecycler, CustomerActivity.this);
                            RecyclerView.LayoutManager recyce = new GridLayoutManager(CustomerActivity.this, 1);
                            mCustomerDevicesRecycler.setLayoutManager(recyce);
                            mCustomerDevicesRecycler.setItemAnimator(new DefaultItemAnimator());
                            mCustomerDevicesRecycler.setAdapter(recyclerAdapter);
                        }
                        else{

                            deviceListRecycler = new ArrayList<>();
                            DeviceAdapter recyclerAdapter = new DeviceAdapter(deviceListRecycler, CustomerActivity.this);
                            mCustomerDevicesRecycler.setAdapter(recyclerAdapter);
                        }

                        /*Get devices list*/
                    } else {
                        //Login failed
                        //LoginPage
                        ThreadToast("User information not found. Please try again later.");
                        RedirectLogin();
                    }
                }

                @Override
                public void onFailure(Call<CustomerInfoResponseModel> call, Throwable t) {
                    ThreadToast("Logging in: " + t.getMessage());
                    call.cancel();
                    RedirectLogin();
                }
            });


        } else {
            //LoginPage
            ThreadToast("User information not found. Please try again later.");
            RedirectLogin();
        }
    }
}
