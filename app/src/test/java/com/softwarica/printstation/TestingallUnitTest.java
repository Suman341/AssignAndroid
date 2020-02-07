package com.softwarica.printstation;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import com.softwarica.printstation.api.API;
import com.softwarica.printstation.api.response.ApiResponse;
import com.softwarica.printstation.api.response.LoginResponse;
import com.softwarica.printstation.entity.CategoryEntity;
import com.softwarica.printstation.entity.Product;
import com.softwarica.printstation.storage.PrefManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(application = PrintStationApplication.class, sdk = 28)
public class TestingallUnitTest {

    private Context context = ApplicationProvider.getApplicationContext();

    @Before
    public void setUp() throws Exception {
        PrefManager.initService(context);
        API.initService(context);
    }

    @Test
    public void testUserLogin() {
        Call<ApiResponse<LoginResponse>> apiResponseCall = API.service().login("suman@gmail.com", "suman143");
        try {
            Response<ApiResponse<LoginResponse>> apiResponseResponse = apiResponseCall.execute();
            assertTrue(apiResponseResponse.isSuccessful());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testUserLoginWithWrongCrdentials() {
        Call<ApiResponse<LoginResponse>> apiResponseCall = API.service().login("suman@gmail.com", "suman1435");
        try {
            Response<ApiResponse<LoginResponse>> apiResponseResponse = apiResponseCall.execute();
            assertFalse(apiResponseResponse.isSuccessful());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testGetAllProducts() {
        Call<ApiResponse<LoginResponse>> loginApiResponseCall = API.service().login("suman@gmail.com", "suman143");
        Call<ApiResponse<List<Product>>> productsApiResponseCall = API.service().getProducts(null);
        try {
            Response<ApiResponse<LoginResponse>> loginApiResponseResponse = loginApiResponseCall.execute();
            assertTrue(loginApiResponseResponse.isSuccessful());

            // save token to local storage
            PrefManager.service().setToken(loginApiResponseResponse.body().getData().getToken());

            Response<ApiResponse<List<Product>>> productsResponse = productsApiResponseCall.execute();
            assertTrue(productsResponse.isSuccessful());

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
