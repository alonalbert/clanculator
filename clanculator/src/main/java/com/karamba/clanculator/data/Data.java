package com.karamba.clanculator.data;


import android.util.SparseArray;
import com.karamba.clanculator.R;

/**
 * todo: javadocs.
 */
public class Data {
    public static class ItemLevel {
        public final long mGold;
        public final long mElixir;
        public final long mDarkElixir;
        public final long mTime;
        public final int mImageResId;

        public ItemLevel(long gold, long elixir, long darkElixir, long time, int imageResId) {
            mGold = gold;
            mElixir = elixir;
            mDarkElixir = darkElixir;
            mTime = time;
            mImageResId = imageResId;
        }
    }

    public static class Item {
        public static final int CANNON = 1;
        public static final int ARCHER_TOWER = 2;

        public final int mType;
        public final int mNameResId;
        public final TownHallLevelInfo[] mTownHallLevelInfos;
        public final ItemLevel[] mLevels;

        public Item(int type, int nameResId, TownHallLevelInfo[] townHallLevelInfos, ItemLevel[] levels) {
            mType = type;
            mNameResId = nameResId;
            mTownHallLevelInfos = townHallLevelInfos;
            mLevels = levels;
        }
    }

    public static class Group {
        public final int mNameResId;
        public final Item[] mItems;

        public Group(int nameResId, Item[] items) {
            mNameResId = nameResId;
            mItems = items;
        }
    }

    public static class TownHallLevelInfo {
        public final int mMaxItems;
        public final int mMaxLevel;

        public TownHallLevelInfo(int maxItems, int maxLevel) {
            mMaxItems = maxItems;
            mMaxLevel = maxLevel;
        }
    }

    public static final Group[] GROUPS = new Group[] {
            new Group(R.string.defense, new Item[] {
                    new Item(
                            Item.CANNON,
                            R.string.cannon,
                            new TownHallLevelInfo[] {
                                    new TownHallLevelInfo(2, 2),
                                    new TownHallLevelInfo(2, 3),
                                    new TownHallLevelInfo(2, 4),
                                    new TownHallLevelInfo(3, 5),
                                    new TownHallLevelInfo(3, 6),
                                    new TownHallLevelInfo(5, 7),
                                    new TownHallLevelInfo(5, 8),
                                    new TownHallLevelInfo(5, 10),
                                    new TownHallLevelInfo(5, 11),
                                    new TownHallLevelInfo(6, 12),
                            },
                            new ItemLevel[]{
                                    new ItemLevel(250, 0, 0, Time.MIN_1, R.drawable.cannon1),
                                    new ItemLevel(1000, 0, 0, Time.MIN_15, R.drawable.cannon2),
                                    new ItemLevel(4000, 0, 0, Time.MIN_45, R.drawable.cannon3),
                                    new ItemLevel(16000, 0, 0, Time.HOUR_2, R.drawable.cannon4),
                                    new ItemLevel(50000, 0, 0, Time.HOUR_6, R.drawable.cannon5),
                                    new ItemLevel(100000, 0, 0, Time.HOUR_12, R.drawable.cannon6),
                                    new ItemLevel(200000, 0, 0, Time.DAY_1, R.drawable.cannon7),
                                    new ItemLevel(400000, 0, 0, Time.DAY_2, R.drawable.cannon8),
                                    new ItemLevel(800000, 0, 0, Time.DAY_3, R.drawable.cannon9),
                                    new ItemLevel(1600000, 0, 0, Time.DAY_4, R.drawable.cannon10),
                                    new ItemLevel(3200000, 0, 0, Time.DAY_5, R.drawable.cannon11),
                                    new ItemLevel(6400000, 0, 0, Time.DAY_6, R.drawable.cannon12),
                            }),
                    new Item(
                            Item.ARCHER_TOWER,
                            R.string.archer_tower,
                            new TownHallLevelInfo[] {
                                    new TownHallLevelInfo(0, 0),
                                    new TownHallLevelInfo(1, 2),
                                    new TownHallLevelInfo(1, 3),
                                    new TownHallLevelInfo(2, 4),
                                    new TownHallLevelInfo(3, 6),
                                    new TownHallLevelInfo(3, 7),
                                    new TownHallLevelInfo(4, 8),
                                    new TownHallLevelInfo(5, 10),
                                    new TownHallLevelInfo(6, 11),
                                    new TownHallLevelInfo(7, 12),
                            },
                            new ItemLevel[]{
                                    new ItemLevel(240, 0, 0, Time.MIN_1, R.drawable.archer_tower1),
                                    new ItemLevel(1000, 0, 0, Time.MIN_15, R.drawable.archer_tower2),
                                    new ItemLevel(4000, 0, 0, Time.MIN_45, R.drawable.archer_tower3),
                                    new ItemLevel(16000, 0, 0, Time.HOUR_2, R.drawable.archer_tower4),
                                    new ItemLevel(50000, 0, 0, Time.HOUR_6, R.drawable.archer_tower5),
                                    new ItemLevel(100000, 0, 0, Time.HOUR_12, R.drawable.archer_tower6),
                                    new ItemLevel(200000, 0, 0, Time.DAY_1, R.drawable.archer_tower7),
                                    new ItemLevel(400000, 0, 0, Time.DAY_2, R.drawable.archer_tower8),
                                    new ItemLevel(800000, 0, 0, Time.DAY_3, R.drawable.archer_tower9),
                                    new ItemLevel(1600000, 0, 0, Time.DAY_4, R.drawable.archer_tower10),
                                    new ItemLevel(3200000, 0, 0, Time.DAY_5, R.drawable.archer_tower11),
                            }),
            }),
    };

    public static final SparseArray<Item> ITEMS = new SparseArray<Item>();

    static {
        for (Group group : GROUPS) {
            for (Item item : group.mItems) {
                ITEMS.put(item.mType, item);
            }
        }
    }
}
