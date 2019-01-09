package com.example.user.paradiseapp;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class act_historyFragment extends Fragment {

    private RecyclerView recycle_act_history;
    private List<act_history> act_History = new ArrayList<>();
    public act_historyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_act_history, container, false);
        recycle_act_history = (RecyclerView) view.findViewById(R.id.recycle_act_history);
        recycle_act_history.setHasFixedSize(true);
        recycle_act_history.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recycle_act_history.setAdapter(new act_historyAdapter(getContext(),act_History));
        return view;
    }


    public interface OnFragmentInteractListenr {
    }

    private class act_historyAdapter extends RecyclerView.Adapter<act_historyAdapter.ViewHolder>{
        private Context context;
        private List<act_history> act_History;

        public act_historyAdapter(Context context, List<act_history> act_History) {
            this.context = context;
            this.act_History = act_History;
        }

        @NonNull
        @Override
        public act_historyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.cardview_act_history,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull act_historyAdapter.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return act_History.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
            }

        }
    }
}
