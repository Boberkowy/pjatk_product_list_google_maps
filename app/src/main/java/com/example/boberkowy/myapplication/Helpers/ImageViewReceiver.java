package com.example.boberkowy.myapplication.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.example.boberkowy.myapplication.Helpers.ImageViewService.enqueueWork;

public class ImageViewReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        enqueueWork(context, intent);
    }
}
