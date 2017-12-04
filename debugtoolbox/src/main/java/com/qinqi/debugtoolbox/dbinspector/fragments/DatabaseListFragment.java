package com.qinqi.debugtoolbox.dbinspector.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.qinqi.debugtoolbox.R;
import com.qinqi.debugtoolbox.dbinspector.helpers.DatabaseHelper;

import java.io.File;

/**
 * Created by dino on 23/02/14.
 */
public class DatabaseListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = (Activity) getActivity();

        activity.getActionBar().setTitle(getString(R.string.dbinspector_databases));
        activity.getActionBar().setDisplayHomeAsUpEnabled(true);

        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,
                DatabaseHelper.getDatabaseList(getActivity()));

        setListAdapter(adapter);

        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.dbinspector_container,
                TableListFragment.newInstance((File) getListAdapter().getItem(position)));
        ft.addToBackStack(null).commit();
        getFragmentManager().executePendingTransactions();
    }
}
