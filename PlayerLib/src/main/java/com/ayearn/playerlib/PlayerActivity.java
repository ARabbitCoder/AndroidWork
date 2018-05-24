package com.ayearn.playerlib;

import android.app.Activity;
import android.os.Bundle;

import com.ayearn.playerlib.base.PlayItem;

public class PlayerActivity extends Activity {
    StanardPlayerView stanardPlayerView;
    private String url = "http://10.5.6.240:8080/news/guanggao1.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        stanardPlayerView = findViewById(R.id.player);
        PlayItem playItem = new PlayItem();
        playItem.setPlayUrl(url);
        stanardPlayerView.startPlay(playItem);
    }
}
