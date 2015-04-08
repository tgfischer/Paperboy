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

            //comments.addAll(this.processChildren(jsonArray, null, 0));
            this.processChildren(jsonArray, null, 0, comments);

            //process(comments, r, 0);

        } catch(Exception e){
            Log.d("ERROR","Could not connect: "+e);
        }

        return comments;
    }

    public ArrayList<Comment> processChildren(JSONArray jsonComments, Comment parent, int level, ArrayList<Comment> masterList) {
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
                    masterList.add(comment);

                    if (!jsonComment.get("replies").equals("")) {
                        JSONArray jsonArray = jsonComment.getJSONObject("replies").getJSONObject("data").getJSONArray("children");

                        comment.addChildren(this.processChildren(jsonArray, comment, level + 1, masterList));
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
