package com.softwarica.printstation.api;

import com.softwarica.printstation.api.request.ProductOrderRequest;
import com.softwarica.printstation.api.response.ApiResponse;
import com.softwarica.printstation.api.response.ErrorResponse;
import com.softwarica.printstation.api.response.LoginResponse;
import com.softwarica.printstation.entity.CategoryEntity;
import com.softwarica.printstation.entity.OrderEntity;
import com.softwarica.printstation.entity.Product;
import com.softwarica.printstation.entity.UserEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @POST("user/register")
    Call<ApiResponse<List<ErrorResponse>>> registerUser(@Body UserEntity user);

    @PUT("user/profile")
    Call<ApiResponse<UserEntity>> update(@Body UserEntity user);

}
