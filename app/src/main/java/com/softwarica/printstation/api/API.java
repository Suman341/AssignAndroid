package com.softwarica.printstation.api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.softwarica.printstation.BuildConfig;
import com.softwarica.printstation.api.response.ApiResponse;
import com.softwarica.printstation.api.response.ErrorResponse;
import com.softwarica.printstation.storage.PrefManager;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {
    public static String IMAGE_URL = "http://10.0.2.2:5000/";
    private static String BASE_URL = "http://10.0.2.2:5000/";
    private static ApiService apiService;
    private static Retrofit retrofit;

    public synchronized static void initService(Context context) {
        if (!PrefManager.isInitialized()) PrefManager.initService(context);

        if (apiService == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient
                    .Builder()
                    .addInterceptor(API::interceptRequest);

            if (BuildConfig.DEBUG)
                clientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            API.retrofit = retrofit;

            apiService = retrofit.create(ApiService.class);
        }
    }

    @NonNull
    private static Response interceptRequest(@NonNull Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder builder = originalRequest
                .newBuilder();

        String token = PrefManager.service().token();
        if (null != token) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        return chain.proceed(builder.build());
    }

    public synchronized static ApiService service() {
        if (apiService == null) throw new RuntimeException("ApiService is not initialized");
        return apiService;
    }

    public static  List<ErrorResponse> getErrorAsBody(ResponseBody errorBody) {
        Converter<ResponseBody, List<ErrorResponse>> responseBodyObjectConverter = API.retrofit.responseBodyConverter(PrefManager.getType(List.class, ErrorResponse.class), new Annotation[]{});
        try {
            return responseBodyObjectConverter.convert(errorBody);
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }
}
