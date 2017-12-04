package com.qinqi.debugtoolbox.dbinspector.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.dbinspector.helpers.CursorOperation;
import com.qinqi.debugtoolbox.dbinspector.helpers.DatabaseHelper;
import com.qinqi.debugtoolbox.dbinspector.helpers.PragmaType;
import com.qinqi.debugtoolbox.log.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dino on 27/02/14.
 */
public class TablePageAdapter {

    public static final int DEFAULT_ROWS_PER_PAGE = 10000;

    private final Context context;

    private final File databaseFile;

    private final String tableName;

    private int rowsPerPage = DEFAULT_ROWS_PER_PAGE;

    private int position = 0;

    private int count = 0;

    private int paddingPx;

    private String pragma;

    public TablePageAdapter(Context context, File databaseFile, String tableName, int startPage) {

        this.context = context;
        this.databaseFile = databaseFile;
        this.tableName = tableName;
        paddingPx = context.getResources().getDimensionPixelSize(R.dimen.dbinspector_row_padding);

        String keyRowsPerPage = this.context.getString(R.string.dbinspector_pref_key_rows_per_page);
        String defaultRowsPerPage = this.context.getString(R.string.dbinspector_rows_per_page_default);
        String rowsPerPage = PreferenceManager.getDefaultSharedPreferences(this.context)
                .getString(keyRowsPerPage, defaultRowsPerPage);
        this.rowsPerPage = Integer.parseInt(rowsPerPage);

        int pageCount = getPageCount();
        if (startPage > pageCount) {
            startPage = pageCount;
        }
        position = this.rowsPerPage * startPage;
    }

    public List<List<String>> getByPragma(PragmaType pragmaType) {
        switch (pragmaType) {
            case FOREIGN_KEY:
                pragma = String.format(DatabaseHelper.PRAGMA_FORMAT_FOREIGN_KEYS, tableName);
                break;
            case INDEX_LIST:
                pragma = String.format(DatabaseHelper.PRAGMA_FORMAT_INDEX, tableName);
                break;
            case TABLE_INFO:
                pragma = String.format(DatabaseHelper.PRAGMA_FORMAT_TABLE_INFO, tableName);
                break;
            default:
                Logger.w(DatabaseHelper.LOGTAG, "Pragma type unknown: " + pragmaType);
        }

        CursorOperation<List<List<String>>> operation = new CursorOperation<List<List<String>>>(databaseFile) {
            @Override
            public Cursor provideCursor(SQLiteDatabase database) {
                return database.rawQuery(pragma, null);
            }

            @Override
            public List<List<String>> provideResult(SQLiteDatabase database, Cursor cursor) {
                cursor.moveToFirst();
                return getTableRows(cursor, true);
            }
        };

        return operation.execute();
    }

    public List<List<String>> getContentPage() {

        CursorOperation<List<List<String>>> operation = new CursorOperation<List<List<String>>>(databaseFile) {
            @Override
            public Cursor provideCursor(SQLiteDatabase database) {
                return database.query(tableName, null, null, null, null, null, null);
            }

            @Override
            public List<List<String>> provideResult(SQLiteDatabase database, Cursor cursor) {
                count = cursor.getCount();
                cursor.moveToPosition(position);
                return getTableRows(cursor, false);
            }
        };

        return operation.execute();
    }

    private List<List<String>> getTableRows(Cursor cursor, boolean allRows) {

        List<List<String>> rows = new ArrayList<>();
        //List<TableRow> rows = new ArrayList<>();
        List<String> header = new ArrayList<>();
        //TableRow header = new TableRow(context);

        for (int col = 0; col < cursor.getColumnCount(); col++) {
//            TextView textView = new TextView(context);
//            textView.setText(cursor.getColumnName(col));
//            textView.setPadding(paddingPx, paddingPx , paddingPx, paddingPx );
//            textView.setTypeface(Typeface.DEFAULT_BOLD);
//
//            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
//            tableRowParams.setMargins(1, 1, 1, 1);
//            header.addView(textView,tableRowParams);
            header.add(cursor.getColumnName(col));
        }

        rows.add(header);

        boolean alternate = true;

        if (cursor.getCount() == 0) {
            return rows;
        }

        do {
            List<String> row = new ArrayList<>();
            //TableRow row = new TableRow(context);
            //row.setBackgroundColor(Color.DKGRAY);
            for (int col = 0; col < cursor.getColumnCount(); col++) {
                //TextView textView = new TextView(context);
                String strData;
                if (DatabaseHelper.getColumnType(cursor, col) == DatabaseHelper.FIELD_TYPE_BLOB) {
                    //textView.setText("(data)");
                    strData = "(data)";
                } else {
                    //textView.setText(cursor.getString(col));
                    strData = cursor.getString(col);
                }
//                textView.setPadding(paddingPx, paddingPx , paddingPx, paddingPx );
//
//                if (alternate) {
//                    textView.setBackgroundColor(context.getResources().getColor(R.color.dbinspector_alternate_row_background));
//                }
//                textView.setBackgroundColor(Color.WHITE);
//                TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
//                tableRowParams.setMargins(1, 1, 1, 1);
                //row.addView(textView,tableRowParams);
                row.add(strData);
            }

            alternate = !alternate;
            rows.add(row);

        } while (cursor.moveToNext() && (allRows || rows.size() <= rowsPerPage));

        return rows;
    }

    public void nextPage() {
        if (position + rowsPerPage < count) {
            position += rowsPerPage;
        }
    }

    public void previousPage() {
        if (position - rowsPerPage >= 0) {
            position -= rowsPerPage;
        }
    }

    public boolean hasNext() {
        return position + rowsPerPage < count;
    }

    public boolean hasPrevious() {
        return position - rowsPerPage >= 0;
    }

    public int getPageCount() {
        return (int) Math.ceil((float) count / rowsPerPage);
    }

    public int getCurrentPage() {
        return position / rowsPerPage + 1;
    }

    /**
     * Go back to the first page.
     */
    public void resetPage() {
        position = 0;
    }
}
