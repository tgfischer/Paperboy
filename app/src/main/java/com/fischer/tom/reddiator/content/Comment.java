package com.fischer.tom.reddiator.content;

import java.util.ArrayList;

/**
 * Created by Tom on 2015-04-05.
 */
public class Comment {
    private String body;
    private String author;
    private int score;
    private double timestamp;
    private int level;
    private ArrayList<Comment> children;
    private Comment parent;

    public Comment(String body, String author, int score, double timestamp, int level, Comment parent) {
        this.body = body;
        this.author = author;
        this.score = score;
        this.timestamp = timestamp;
        this.level = level;
        this.children = new ArrayList<Comment>();
        this.parent = parent;
    }

    public String getBody() {
        return this.body;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getScore() {
        return this.score;
    }

    public String calculateTimestamp(double now) {
        return null;
    }

    public int getLevel() {
        return this.level;
    }

    public void addChildren(ArrayList<Comment> comments) {
        this.children.addAll(comments);
    }

    public ArrayList<Comment> getChildren () {
        return this.children;
    }
}
