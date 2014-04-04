package com.eavg.sampleapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.eavg.sampleapp.adapter.GoalItemsAdapter;
import com.eavg.sampleapp.model.Goal;
import com.eavg.sampleapp.model.Item;
import com.eavg.sampleapp.model.Items;
import com.eavg.sampleapp.model.manager.ModelManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class HomeFragment extends Fragment {

    private View mFragmentView;
    private TextView mGoalName;
    private TextView mGoalDescription;
    private ListView mGoalItemsList;
    private ProgressBar mSpinner;
    private int mGoalId;
    private Goal mGoal;

    public HomeFragment(int goalId)
    {
        mGoalId = goalId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_home, container, false);

        mGoalItemsList = (ListView)mFragmentView.findViewById(R.id.goal_items_list);

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View headerView = layoutInflater.inflate(R.layout.goal_header, mGoalItemsList, false);
        mGoalItemsList.addHeaderView(headerView);

        mGoalName = (TextView)headerView.findViewById(R.id.goal_name_text);
        mGoalDescription = (TextView)headerView.findViewById(R.id.goal_description_text);

        mSpinner = (ProgressBar)mFragmentView.findViewById(R.id.goal_items_progress);

        loadData();

        return mFragmentView;
    }

    private void loadData() {
        ModelManager modelManager = ModelManager.getInstance(getActivity());
        modelManager.fetchSingleGoal(mGoalId, successListenerForSingleGoal());
    }

    private ModelManager.ModelListener<Items> successListenerForItems() {
        return new ModelManager.ModelListener<Items>() {
            @Override
            public void onFetchedData(Items data) {
                mGoalItemsList.setAdapter(new GoalItemsAdapter(getActivity(), data, mGoal.getLanguage()));
                mSpinner.setVisibility(View.GONE);
            }
        };
    }

    private ModelManager.ModelListener<Goal> successListenerForSingleGoal() {
        return new ModelManager.ModelListener<Goal>() {
            @Override
            public void onFetchedData(Goal data) {
                mGoal = data;
                mGoalName.setText(data.getTitle());
                mGoalDescription.setText(data.getDescription());
                ModelManager.getInstance(getActivity()).fetchItemsForGoal(mGoalId, successListenerForItems());
            }
        };
    }

    @Override
    public void onDestroy() {
        ModelManager.getInstance(getActivity()).getRequestQueue().cancelAll(this);
        super.onDestroy();
    }
}
