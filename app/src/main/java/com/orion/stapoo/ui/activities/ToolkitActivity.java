package com.orion.stapoo.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orion.stapoo.R;
import com.orion.stapoo.adapters.YoutubeVideoAdapter;
import com.orion.stapoo.interfaces.ClickInterface;
import com.orion.stapoo.models.ToolKitItem;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class ToolkitActivity extends AppCompatActivity {
    List<ToolKitItem> linkList;
    private ClickInterface clickInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolkit);

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        linkList = new ArrayList<>();
        clickInterface = new ClickInterface() {
            @Override
            public void cardClickInterface(String videoId) {
                Intent intent = new Intent(getApplicationContext(), ToolkitVideoPlayActivity.class);
                intent.putExtra("videoId", videoId);
                startActivity(intent);
            }
        };

        FirebaseDatabase.getInstance().getReference().child("toolkitLinks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                linkList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    linkList.add(dataSnapshot.getValue(ToolKitItem.class));
                }
                recyclerView.setAdapter(new YoutubeVideoAdapter(linkList, clickInterface));
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                //progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

