package com.fischer.tom.reddiator.content;

/**
 * Created by Tom on 2015-03-31.
 */
public class Post {
    private String subreddit;
    private String title;
    private String author;
    private int points;
    private int numComments;
    private String permalink;
    private String url;
    private String domain;
    private String id;

    public Post(String subreddit, String title, String author, int points, int numComments, String permalink, String url, String domain, String id) {
        this.subreddit = subreddit;
        this.title = title;
        this.author = author;
        this.points = points;
        this.numComments = numComments;
        this.permalink = permalink;
        this.url = url;
        this.domain = domain;
        this.id = id;
    }

    @Override
    public String toString(){
        return this.title + " - Posted by: " + this.author + " in " + this.subreddit;
    }

    public String getSubreddit() {
        return this.subreddit;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getPoints() {
        return this.points;
    }

    public int getNumComments() {
        return this.numComments;
    }

    public String getPermalink() {
        return this.permalink;
    }

    public String getURL() {
        return this.url;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getID() {
        return this.id;
    }
}
