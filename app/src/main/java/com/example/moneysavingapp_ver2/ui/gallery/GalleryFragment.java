package com.example.moneysavingapp_ver2.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.moneysavingapp_ver2.FriendList_item;
import com.example.moneysavingapp_ver2.Friend_Adapter;
import com.example.moneysavingapp_ver2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private String uid,nickname;
    private RecyclerView recyclerView;
    private Friend_Adapter adapter;
    private ArrayList<FriendList_item> list = new ArrayList<>();
    private Button btn_findFriend;
   // private GalleryViewModel galleryViewModel;
   private DatabaseReference database;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_gallery,container,false);
        Bundle bundle = getArguments();
        uid=bundle.getString("uid");
        nickname=bundle.getString("nickname");

        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);

        database= FirebaseDatabase.getInstance().getReference();
        database=database.child("Users").child(uid).child("Friend");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d("aaaaaa", noteDataSnapshot.getValue().toString());
                    list.add(new FriendList_item(noteDataSnapshot.getValue().toString()));
                }
                adapter = new Friend_Adapter(list);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        database.addValueEventListener(valueEventListener);


      /*  adapter = new Friend_Adapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);*/
        btn_findFriend=rootView.findViewById(R.id.btn_findFriend);
        btn_findFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("GDGD","Click");
                FragmentDialog_findFriend dialog_find_friend = new FragmentDialog_findFriend();

                dialog_find_friend.show(getActivity().getSupportFragmentManager(),"find_approval");
                dialog_find_friend.setResult(new FragmentDialog_findFriend.DialogFindFriendResult() {
                    @Override
                    public void finish(String result) { //닉네임을받아서 데이타 베이스에 내친구를 등록한다.
                        if(!result.equals("")) {
                            if(!list.contains(result)) {
                                database = FirebaseDatabase.getInstance().getReference();
                                database = database.child("Users").child(uid).child("Friend");
                                database.push().setValue(result);
                            }
                        }
                    }
                });
            }
        });
        /*galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
       // final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
         //       textView.setText(s);
            }
        });*/
        return rootView;
    }
}