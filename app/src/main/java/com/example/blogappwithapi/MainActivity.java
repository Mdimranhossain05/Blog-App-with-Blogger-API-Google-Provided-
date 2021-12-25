package com.example.blogappwithapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    RecyclerView recyclerView;
    PostAdapter adapter;
    List<Item> items = new ArrayList<>();
    LinearLayoutManager manager;
    boolean isScrolling = false;
    int currentItems, totalItems,scrollOutItems;
    String token = "";
    SpinKitView progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.spin_kit);

        FirebaseMessaging.getInstance().subscribeToTopic("content");
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Done";
//                        if (!task.isSuccessful()) {
//                            msg = "Failed";
//                        }
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });


        navigationView = findViewById(R.id.navigationViewID);
        recyclerView = findViewById(R.id.postListID);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new PostAdapter(this,items);
        recyclerView.setAdapter(adapter);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(getApplicationContext(), "Home Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_android:
                        Toast.makeText(getApplicationContext(), "Android Clicked", Toast.LENGTH_SHORT).show();
                        break;

                }
                return false;
            }
        });

        setUpToolbar();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling == true && (currentItems+scrollOutItems == totalItems) ){
                    isScrolling = false;
                    getData();
                }

            }
        });
        getData();

    }

    private void setUpToolbar(){
        drawerLayout = findViewById(R.id.drawerLayoutID);
        toolbar = findViewById(R.id.toolbarID);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }
    private void getData(){

        String url = BloggerAPI.url + "?key=" + BloggerAPI.key;
        if (token!=""){
            url = url+"&pageToken="+token;
        }
        if(token == null){
            return;
        }
        progress.setVisibility(View.VISIBLE);
        Call<PostList> postlist = BloggerAPI.getPostService().getPostList(url);
        postlist.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                PostList postList = response.body();
                token = postList.getNextPageToken();
                Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                Log.d("TAG", "onResponse: "+postList.getItems().get(0).getTitle());
                items.addAll(postList.getItems());
                adapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
            }
        });

    }

}