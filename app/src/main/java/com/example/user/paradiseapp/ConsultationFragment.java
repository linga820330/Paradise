package com.example.user.paradiseapp;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConsultationFragment extends Fragment {

    private RecyclerView recycle_cons;
    private List<cons_list> cons_List = new ArrayList<>();
    private Button bt_reserve;
    private View view;
    public ConsultationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_consultation, container, false);

         setRecyclerview();

        bt_reserve = (Button) view.findViewById(R.id.bt_reserve);
        bt_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),Consultation2Activity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    //設置已預約的諮詢紀錄Recyclerview
    private void setRecyclerview() {
        recycle_cons = (RecyclerView) view.findViewById(R.id.recycle_cons);
        recycle_cons.setHasFixedSize(true);
        recycle_cons.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recycle_cons.setAdapter(new ConsultationFragment.cons_listAdapter(getContext(),cons_List));
    }

    //設置Recyclerview配置器
    private class cons_listAdapter extends RecyclerView.Adapter<ConsultationFragment.cons_listAdapter.ViewHolder>{
        private Context context;
        private List<cons_list> cons_List;

        public cons_listAdapter(Context context, List<cons_list> cons_List) {
            this.context = context;
            this.cons_List = cons_List;

        }

        @NonNull
        @Override
        public cons_listAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.cardview_consultation,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull cons_listAdapter.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 2;
        }
        class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

            }
        }
    }

    public class cons_list {

    }
}
