package com.softwarica.printstation.storage;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softwarica.printstation.entity.Product;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class PrefManager {
    private static PrefManager prefManager;
    private final SharedPreferences preferences;
    private final Gson gson;

    public PrefManager(@NonNull Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.gson = new GsonBuilder().create();
    }

    public synchronized static void initService(@NonNull Context context) {
        if (prefManager == null) prefManager = new PrefManager(context);
    }

    public synchronized static boolean isInitialized() {
        return prefManager != null;
    }

    public synchronized static PrefManager service() {
        if (prefManager == null) throw new RuntimeException("PrefManager is not initialized");
        return prefManager;
    }

    public String token() {
        return this.preferences.getString(Constants.KEY_TOKEN, null);
    }

    public void setToken(String token) {
        this.preferences.edit().putString(Constants.KEY_TOKEN, token).apply();
    }

    public void clearToken() {
        this.preferences.edit().putString(Constants.KEY_TOKEN, null).apply();
    }




    public void resetOrders() {
        this.preferences.edit().putStringSet(Constants.KEY_ORDERS, new HashSet<>()).apply();
    }

    /**
     * This method provide the Type for serialization/deserialization for cache data
     *
     * @return the type of provided parameter Object
     */
    public static Type getType(Class wrapper, Class dataKey) {
        return new ParameterizedType() {
            @NonNull
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{dataKey};
            }

            @NonNull
            @Override
            public Type getRawType() {
                return wrapper;
            }

            @Nullable
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }

    public interface OnChange<T> {
        void onChange(T item);
    }

    private interface Constants {
        String KEY_TOKEN = "KEY_TOKEN";
        String KEY_ORDERS = "KEY_ORDERS";
    }
}
