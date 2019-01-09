package com.example.user.paradiseapp;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SystemFragment extends Fragment {


    public SystemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system, container, false);


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tablayout_system);
        tabLayout.addTab(tabLayout.newTab().setText("商會簡介"));
        tabLayout.addTab(tabLayout.newTab().setText("會員制度"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);//填滿tablayout
        final ViewPager viewpager_system =(ViewPager)  view.findViewById(R.id.viewpager_system);
        final SystemFragment.systemPagerAdapter adapter = new SystemFragment.systemPagerAdapter(getFragmentManager(),tabLayout.getTabCount());
        viewpager_system.setAdapter(adapter);
        viewpager_system.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager_system.setCurrentItem(tab.getPosition());
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

    public class systemPagerAdapter extends FragmentStatePagerAdapter {

        int count;
        public systemPagerAdapter(FragmentManager fm, int count) {
            super(fm);
            this.count = count;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){

                case 0:
                    ClubFragment clubFragment = new ClubFragment();
                    return clubFragment;
                case 1:
                    RmberFragment rmberFragment = new RmberFragment();
                    return rmberFragment;
            }

            return null;
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
