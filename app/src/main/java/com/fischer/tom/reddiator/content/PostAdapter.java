package com.fischer.tom.reddiator.content;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public PostAdapter(Context context, int layoutResourceId, ArrayList<Post> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

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

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Post post = data.get(position);

        holder.postTitleTextView.setText(post.getTitle());
        holder.usernameTextView.setText(post.getAuthor());
        holder.subredditTextView.setText("r/" + post.getSubreddit());

        String numComments = post.getNumComments() > 999 ?
                Double.toString((double)Math.round(post.getNumComments() / 100) / 10) + "K" :
                Integer.toString(post.getNumComments());

        holder.numCommentsTextView.setText(numComments);

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
    }
}
