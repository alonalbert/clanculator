package com.karamba.clanculator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import com.karamba.clanculator.data.Data;
import com.karamba.clanculator.data.Data.Item;
import com.karamba.clanculator.data.Data.ItemLevel;
import com.karamba.clanculator.data.Data.TownHallLevelInfo;
import com.karamba.clanculator.database.Contract.Inventory;
import com.karamba.clanculator.database.DatabaseHelper;
import com.karamba.clanculator.utils.Utils;

/**
 * todo: javadocs.
 */
public class ItemListFragment extends ListFragment {
    private static final String ARG_TOWN_HALL_LEVEL = "townHallLevel";
    private static final String ARG_ITEM_TYPE = "itemType";
    private TownHallLevelInfo mTownHallLevelInfo;

    public static ItemListFragment newInstance(int townHallLevel, int itemType) {
        final ItemListFragment fragment = new ItemListFragment();
        final Bundle args = new Bundle();
        args.putInt(ARG_TOWN_HALL_LEVEL, townHallLevel);
        args.putInt(ARG_ITEM_TYPE, itemType);
        fragment.setArguments(args);
        return fragment;
    }

    public ItemListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final Bundle args = getArguments();
        final int townHallLevel = args.getInt(ARG_TOWN_HALL_LEVEL);
        final int itemType = args.getInt(ARG_ITEM_TYPE);

        final Item item = Data.ITEMS.get(itemType);
        final SQLiteDatabase db = new DatabaseHelper(getActivity()).getWritableDatabase();
        assert db != null;
        mTownHallLevelInfo = item.mTownHallLevelInfos[townHallLevel - 1];
        final Cursor cursor = db.query(
                Inventory.TABLE_NAME,
                new String[]{
                        Inventory._ID,
                        Inventory.NUMBER,
                        Inventory.LEVEL,
                },
                Inventory.TYPE + "=? AND " + Inventory.NUMBER + "<?",
                new String[] {
                        String.valueOf(itemType),
                        String.valueOf(mTownHallLevelInfo.mMaxItems),
                },
                null,
                null,
                null);
        setListAdapter(new ItemListAdapter(getActivity(), R.layout.item_list_item, cursor, true, item));
    }

    private class ItemListAdapter extends ResourceCursorAdapter {


        private final Item mItem;

        public ItemListAdapter(Context context, int layout, Cursor c, boolean autoRequery, Item item) {
            super(context, layout, c, autoRequery);
            mItem = item;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final int num = cursor.getInt(1);
            final int level = num + 1;

            final TextView numView = (TextView) view.findViewById(R.id.number);
            numView.setText(String.valueOf(num + 1));

            final TextView levelView = (TextView) view.findViewById(R.id.level);
            levelView.setText(getString(R.string.level_indicator, level));

            final ImageView itemImageView = (ImageView) view.findViewById(R.id.itemImage);
            itemImageView.setBackgroundResource(mItem.mLevels[level - 1].mImageResId);

            final int maxLevel = mTownHallLevelInfo.mMaxLevel;
            long gold = 0;
            long time = 0;
            for (int i = level; i < maxLevel; ++i) {
                final ItemLevel itemLevel = mItem.mLevels[i];
                gold += itemLevel.mGold;
                time += itemLevel.mTime;
            }

            final TextView goldView = (TextView) view.findViewById(R.id.resource);
            goldView.setText(Utils.longToAmount(gold));

            final TextView timeView = (TextView) view.findViewById(R.id.time);
            timeView.setText(Utils.longToTime(time));

        }
    }
}