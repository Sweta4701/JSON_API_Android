package com.example.json_api;

import retrofit2.converter.gson.GsonConverterFactory;


public class Retrofit {
    private static Retrofit instance = null;
    private JSONPlaceholder jsonPlaceholder;

    public Retrofit() {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder().baseUrl("https://jsonplaceholder.typicode.com/").addConverterFactory(GsonConverterFactory.create()).build();

        this.jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
    }

    public static synchronized Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit();
        }
        return new Retrofit();
    }

    public JSONPlaceholder getJsonPlaceholder() {
        return jsonPlaceholder;
    }
}
