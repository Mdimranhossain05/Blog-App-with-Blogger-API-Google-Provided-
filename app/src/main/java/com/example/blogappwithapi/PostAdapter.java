package com.example.blogappwithapi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostviewHolder> {
    private Context context;
    private List<Item> items;

    public PostAdapter(Context context, List<Item> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public PostviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_sample_view,parent,false);
        return new PostviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostviewHolder holder, int position) {

//        Pattern p = Pattern.compile("(?i)<img[^>]+?src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
//        Matcher m = p.matcher(items.get(position).getContent());
//        List<String> tokens = new ArrayList<String>();
//        while (m.find()){
//            String token = m.group(1);
//            tokens.add(token);
//        }

        Item item = items.get(position);

        Document document = Jsoup.parse(item.getContent());
        holder.postTitle.setText(item.getTitle());
        holder.postdesc.setText(document.text());

        Elements element = document.select("img");
        Glide.with(context).load(element.get(0).attr("src")).into(holder.postImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,DetailActivity.class);
                i.putExtra("URL",item.getUrl());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostviewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView postTitle,postdesc;
        public PostviewHolder(@NonNull View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImageID);
            postTitle = itemView.findViewById(R.id.postTitleID);
            postdesc = itemView.findViewById(R.id.postDescID);
        }
    }

}
