package com.karamba.clanculator;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.karamba.clanculator.data.Data;
import com.karamba.clanculator.data.Data.Item;
import com.karamba.clanculator.database.Contract;
import com.karamba.clanculator.database.Contract.Inventory;
import com.karamba.clanculator.database.DatabaseHelper;

/**
 * todo: javadocs.
 */
public class ItemListFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final SQLiteDatabase db = new DatabaseHelper(getActivity()).getWritableDatabase();
        assert db != null;
        final Cursor cursor = db.query(
                Inventory.TABLE_NAME,
                new String[]{
                        Inventory._ID,
                        Inventory.NUMBER,
                },
                Inventory.TYPE + "=?",
                new String[] {
                        String.valueOf(Item.CANNON),
                },
                null,
                null,
                null);
        setListAdapter(new ItemListAdapter(getActivity(), R.layout.item_list_item, cursor, true));
    }

    private class ItemListAdapter extends ResourceCursorAdapter {


        public ItemListAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
            super(context, layout, c, autoRequery);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final int num = cursor.getInt(1);
            final TextView numView = (TextView) view.findViewById(R.id.number);
            numView.setText(String.valueOf(num));
        }
    }
}