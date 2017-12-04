package com.qinqi.debugtoolbox.dbinspector.fragments;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.dbinspector.activities.DataGridView;
import com.qinqi.debugtoolbox.dbinspector.adapters.TablePageAdapter;
import com.qinqi.debugtoolbox.dbinspector.helpers.PragmaType;
import com.qinqi.debugtoolbox.dbinspector.services.ClearTableIntentService;

import java.io.File;
import java.util.List;

/**
 * Created by dino on 24/02/14.
 */
public class TableFragment extends Fragment implements ActionBar.OnNavigationListener {

    private static final String KEY_DATABASE = "database_name";

    private static final String KEY_TABLE = "table_name";

    private static final String KEY_SHOWING_CONTENT = "showing_content";

    private static final String KEY_PAGE = "current_page";

    private static final String KEY_LAST_PRAGMA = "last_pragma";

    public static final int DROPDOWN_CONTENT_POSITION = 0;

    public static final int DROPDOWN_INFO_POSITION = 1;

    public static final int DROPDOWN_FOREIGN_KEYS_POSITION = 2;

    public static final int DROPDOWN_INDEXES_POSITION = 3;

    private File databaseFile;

    private String tableName;

    //private TableLayout     tableLayout;
    //private TableLayout     tableHeaderLayout;
    private TablePageAdapter adapter;

    //private View nextButton;

    //private View previousButton;

    //private TextView currentPageText;

    //private View contentHeader;

    //private ScrollView scrollView;

    //private HorizontalScrollView horizontalScrollView;

    private boolean showingContent = true;

    private PragmaType lastPragmaType = PragmaType.TABLE_INFO;

    private int currentPage;

    private BroadcastReceiver mClearTableReceiver = new ClearTableReceiver();
    private TextView emptyView;

    private ProgressDialog dialog = null;
    private DataGridView dataGridView;
    private View.OnClickListener nextListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            currentPage++;
//            adapter.nextPage();
//            showContent();
//
//            scrollView.scrollTo(0, 0);
//            horizontalScrollView.scrollTo(0, 0);
        }
    };
    private View.OnClickListener previousListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
