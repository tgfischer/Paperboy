package com.fischer.tom.reddiator.content;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;

import com.fischer.tom.reddiator.content.Post;
import com.fischer.tom.reddiator.content.RedditData;

/**
 * Created by Tom on 2015-03-31.
 */
public class Posts {
    private final String URL_TEMPLATE = "http://www.reddit.com/r/SUBREDDIT_NAME/.json?after=AFTER";
    public static HashMap<Integer, Post> DATA = new HashMap<Integer, Post>();

    private String url;
    private String after;
    private String subReddit;

    public Posts(String subReddit) {
        this.subReddit = subReddit;
        this.after = "";
        this.generateURL();
    }

    private void generateURL(){
        url = URL_TEMPLATE.replace("SUBREDDIT_NAME", this.subReddit);
        url = url.replace("AFTER", this.after);
    }

    public ArrayList<Post> fetchPosts(){
        String rawData = RedditData.getJSON(url);

        ArrayList<Post> list=new ArrayList<Post>();

        try{
            JSONObject data = new JSONObject(rawData).getJSONObject("data");
            JSONArray children = data.getJSONArray("children");

            after = data.getString("after");

            int listSize = list.size();

            for(int i = 0; i < children.length(); i++){
                JSONObject child = children.getJSONObject(i).getJSONObject("data");

                Post post = new Post(
                    child.optString("subreddit"),
                    child.optString("title"),
                    child.optString("author"),
                    child.optInt("score"),
                    child.optInt("num_comments"),
                    child.optString("permalink"),
                    child.optString("url"),
                    child.optString("domain"),
                    child.optString("id")
                );

                if(post.getTitle() != null) {
                    list.add(post);
                    DATA.put(i + listSize, post);
                }
            }
        }catch(Exception e){
            Log.e("fetchPosts()",e.toString());
        }

        return list;
    }

    public ArrayList<Post> fetchMorePosts(){
        this.generateURL();
        return this.fetchPosts();
    }
}
