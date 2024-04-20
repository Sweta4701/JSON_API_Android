package com.example.json_api;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class JsonActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button buttonGet;
    private Button buttonPost;
    private Button buttonPut;
    private Button buttonDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_json);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        buttonGet = findViewById(R.id.buttonGet);
        buttonPost = findViewById(R.id.buttonPost);
        buttonPut = findViewById(R.id.buttonPut);
        buttonDelete = findViewById(R.id.buttonDelete);


        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(JsonActivity.this, MainActivity.class);
//                startActivity(intent);
                buttonGet.setEnabled(false);
                getMethod();

            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonPost.setEnabled(false);
                createPost();


            }
        });

        buttonPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonPut.setEnabled(false);
                updatePost();

            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonDelete.setEnabled(false);
                deletePost();

            }
        });
    }


    private void getMethod() {
        Call<List<Post>> call = Retrofit.getInstance().getJsonPlaceholder().getPost();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(JsonActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                List<Post> postList = response.body();
                if (postList != null) {
                    for (Post post :
                            postList) {
                        Log.i(TAG, "onResponse: ID: " + post.getId() + " UserID: " + post.getUserId() + " Title: " + post.getTitle());

                    }
                }
//                PostAdapter postAdapter = new PostAdapter(JsonActivity.this,postList);
//                recyclerView.setAdapter(postAdapter);

                Gson gson = new Gson();
                String json = gson.toJson(postList);

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("postList", json);
                edit.apply();

                buttonGet.setEnabled(true);
                Toast.makeText(JsonActivity.this, "GET API is calling", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(JsonActivity.this, MainActivity.class);
                intent.putExtra("postList", json);

                startActivity(intent);

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(JsonActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPost() {
        PostDialog PostDialog = new PostDialog(this, 1, new PostDialog.PostListener() {
            @Override
            public void onUpdatePost(String userId, String postId, String title, String body) {
                Post post = new Post(userId, postId, title, body);
                Call<Post> call = Retrofit.getInstance().getJsonPlaceholder().createPost(post);

                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(JsonActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (response.body() != null) {
                            Post responseFromBackend = response.body();
                            String body = responseFromBackend.getBody();

                            Log.d(TAG, "onResponse: " + responseFromBackend.getBody());
                            Log.d(TAG, "onResponse: " + responseFromBackend.getTitle());
                            Log.d(TAG, "onResponse: getId : " + responseFromBackend.getId());
                            Log.d(TAG, "onResponse: getUserId : " + responseFromBackend.getUserId());

                            Post post1 = new Post(responseFromBackend.getUserId(), responseFromBackend.getId(), responseFromBackend.getTitle(), body);

                            buttonGet.setEnabled(true);
                            Toast.makeText(JsonActivity.this, "Post API is calling", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(JsonActivity.this, MainActivity.class);
                            intent.putExtra("post", post1);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(JsonActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        PostDialog.showDialog();
    }

    private void updatePost() {
        Log.i(TAG, "updatePost: btn update  is clicked");
        PostDialog PostDialog = new PostDialog(this, 2, new PostDialog.PostListener() {
            public void onUpdatePost(String userId, String postId, String title, String body) {
                Log.i(TAG, "onUpdatePost: on clicked");
                Post post = new Post(userId, postId, title, body);
                Call<Post> call = Retrofit.getInstance().getJsonPlaceholder().putPost(2, post);

                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(JsonActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (response.body() != null) {
                            Post responseFromBackend = response.body();
                            String body = responseFromBackend.getBody();
                            String title = responseFromBackend.getTitle();
                            String Id = responseFromBackend.getId();
                            String UserId = responseFromBackend.getUserId();

                            Log.d(TAG, "onResponse: " + responseFromBackend.getBody());
                            Log.d(TAG, "onResponse: " + responseFromBackend.getTitle());
                            Log.d(TAG, "onResponse: getId : " + responseFromBackend.getId());
                            Log.d(TAG, "onResponse: getUserId : " + responseFromBackend.getUserId());


                            Post post1 = new Post(UserId, Id, title, body);

                            buttonPut.setEnabled(true);
                            Toast.makeText(JsonActivity.this, "PUT API is calling", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(JsonActivity.this, MainActivity.class);
                            intent.putExtra("post", post1);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(JsonActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
        PostDialog.showDialog();
    }

    private void deletePost() {
        Call<Void> call = Retrofit.getInstance().getJsonPlaceholder().deletePost(2);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle successful response
                    Log.d("MainActivity", "Post deleted successfully");
                } else {
                    // Handle error response
                    Log.e("MainActivity", "Failed to delete post: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(JsonActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}