//            currentPage--;
//            adapter.previousPage();
//            showContent();
//
//            scrollView.scrollTo(0, 0);
//            horizontalScrollView.scrollTo(0, 0);
        }
    };

    public static TableFragment newInstance(File databaseFile, String tableName) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_DATABASE, databaseFile);
        args.putString(KEY_TABLE, tableName);

        TableFragment tf = new TableFragment();
        tf.setArguments(args);

        return tf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
            databaseFile = (File) getArguments().getSerializable(KEY_DATABASE);
            tableName = getArguments().getString(KEY_TABLE);
        }

        if (savedInstanceState != null) {
            showingContent = savedInstanceState.getBoolean(KEY_SHOWING_CONTENT, true);
            lastPragmaType = PragmaType.values()[savedInstanceState.getInt(KEY_LAST_PRAGMA, 1)];
            currentPage = savedInstanceState.getInt(KEY_PAGE, 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_dbinspector_table, container, false);

       dataGridView = (DataGridView)view.findViewById(R.id.datagrid);
//        tableLayout = (TableLayout) view.findViewById(R.id.dbinspector_table_layout);
//        tableHeaderLayout = (TableLayout) view.findViewById(R.id.dbinspector_table_layout_container);
//        previousButton = view.findViewById(R.id.dbinspector_button_previous);
//        nextButton = view.findViewById(R.id.dbinspector_button_next);
//        currentPageText = (TextView) view.findViewById(R.id.dbinspector_text_current_page);
//        contentHeader = view.findViewById(R.id.dbinspector_layout_content_header);
//        scrollView = (ScrollView) view.findViewById(R.id.dbinspector_scrollview_table);
//        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.dbinspector_horizontal_scrollview_table);
        emptyView = (TextView)view.findViewById(R.id.emptyView);

        //previousButton.setOnClickListener(previousListener);
        //nextButton.setOnClickListener(nextListener);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpActionBar();
        adapter = new TablePageAdapter(getActivity(), databaseFile, tableName, currentPage);
//        if (showingContent) {
//            showContent();
//        } else {
//            showByPragma(lastPragmaType);
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpActionBar();
        ClearTableIntentService.registerListener(getActivity(), mClearTableReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        ClearTableIntentService.unregisterListener(getActivity(), mClearTableReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_SHOWING_CONTENT, showingContent);
        outState.putInt(KEY_PAGE, currentPage);
        outState.putInt(KEY_LAST_PRAGMA, lastPragmaType.ordinal());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        ((Activity) getActivity()).getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_dbinspector_frag_table, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.dbinspector_action_clear_table) {
            ClearTableAlertDialogFragment fragment = ClearTableAlertDialogFragment.newInstance(databaseFile, tableName);
            fragment.show(getFragmentManager(), "CONFIRM_DIALOG");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpActionBar() {
        final ActionBar actionBar = getActivity().getActionBar();

        actionBar.setTitle(tableName);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Set up the action bar to show a dropdown list.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<>(actionBar.getThemedContext(), android.R.layout.simple_list_item_1,
                        android.R.id.text1, new String[]{getString(R.string.dbinspector_content),
                        getString(R.string.dbinspector_structure), getString(R.string.dbinspector_foreign_keys),
                        getString(R.string.dbinspector_indexes)}),
                this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void showContent() {
        showingContent = true;
        emptyView.setVisibility(View.VISIBLE);
        //horizontalScrollView.setVisibility(View.GONE);

        //tableLayout.removeAllViews();
//        for(int i=0; i< tableHeaderLayout.getChildCount(); i++){
//            View v = tableHeaderLayout.getChildAt(i);
//            if(v instanceof TableRow){
//                tableHeaderLayout.removeView(v);
//                break;
//            }
//        }
        List<List<String>> rows = adapter.getContentPage();
        if(rows.size() > 0){
            dataGridView.setData(rows,true);
        }
//        for (int i=0; i< rows.size(); i++) {
//            tableLayout.addView(rows.get(i));
//        }
//        final TableRow header = (TableRow)rows.get(0);

        emptyView.setVisibility(View.GONE);
        //horizontalScrollView.setVisibility(View.VISIBLE);


        //calc width save
//        header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                // Ensure you call it only once :
//                header.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                //remove headerview
//                for(int i=0; i< tableHeaderLayout.getChildCount(); i++){
//                    View v = tableHeaderLayout.getChildAt(i);
//                    if(v instanceof TableRow){
//                        tableHeaderLayout.removeView(v);
//                        break;
//                    }
//                }
//                //calc header width&height  hide old header
//                List<Integer> widthList = new ArrayList<>();
//                int columnHeight = 0;
//                for (int i=0; i< header.getChildCount(); i++) {
//                    View v2 = header.getVirtualChildAt(i);
//                    int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                    int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//                    v2.measure(w, h);
//                    widthList.add(v2.getWidth());
//                    columnHeight = v2.getHeight();
//                    ViewGroup.LayoutParams param = v2.getLayoutParams();
//                    param.height = 0;
//                    v2.setLayoutParams(param);
//                }
//
//                //create new header
//                TableRow fixedHheader = new TableRow(getActivity());
//                fixedHheader.setBackgroundColor(Color.DKGRAY);
//                for (int col = 0; col < header.getChildCount(); col++) {
//                    TextView textView = new TextView(getActivity());
//                    TextView tv = (TextView)header.getChildAt(col);
//                    textView.setWidth(widthList.get(col));
//                    textView.setHeight(columnHeight);
//                    textView.setText(tv.getText());
//                    int padding = getActivity().getResources().getDimensionPixelSize(R.dimen.dbinspector_row_padding);
//                    textView.setPadding(padding,padding,padding,padding);
//                    textView.setTypeface(Typeface.DEFAULT_BOLD);
//                    textView.setBackgroundColor(Color.GRAY);
//                    TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams();
//                    tableRowParams.setMargins(1, 1, 1, 1);
//                    fixedHheader.addView(textView,tableRowParams);
//                }
//                tableHeaderLayout.addView(fixedHheader, 0);
//            }
//        });

//        currentPageText.setText(adapter.getCurrentPage() + "/" + adapter.getPageCount());
//
//        contentHeader.setVisibility(View.GONE);
//
//        nextButton.setEnabled(adapter.hasNext());
//        previousButton.setEnabled(adapter.hasPrevious());
    }

    private void showByPragma(PragmaType pragmaType) {
        lastPragmaType = pragmaType;
        showingContent = false;
//        tableLayout.removeAllViews();
//        for(int i=0; i< tableHeaderLayout.getChildCount(); i++){
//            View v = tableHeaderLayout.getChildAt(i);
//            if(v instanceof TableRow){
//                tableHeaderLayout.removeView(v);
//                break;
//            }
//        }
//
        List<List<String>> rows = adapter.getByPragma(pragmaType);
        if(rows.size() > 0){
            dataGridView.setData(rows,true);
        }
//        for (TableRow row : rows) {
//            tableLayout.addView(row);
//        }

        //contentHeader.setVisibility(View.GONE);
    }

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {

        switch (itemPosition) {
            case DROPDOWN_CONTENT_POSITION:
                showContent();
                break;
            case DROPDOWN_INFO_POSITION:
                showByPragma(PragmaType.TABLE_INFO);
                break;
            case DROPDOWN_FOREIGN_KEYS_POSITION:
                showByPragma(PragmaType.FOREIGN_KEY);
                break;
            case DROPDOWN_INDEXES_POSITION:
                showByPragma(PragmaType.INDEX_LIST);
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * Listen for the result of deleting the content of a table.
     */
    private class ClearTableReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ClearTableIntentService.isSuccess(intent)) {
                adapter.resetPage();
                showContent();
            }
        }
    }
}
