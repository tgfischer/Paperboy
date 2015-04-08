package com.fischer.tom.reddiator.content;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tom on 2015-04-05.
 */
public class Comments {
    private final String URL_TEMPLATE = "http://www.reddit.comPERMALINK.json?after=AFTER&?sort=confidence";
    public static HashMap<Integer, Comment> DATA = new HashMap<Integer, Comment>();

    private String url;
    private String after;
    private String permalink;
    private ArrayList<Comment> mComments;

    public Comments(String permalink) {
        this.permalink = permalink;
        this.after = "";
        this.generateURL();
    }

    private void generateURL(){
        url = URL_TEMPLATE.replace("PERMALINK", this.permalink);
        url = url.replace("AFTER", this.after);
    }

    // Load various details about the comment
    private Comment loadComment(JSONObject data, Comment parent, int level){
        try{
            Comment comment = new Comment(
                data.getString("body_html"),
                data.getString("author"),
                data.getInt("score"),
                data.getDouble("created_utc"),
                level,
                parent
            );

            return comment;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // This is where the comment is actually loaded
    // For each comment, its replies are recursively loaded
    /*private void process(ArrayList<Comment> comments, JSONArray c, int level) throws Exception {
        for (int i = 0; i < c.length(); i++) {
            if (c.getJSONObject(i).optString("kind") == null) {
                continue;
            } else if (!c.getJSONObject(i).optString("kind").equals("t1")) {
                continue;
            }

            JSONObject data = c.getJSONObject(i).getJSONObject("data");

            Comment comment = this.loadComment(data, level);

            if (comment.getBody() != null) {
                //ArrayList<Comment> children = this.addReplies(comments, data, level + 1);

                //if (children != null) {
                    //comment.addChildren(children);
                //}

                this.addReplies(comment.getChildren(), data, level + 1);

                comments.add(comment);
            }
        }
    }

    // Add replies to the comments
    private ArrayList<Comment> addReplies(ArrayList<Comment> comments, JSONObject parent, int level){
        try {
            if (parent.get("replies").equals("")) {
                // This means the comment has no replies
                return null;
            }

            JSONArray r = parent.getJSONObject("replies").getJSONObject("data").getJSONArray("children");
            process(comments, r, level);

            return comments;
        } catch(Exception e) {
            Log.d("ERROR", "addReplies : " + e);
        }

        return null;
    }*/

    // Load the comments as an ArrayList, so that it can be
    // easily passed to the ArrayAdapter
    public ArrayList<Comment> fetchComments() {
        ArrayList<Comment> comments = new ArrayList<Comment>();

        try {
            // Fetch the contents of the comments page
            String raw = RedditData.getJSON(url);

            JSONArray jsonArray = new JSONArray(raw).getJSONObject(1).getJSONObject("data").getJSONArray("children");

            // All comments at this point are at level 0
            // (i.e., they are not replies)

            comments.addAll(this.processChildren(jsonArray, null, 0));

            //process(comments, r, 0);

        } catch(Exception e){
            Log.d("ERROR","Could not connect: "+e);
        }

        return comments;
    }

    public ArrayList<Comment> processChildren(JSONArray jsonComments, Comment parent, int level) {
        ArrayList<Comment> comments = new ArrayList<Comment>();

        for (int i = 0; i < jsonComments.length(); i++) {
            try {
                JSONObject raw = jsonComments.getJSONObject(i);

                if (raw.optString("kind") == null) {
                    continue;
                } else if (!raw.optString("kind").equals("t1")) {
                    continue;
                }

                JSONObject jsonComment = raw.getJSONObject("data");
                Comment comment = this.loadComment(jsonComment, parent, level);

                if (comment != null) {
                    if (!jsonComment.get("replies").equals("")) {
                        JSONArray jsonArray = jsonComment.getJSONObject("replies").getJSONObject("data").getJSONArray("children");

                        comment.addChildren(this.processChildren(jsonArray, comment, level + 1));
                    }

                    comments.add(comment);
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        return comments;
    }

    public void clearComments() {
        this.after = "";
        this.mComments = null;
        this.generateURL();
    }
}
