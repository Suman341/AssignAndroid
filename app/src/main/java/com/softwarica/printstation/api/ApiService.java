package com.softwarica.printstation.api;

import com.softwarica.printstation.api.response.ApiResponse;
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

    @FormUrlEncoded
    @POST("user/login")
    Call<ApiResponse<LoginResponse>> login(@Field("email") String email, @Field("password") String password);

    @GET("user/profile")
    Call<ApiResponse<UserEntity>> getUserProfile();

    @GET("products")
    Call<ApiResponse<List<Product>>> getProducts(@Query("categoryId") String categoryId);

    @GET("product/{productId}")
    Call<ApiResponse<Product>> getProduct(@Path("productId") String productId);

    @POST("product/order")
    Call<ApiResponse<String>> orderProducts(@Body ProductOrderRequest orders);

    @GET("orders")
    Call<ApiResponse<List<OrderEntity>>> getMyOrders();

    @GET("categories")
    Call<ApiResponse<List<CategoryEntity>>> getCategories();
}
