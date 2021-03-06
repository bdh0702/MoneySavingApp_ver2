package com.example.moneysavingapp_ver2.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneysavingapp_ver2.ChatRoom;
import com.example.moneysavingapp_ver2.ChatRoomAdapter;
import com.example.moneysavingapp_ver2.EnterRoomActivity;
import com.example.moneysavingapp_ver2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {
    private String uid,nickname;
    private ArrayList<ChatRoom> roomlist;
    private SlideshowViewModel slideshowViewModel;
    private RecyclerView recyclerView;
    private ChatRoomAdapter cr_Adapter;
    private DatabaseReference database;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        roomlist= new ArrayList<ChatRoom>();


        Bundle bundle = getArguments();
        uid=bundle.getString("uid");
        nickname=bundle.getString("nickname");
        database= FirebaseDatabase.getInstance().getReference();
        database = database.child("Users").child(uid).child("myroom");
        ValueEventListener valueEventListener =new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              roomlist.clear();
                for(DataSnapshot da : dataSnapshot.getChildren()){

                  roomlist.add(new ChatRoom(da.getValue().toString()));
              }
                cr_Adapter =new ChatRoomAdapter(roomlist);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                cr_Adapter.setOnItemClickListener(new ChatRoomAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        Log.d("chatRoom",cr_Adapter.getItem(pos).getRoomname());
                        Intent intent1 = new Intent(getActivity(), EnterRoomActivity.class);
                        intent1.putExtra("uid",uid);
                        intent1.putExtra("roomname",cr_Adapter.getItem(pos).getRoomname());
                        intent1.putExtra("nickname",nickname);
                        startActivity(intent1);
                    }
                });
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(cr_Adapter);

                cr_Adapter.setOnLongClickListener(new ChatRoomAdapter.OnItemLongClickListener() {

                    @Override
                    public void onItemLongClick(View v, int pos) {
                        Log.d("roomroom","longlong");
                        String item = cr_Adapter.getItem(pos).getRoomname();
                        FragmentDialogRoom_longClick deleteRoom = new FragmentDialogRoom_longClick(item,uid,nickname);
                        deleteRoom.show(getActivity().getSupportFragmentManager(),"deleteRoom_approval");
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        database.addValueEventListener(valueEventListener);





        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
       // final TextView textView = root.findViewById(R.id.text_slideshow);
        recyclerView=root.findViewById(R.id.reclv_mychat);




        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
           //     textView.setText(s);
            }
        });
        return root;
    }
    public ArrayList<ChatRoom> getRoomlist(){
       return roomlist ;
    }
}