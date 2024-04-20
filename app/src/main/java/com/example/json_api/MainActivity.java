package com.example.json_api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        Post post = (Post) intent.getSerializableExtra("post");
        Log.i(TAG, "onCreate: Pst " + post);
        if (post != null){
            postGet(post);

        }else {
            Log.i(TAG, "onCreate: in else");
            sharedPreferenceForGet();
        }
    }
    private void sharedPreferenceForGet(){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String json = prefs.getString("postList", "");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Post>>() {
        }.getType();
        List<Post> postList = gson.fromJson(json, type);

        PostAdapter postAdapter = new PostAdapter(MainActivity.this, postList);
        recyclerView.setAdapter(postAdapter);
    }

    private void postGet(Post post){
        ArrayList<Post> postList = new ArrayList<>();
        postList.add(post);

        PostAdapter postAdapter = new PostAdapter(MainActivity.this, postList);
        recyclerView.setAdapter(postAdapter);
    }

}