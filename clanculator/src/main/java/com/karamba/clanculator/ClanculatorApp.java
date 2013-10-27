package com.karamba.clanculator;

import android.app.Application;
import com.karamba.clanculator.database.DatabaseHelper;

/**
 * todo: javadocs.
 */
public class ClanculatorApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseHelper db = new DatabaseHelper(this);
        db.getWritableDatabase();
    }
}
