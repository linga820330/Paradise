package com.example.user.paradiseapp;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.disklrucache.DiskLruCache;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class act_listFragment extends Fragment {
    private RecyclerView recycle_act_list;
    private List<act_list> act_List = new ArrayList<>();
    private View view;
    public act_listFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_act_list, container, false);

      //  act_List.add(new act_list(0,"1111",R.drawable.one));
      //  act_List.add(new act_list(1,"2222",R.drawable.two));

        setRecyclerview();



        return view;
    }

    //設置setRecyclerview
    private void setRecyclerview() {
        recycle_act_list = (RecyclerView) view.findViewById(R.id.recycle_atc_list);
        recycle_act_list.setHasFixedSize(true);
        recycle_act_list.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recycle_act_list.setAdapter(new act_listAdapter(getContext(),act_List));
    }

    public interface OnFragmentInteractListenr {
    }

    //Recyclerview配置器
    private class act_listAdapter extends RecyclerView.Adapter<act_listAdapter.ViewHolder>{

        private Context context;
        private List<act_list> act_List;
        public act_listAdapter(Context context, List<act_list> act_list) {
            this.context = context;
            this.act_List = act_list;

        }

        @NonNull
        @Override
        public act_listAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(context).inflate(R.layout.cardview_activity,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull act_listAdapter.ViewHolder viewHolder, int i) {
            viewHolder.ib_activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent intent = new Intent(context,ACT_DetailActivity.class);
                   startActivity(intent);
                }
            });
        //    final act_list Act_list = act_List.get(i);
        //    viewHolder.title.setText(Act_list.getTitle());
        //    viewHolder.imageView.setImageResource(Act_list.getImage());
        //    viewHolder.position.setText(String.valueOf(Act_list.getPosition()));
        //    viewHolder.bt_detail.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //        public void onClick(View v) {
        //            Intent intent = new Intent(context,ACT_DetailActivity.class);
        //            startActivity(intent);
        //        }
        //    });
        //    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
        //        @Override
        //        public void onClick(View v) {
        //            ImageView imageView = new ImageView(context);
        //            imageView.setImageResource(Act_list.getImage());
        //            Toast toast = new Toast(context);
        //            toast.setView(imageView);
        //            toast.setDuration(Toast.LENGTH_LONG);
        //            toast.show();
        //        }
        //    });

        }

        @Override
        public int getItemCount() {
            return 2;
        }

        //Adapter 需要一個 ViewHolder，只要實作它的 constructor 就好，保存起來的view會放在itemView裡面
        class ViewHolder extends RecyclerView.ViewHolder{
            ImageButton ib_activity;
          //  ImageView imageView;
          //  TextView title,position;
          //  Button bt_detail;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ib_activity = (ImageButton) itemView.findViewById(R.id.ib_activity);
           //     title = (TextView) itemView.findViewById(R.id.post_title);
           //     imageView = (ImageView) itemView.findViewById(R.id.post_image);
           //     position = (TextView) itemView.findViewById(R.id.text_position);
           //     bt_detail = (Button) itemView.findViewById(R.id.bt_detail);
            }
        }
    }
}
