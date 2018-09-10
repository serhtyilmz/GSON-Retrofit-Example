package com.yourapp.name.api;

import com.yourapp.name.models.CustomerInfoModel;
import com.yourapp.name.models.CustomerInfoResponseModel;
import com.yourapp.name.models.UserLoginModel;
import com.yourapp.name.models.UserLoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("/api/UserLogin")
    Call<UserLoginResponseModel> userLogin(@Body UserLoginModel userloginmodel);

    @POST("/api/CustomerInfo")
    Call<CustomerInfoResponseModel> customerInfo(@Body CustomerInfoModel customerInfoModel);
}
