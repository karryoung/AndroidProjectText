package com.li.androidprojecttext.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.li.androidprojecttext.R;

/**
 * Created by okkuaixiu on 2017/3/13.
 */

public class VideoActivity extends Activity {

    private Button start_bt;
    private VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        videoView = (VideoView) findViewById(R.id.video);
        start_bt = (Button) findViewById(R.id.start_bt);
        start_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"视频路径");
                //1.调用自带的播放器
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(uri, "视频路径");
//                startActivity(intent);
                //2.使用VideoView实现视频播放
                videoView.setMediaController(new MediaController(VideoActivity.this));
                videoView.setVideoURI(uri);
                videoView.start();
                videoView.requestFocus();

            }
        });
    }
}
