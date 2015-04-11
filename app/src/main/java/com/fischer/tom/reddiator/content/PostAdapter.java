package com.fischer.tom.reddiator.content;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.app.Activity;

import com.fischer.tom.reddiator.ImageLoader;
import com.fischer.tom.reddiator.R;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Tom on 2015-03-31.
 */
public class PostAdapter extends ArrayAdapter<Post> {
    private final Context context;
    private final ArrayList<Post> data;
    private final int layoutResourceId;
    private DBAdapter dbAdapter;

    public PostAdapter(Context context, int layoutResourceId, ArrayList<Post> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
        this.dbAdapter = Database.getInstance(this.context);
    }

    public void updateList(ArrayList<Post> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        Post post = data.get(position);

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.postTitleTextView = (TextView)row.findViewById(R.id.postTitleTextView);
            holder.usernameTextView = (TextView)row.findViewById(R.id.usernameTextView);
            holder.subredditTextView = (TextView)row.findViewById(R.id.subredditTextView);
            holder.numCommentsTextView = (TextView)row.findViewById(R.id.numCommentsTextView);
            holder.thumbnailImageView = (ImageView)row.findViewById(R.id.thumbnailImageView);
            holder.scoreTextView = (TextView)row.findViewById(R.id.scoreTextView);
            holder.timestampTextView = (TextView)row.findViewById(R.id.timestampTextView);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        if (this.dbAdapter.containsURL(post.getPermalink())) {
            row.setBackgroundColor(context.getResources().getColor(R.color.Grey100));
            holder.postTitleTextView.setTextColor(context.getResources().getColor(R.color.Grey700));
        } else {
            row.setBackgroundColor(Color.WHITE);
            holder.postTitleTextView.setTextColor(context.getResources().getColor(R.color.Grey900));
        }

        String score = post.getPoints() > 9999 ?
                Double.toString((double)Math.round(post.getPoints() / 100) / 10) + "K" :
                Integer.toString(post.getPoints());

        holder.scoreTextView.setText(score);


        holder.postTitleTextView.setText(post.getTitle());
        holder.subredditTextView.setText("r/" + post.getSubreddit());

        String numComments = post.getNumComments() > 999 ?
                Double.toString((double)Math.round(post.getNumComments() / 100) / 10) + "K" :
                Integer.toString(post.getNumComments());

        holder.numCommentsTextView.setText(numComments + " comments");
        holder.usernameTextView.setText("by " + post.getAuthor());
        holder.timestampTextView.setText(post.calculateTimestamp(System.currentTimeMillis() / 1000L));

        try {
            new ImageLoader(new URI(post.getThumbnail()), holder.thumbnailImageView, 50, 38).execute();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return row;
    }

    static class ViewHolder
    {
        TextView postTitleTextView;
        TextView usernameTextView;
        TextView subredditTextView;
        TextView numCommentsTextView;
        ImageView thumbnailImageView;
        TextView scoreTextView;
        TextView timestampTextView;
    }
}
