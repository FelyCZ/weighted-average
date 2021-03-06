package cz.fely.weightedaverage.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cz.fely.weightedaverage.R;
import cz.fely.weightedaverage.activities.MainActivity;
import cz.fely.weightedaverage.db.Database;

public class ListAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;

    public ListAdapter(MainActivity activity, Cursor cursor, int flags) {
        super(activity, cursor, flags);
        mLayoutInflater = LayoutInflater.from(activity);
    }

    public View newView(Context context, Cursor cusor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.colmn_row, parent, false);
    }

    public void bindView(View v, Context context, Cursor c) {
        ((TextView) v.findViewById(R.id.name)).setText(c.getString(c.getColumnIndex(Database.COLUMN_NAME)));
        ((TextView) v.findViewById(R.id.mark)).setText(c.getString(c.getColumnIndex(Database.COLUMN_MARK)));
        ((TextView) v.findViewById(R.id.weight)).setText(c.getString(c.getColumnIndex(Database.COLUMN_WEIGHT)));
    }
}