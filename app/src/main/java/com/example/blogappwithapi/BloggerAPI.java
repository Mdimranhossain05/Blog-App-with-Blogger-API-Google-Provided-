package com.example.blogappwithapi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class BloggerAPI {

    public static final String key = "AIzaSyBw56eNEP-ozn_NcQiWofu9ylPdYcSdoWM";
    public static final String url = "https://www.googleapis.com/blogger/v3/blogs/8637963151355911174/posts/";


    public static PostService postService = null;

    public static PostService getPostService(){
        if (postService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
            postService = retrofit.create(PostService.class);
        }
        return postService;
    }

    public interface PostService{
        @GET
        Call<PostList> getPostList(@Url String url );
    }

}

//        @GET("{postId}/?key"+key) //to Access Single post
//        Call<Item> getPostItem(@Path("postId") String id);
