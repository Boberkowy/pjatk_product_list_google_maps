package com.example.boberkowy.myapplication.Helpers;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

public class ImageViewService extends JobIntentService {

    public static final int JOB_ID = 1234;

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, ImageViewService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
}
