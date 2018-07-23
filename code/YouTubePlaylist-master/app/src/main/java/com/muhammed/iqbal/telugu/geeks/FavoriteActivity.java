package com.muhammed.iqbal.telugu.geeks;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.muhammed.iqbal.telugu.R;
import com.muhammed.iqbal.telugu.geeks.adapter.FavoritelistCardAdapter;
import com.muhammed.iqbal.telugu.geeks.db.VideoViewModel;

public class FavoriteActivity extends AppCompatActivity {
    private VideoViewModel mVideoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mVideoViewModel = ViewModelProviders.of(this).get(VideoViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final FavoritelistCardAdapter adapter = new FavoritelistCardAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
