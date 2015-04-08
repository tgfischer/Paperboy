package com.fischer.tom.reddiator.content;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
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

    public CommentsAdapter(Context context, int layoutResourceId, ArrayList<Comment> comments) {
        super();

        this.context = context;
        this.comments = comments;
        this.layoutResourceId = layoutResourceId;
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
            holder.commentContentTextView = (TextView)row.findViewById(R.id.commentContentTextView);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Spanned test = Html.fromHtml(Html.fromHtml(comment.getBody()).toString());

        holder.commentContentTextView.setText(test.subSequence(0, test.length() - 2));

        return row;
    }

    static class ViewHolder
    {
        TextView commentContentTextView;
    }

    /*private final Context context;
    private final ArrayList<Comment> data;
    private final int layoutResourceId;

    public CommentsAdapter(Context context, int layoutResourceId, ArrayList<Comment> data) {
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    public void updateList(ArrayList<Comment> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    static class ViewHolder
    {
        TextView commentContentTextView;
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Comment> childrenList = data.get(groupPosition).getChildren();
        return childrenList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Comment> commentList = data.get(groupPosition).getChildren();
        return commentList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        Comment comment = data.get(groupPosition);

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.commentContentTextView = (TextView)row.findViewById(R.id.commentContentTextView);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Spanned test = Html.fromHtml(Html.fromHtml(comment.getBody()).toString());

        // For some reason, the HTML has \n\n at the end of each comment
        holder.commentContentTextView.setText(test.subSequence(0, test.length() - 2));

        return row;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;
        Comment comment = data.get(groupPosition).getChildren().get(childPosition);

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.commentContentTextView = (TextView)row.findViewById(R.id.commentContentTextView);

            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }

        Spanned test = Html.fromHtml(Html.fromHtml(comment.getBody()).toString());

        // For some reason, the HTML has \n\n at the end of each comment
        holder.commentContentTextView.setText(test.subSequence(0, test.length() - 2));

        return row;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }*/
}
