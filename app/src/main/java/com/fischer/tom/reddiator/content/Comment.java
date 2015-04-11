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
        String timestamp = "TIME UNITS ago";

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
