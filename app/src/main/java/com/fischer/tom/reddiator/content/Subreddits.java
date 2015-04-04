package com.fischer.tom.reddiator.content;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tom on 2015-04-03.
 */
public class Subreddits {
    private static ArrayList<String> mSubredditsList = new ArrayList<String>();

    static {
        mSubredditsList.add("Front Page");
        mSubredditsList.add("All");
        //mSubredditsList.add("New");
        mSubredditsList.add("announcements");
        mSubredditsList.add("Art");
        mSubredditsList.add("AskReddit");
        mSubredditsList.add("askscience");
        mSubredditsList.add("aww");
        mSubredditsList.add("blog");
        mSubredditsList.add("creepy");
        mSubredditsList.add("dataisbeautiful");
        mSubredditsList.add("DIY");
        mSubredditsList.add("Documentaries");
        mSubredditsList.add("EarthPorn");
        mSubredditsList.add("explainlikeimfive");
        mSubredditsList.add("Fitness");
        mSubredditsList.add("food");
        mSubredditsList.add("funny");
        mSubredditsList.add("Futurology");
        mSubredditsList.add("gadgets");
        mSubredditsList.add("gaming");
        mSubredditsList.add("GetMotivated");
        mSubredditsList.add("gifs");
        mSubredditsList.add("history");
        mSubredditsList.add("IAmA");
        mSubredditsList.add("InternetIsBeautiful");
        mSubredditsList.add("Jokes");
        mSubredditsList.add("LifeProTips");
        mSubredditsList.add("listentothis");
        mSubredditsList.add("mildlyinteresting");
        mSubredditsList.add("movies");
        mSubredditsList.add("Music");
        mSubredditsList.add("news");
        mSubredditsList.add("nosleep");
        mSubredditsList.add("nottheonion");
        mSubredditsList.add("oldschoolcool");
        mSubredditsList.add("personalfinance");
        mSubredditsList.add("philosophy");
        mSubredditsList.add("photoshopbattles");
        mSubredditsList.add("pics");
        mSubredditsList.add("science");
        mSubredditsList.add("Showerthoughts");
        mSubredditsList.add("space");
        mSubredditsList.add("sports");
        mSubredditsList.add("television");
        mSubredditsList.add("tifu");
        mSubredditsList.add("todayilearned");
        mSubredditsList.add("TwoXChromosomes");
        mSubredditsList.add("UpliftingNews");
        mSubredditsList.add("videos");
        mSubredditsList.add("worldnews");
        mSubredditsList.add("writingprompts");
    }

    public static ArrayList<String> getSubreddits() {
        return mSubredditsList;
    }

    public static String get(int id) {
        return mSubredditsList.get(id);
    }
}
