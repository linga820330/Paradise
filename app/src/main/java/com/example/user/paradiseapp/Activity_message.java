package com.example.user.paradiseapp;



import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

//Firebase Recyclerview 試做
public class Activity_message extends AppCompatActivity {

private RecyclerView recycle_aty;
private DatabaseReference databaseReference;
private FirebaseRecyclerAdapter<activitysignup_list,Activity_message.MyViewHolder> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
        databaseReference.keepSynced(true);

        recycle_aty = (RecyclerView) findViewById(R.id.recycle_aty);
        recycle_aty.setHasFixedSize(true);
        recycle_aty.setLayoutManager(new LinearLayoutManager(this));

        //串接firebase 資料庫
        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("activity");
        Query personsQuery = personsRef.orderByKey();
        FirebaseRecyclerOptions personsOptions = new  FirebaseRecyclerOptions.Builder<activitysignup_list>().setQuery(personsQuery,activitysignup_list.class).build();

        myAdapter = new FirebaseRecyclerAdapter<activitysignup_list, MyViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull activitysignup_list model) {
                holder.setTitle(model.getTitle());
                holder.setImage(getBaseContext(),model.getImage());
                holder.setButton(getBaseContext(),position);
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.cardview_activitysignup,viewGroup,false);
                return new Activity_message.MyViewHolder(view);
            }
        };
        recycle_aty.setAdapter(myAdapter);


   }

    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View view;
       public MyViewHolder(@NonNull View itemView) {
           super(itemView);
           view = itemView;
       }
       public void setTitle(String title){
           TextView post_title = (TextView)view.findViewById(R.id.post_title);
           post_title.setText(title);
       }
       public void setImage(Context context,String image){
           PhotoView post_image = (PhotoView) view.findViewById(R.id.post_image);
           Picasso.with(context).load(image).into(post_image);
       }
       public void setButton(final Context context, final int position){
           final Button bt_signup = (Button)view.findViewById(R.id.bt_signup);
           bt_signup.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   bt_signup.setText("已報名");
                  Toast.makeText(context,"已報名"+position,Toast.LENGTH_LONG).show();
               }
           });
       }
   }

}
