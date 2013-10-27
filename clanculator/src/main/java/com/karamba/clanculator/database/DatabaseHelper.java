/*
 * Copyright (C) 2012 Google Inc. All Rights Reserved.
 */
package com.karamba.clanculator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils.InsertHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.karamba.clanculator.data.Data;
import com.karamba.clanculator.data.Data.Group;
import com.karamba.clanculator.data.Data.Item;
import com.karamba.clanculator.database.Contract.Inventory;
import com.karamba.clanculator.database.Contract.Settings;
import com.karamba.clanculator.utils.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Database helper for Memory app.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "Clanculator";

    private static final String DATABASE_NAME = "clanculator.db";
    private static final int DATABASE_VERSION = 1;

    public static final String UPGRADE_METHOD_PREFIX = "upgradeFrom";

    public DatabaseHelper(Context context) {
        this(context, DATABASE_NAME, DATABASE_VERSION);
    }

    private DatabaseHelper(Context context, String databaseName, int databaseVersion) {
        super(context, databaseName, null, databaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Bootstrapping database version: " + DATABASE_VERSION);
        checkUpgradeMethods();

        db.execSQL("CREATE TABLE " + Settings.TABLE_NAME + " ("
                + Settings.NAME + " TEXT NOT NULL, "
                + Settings.VALUE + " STRING NOT NULL"
                + ");");

        db.execSQL("CREATE TABLE " + Inventory.TABLE_NAME + " ("
                + Inventory.TYPE + " INTEGER NOT NULL, "
                + Inventory.NUMBER + " INTEGER NOT NULL, "
                + Inventory.LEVEL + " INTEGER NOT NULL"
                + ");");


        populateData(db);
    }

    private void populateData(SQLiteDatabase db) {
        final InsertHelper inserter = new InsertHelper(db, Inventory.TABLE_NAME);
        inserter.prepareForInsert();
        final ContentValues values = new ContentValues();
        for (Group group : Data.GROUPS) {
            for (Item item : group.mItems) {
                values.put(Inventory.TYPE, item.mType);
                values.put(Inventory.LEVEL, 0);
                final int maxItems = item.mPerTownHallLevel[item.mPerTownHallLevel.length - 1];
                for (int i = 0; i < maxItems; i++) {
                    values.put(Inventory.NUMBER, i);
                    inserter.insert(values);
                }
            }
        }
    }

    private void checkUpgradeMethods() {
        final Method[] methods = getClass().getMethods();
        for (final Method method : methods) {
            final String name = method.getName();
            if (name.startsWith(UPGRADE_METHOD_PREFIX)) {
                final int version = Integer.valueOf(name.substring(UPGRADE_METHOD_PREFIX.length()));
                if (version >= DATABASE_VERSION) {
                    throw new IllegalStateException(String.format(
                            "Found an upgrade method for a version that does not exist. "
                                    + "Did you forget to bump DATABASE_VERSION. %d >= %d",
                            version,
                            DATABASE_VERSION));
                }
                final Class<?> returnType = method.getReturnType();
                if (!returnType.equals(void.class) && !returnType.equals(int.class)) {
                    throw new IllegalStateException(String.format(
                            "method %s must return either int or void", name));
                }
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.i(TAG, "Upgrading database from version %d to %d", oldVersion, newVersion);
        checkUpgradeMethods();

        // Use reflection to find upgrade methods. Upgrade methods are named "upgradeFromXXX().
        // The method can return a version number for the resulting database or be a void method
        // if the resulting method is the old version + 1
        final Class<? extends DatabaseHelper> thisClass = getClass();
        int from = oldVersion;
        while (true) {
            final int to;
            try {
                final Method upgradeMethod = thisClass.getMethod(
                        UPGRADE_METHOD_PREFIX + from, new Class[]{SQLiteDatabase.class});
                if (upgradeMethod.getReturnType().equals(void.class)) {
                    upgradeMethod.invoke(this, db);
                    to = from + 1;
                } else {
                    to = (Integer) upgradeMethod.invoke(this, db);
                }
            } catch (final InvocationTargetException e) {
                final Throwable cause = e.getCause();
                if (cause == null) {
                    throw new IllegalStateException("Invocation failed with null cause.", e);
                }
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                }
                throw new IllegalStateException("Failed to invoke upgrade Method", cause);
            } catch (final IllegalAccessException e) {
                throw new IllegalStateException("Failed to invoke upgrade Method", e);
            } catch (final NoSuchMethodException e) {
                throw new IllegalStateException(
                        "Missing upgrade from version: " + from, e);
            }
            LogUtils.i(TAG, "Upgraded database from version %d to %d", from, to);
            if (to <= from) {
                throw new IllegalStateException("UpgradeFrom" + from + "() did not advance");
            }
            if (to > newVersion) {
                throw new IllegalStateException("UpgradeFrom" + from +
                        "() returned an invalid version: " + to);
            }
            if (to == newVersion) {
                break;
            }
            from = to;
        }
    }

//    @SuppressWarnings("UnusedDeclaration")
//    public void upgradeFromX(SQLiteDatabase db) {
//    }
}
