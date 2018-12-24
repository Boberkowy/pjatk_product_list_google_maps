package com.example.boberkowy.myapplication.Helpers;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import java.io.File;
import java.io.IOException;

public class MusicService extends JobIntentService {

    public static final int JOB_ID = 0;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, MusicService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String home = Environment.getExternalStorageDirectory().toString() + "/PJATK - App/songs/";
        File fileList[] = new File(home).listFiles();
        MediaPlayer mediaPlayer = MusicSingleton.getInstance();
        String action = intent.getAction() != null ? intent.getAction() : "";

        switch (action) {
            case "stop": {
                mediaPlayer.stop();
                mediaPlayer.reset();
                MusicSingleton.setPaused(false);
                break;
            }
            case "play": {
                if (!mediaPlayer.isPlaying()) {
                    try {
                        if (!MusicSingleton.isPaused()) {
                            String url = fileList[MusicSingleton.getCurrentSong()].getPath();
                            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(url));
                            mediaPlayer.prepare();
                        }
                        mediaPlayer.start();
                        MusicSingleton.setPaused(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "next": {
                mediaPlayer.stop();
                mediaPlayer.reset();
                if (MusicSingleton.getCurrentSong() + 1 >= fileList.length) {
                    MusicSingleton.setCurrentSong(0);
                } else {
                    MusicSingleton.setCurrentSong(MusicSingleton.getCurrentSong() + 1);
                }
                break;
            }
            case "pause": {
                break;

            }
        }
    }
}
