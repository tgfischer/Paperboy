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
    private String thumbnail;
    private double timestamp;
    private String selftext_html;
    private int gilded;
    private boolean over_18;
    private boolean stickied;

    public Post(String subreddit, String title, String author, int points, int numComments,
                String permalink, String url, String domain, String id, String thumbnail,
                double timestamp, String selftext_html, int guilded, boolean over_18,
                boolean stickied) {
        this.subreddit = subreddit;
        this.title = title;
        this.author = author;
        this.points = points;
        this.numComments = numComments;
        this.permalink = permalink;
        this.url = url;
        this.domain = domain;
        this.id = id;
        this.thumbnail = thumbnail;
        this.timestamp = timestamp;
        this.selftext_html = selftext_html;
        this.gilded = gilded;
        this.over_18 = over_18;
        this.stickied = stickied;
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

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getSelftext() {
        return this.selftext_html;
    }

    public int getGilded() {
        return this.gilded;
    }

    public boolean isOver_18() {
        return this.over_18;
    }

    public boolean isStickied() {
        return this.stickied;
    }

    public boolean hasSelftext() {
        return !this.selftext_html.equals("null");
    }

    public String calculateTimestamp(double now) {
        String timestamp = "posted TIME UNITS ago";

        Integer difference = (int)Math.round(now - this.timestamp);

        if (difference < 60) {
            timestamp = timestamp.replace("TIME", difference.toString());

            if (difference < 2) {
                timestamp = timestamp.replace("UNITS", "second");
            } else {
                timestamp = timestamp.replace("UNITS", "seconds");
            }
        } else if (difference < 60 * 60) {
            Integer toMinutes = Math.round(difference / 60);
            timestamp = timestamp.replace("TIME", toMinutes.toString());

            if (toMinutes < 2) {
                timestamp = timestamp.replace("UNITS", "minute");
            } else {
                timestamp = timestamp.replace("UNITS", "minutes");
            }
        }  else if (difference < 60 * 60 * 24) {
            Integer toHours = Math.round(difference / (60 * 60));
            timestamp = timestamp.replace("TIME", toHours.toString());

            if (toHours < 2) {
                timestamp = timestamp.replace("UNITS", "hour");
            } else {
                timestamp = timestamp.replace("UNITS", "hours");
            }
        }  else if (difference < 60 * 60 * 24 * 7) {
            Integer toDays = Math.round(difference / (60 * 60 * 24));
            timestamp = timestamp.replace("TIME", toDays.toString());

            if (toDays < 2) {
                timestamp = timestamp.replace("UNITS", "day");
            } else {
                timestamp = timestamp.replace("UNITS", "days");
            }
        }  else if (difference < 60 * 60 * 24 * 7 * 52) {
            Integer toWeeks = Math.round(difference / (60 * 60 * 24 * 7));
            timestamp = timestamp.replace("TIME", toWeeks.toString());

            if (toWeeks < 2) {
                timestamp = timestamp.replace("UNITS", "week");
            } else {
                timestamp = timestamp.replace("UNITS", "weeks");
            }
        } else {
            Integer toYears = Math.round(difference / (60 * 60 * 24 * 365));
            timestamp = timestamp.replace("TIME", toYears.toString());

            if (toYears < 2) {
                timestamp = timestamp.replace("UNITS", "year");
            } else {
                timestamp = timestamp.replace("UNITS", "years");
            }
        }

        return timestamp;
    }
}
