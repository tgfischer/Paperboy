package com.fischer.tom.reddiator.content;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fischer.tom.reddiator.ImageLoader;
import com.fischer.tom.reddiator.R;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Tom on 2015-04-05.
 */
public class CommentsAdapter extends BaseAdapter {
    private final Context context;
    private final ArrayList<Comment> comments;
    private final int layoutResourceId;
    private final int padding;

    public CommentsAdapter(Context context, int layoutResourceId, ArrayList<Comment> comments) {
        super();

        this.context = context;
        this.comments = comments;
        this.layoutResourceId = layoutResourceId;
        this.padding = (int)(5 * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public void updateList(ArrayList<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
    }

    @Override
    public int getCount() {
        return this.comments.size();
    }

    @Override
    public Object getItem(int position) {
        return this.comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        Comment comment = comments.get(position);

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.relativeLayout = (LinearLayout)row.findViewById(R.id.relativeLayout);
            holder.leftBar = (View)row.findViewById(R.id.leftBar);
            holder.commentContentTextView = (TextView)row.findViewById(R.id.commentContentTextView);
            holder.authorTextView = (TextView)row.findViewById(R.id.authorTextView);
            holder.scoreTextView = (TextView)row.findViewById(R.id.scoreTextView);
            holder.timestampTextView = (TextView)row.findViewById(R.id.timestampTextView);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        holder.relativeLayout.setPadding(comment.getLevel() * padding, 0, 0, 0);

        Spanned text = Html.fromHtml(Html.fromHtml(comment.getBody()).toString());
        holder.commentContentTextView.setText(text.subSequence(0, text.length() - 2));

        holder.authorTextView.setText(comment.getAuthor());
        holder.authorTextView.setTypeface(null, Typeface.BOLD);
        holder.scoreTextView.setText(comment.getScore() + " points");
        holder.timestampTextView.setText(comment.calculateTimestamp(System.currentTimeMillis() / 1000L));

        //holder.leftBar.setLayoutParams(new RelativeLayout.LayoutParams(padding, RelativeLayout.LayoutParams.MATCH_PARENT));

        return row;
    }

    static class ViewHolder
    {
        LinearLayout relativeLayout;
        View leftBar;
        TextView commentContentTextView;
        TextView authorTextView;
        TextView scoreTextView;
        TextView timestampTextView;
    }
}
