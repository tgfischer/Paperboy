package com.fischer.tom.reddiator.content;

import android.content.Context;

/**
 * Created by Tom on 2015-04-05.
 */
public class Database {
    private static DBAdapter adapter;

    public static DBAdapter getInstance(Context context) {
        if (adapter == null) {
            adapter = new DBAdapter(context);
        }
        return adapter;
    }
}
