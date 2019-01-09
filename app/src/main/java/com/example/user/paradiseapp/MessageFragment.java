package com.example.user.paradiseapp;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements act_listFragment.OnFragmentInteractListenr,act_historyFragment.OnFragmentInteractListenr {


    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout_act);
        tabLayout.addTab(tabLayout.newTab().setText("活動報名"));
        tabLayout.addTab(tabLayout.newTab().setText("報名記錄"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//填滿tablayout
        final ViewPager viewpager_act =(ViewPager)  view.findViewById(R.id.viewpager_act);
        final actPagerAdapter adapter = new actPagerAdapter(getFragmentManager(),tabLayout.getTabCount());
        viewpager_act.setAdapter(adapter);
        viewpager_act.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager_act.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        return view;

    }
    public class actPagerAdapter extends FragmentStatePagerAdapter {

        int count;
        public actPagerAdapter(FragmentManager fm,int count) {
            super(fm);
            this.count = count;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){

                case 0:
                    act_listFragment act_listFragment = new act_listFragment();
                    return act_listFragment;
                case 1:
                    act_historyFragment act_historyFragment = new act_historyFragment();
                    return act_historyFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
