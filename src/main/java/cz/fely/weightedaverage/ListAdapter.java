package cz.fely.weightedaverage;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.fely.weightedaverage.db.DatabaseAdapter;

public class ListAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;

    public ListAdapter(MainActivity activity, Cursor cursor, int flags) {
        super(activity, cursor, flags);
        this.mLayoutInflater = LayoutInflater.from(activity);
    }

    public View newView(Context context, Cursor cusor, ViewGroup parent) {
        return this.mLayoutInflater.inflate(R.layout.colmn_row, parent, false);
    }

    public void bindView(View v, Context context, Cursor c) {
        ((TextView) v.findViewById(R.id.name)).setText(c.getString(c.getColumnIndex(DatabaseAdapter.COLUMN_NAME)));
        ((TextView) v.findViewById(R.id.mark)).setText(c.getString(c.getColumnIndex(DatabaseAdapter.COLUMN_MARK)));
        ((TextView) v.findViewById(R.id.weight)).setText(c.getString(c.getColumnIndex(DatabaseAdapter.COLUMN_WEIGHT)));
    }
}
