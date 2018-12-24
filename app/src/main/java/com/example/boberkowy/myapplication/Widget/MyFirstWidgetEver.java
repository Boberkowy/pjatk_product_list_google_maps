package com.example.boberkowy.myapplication.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.boberkowy.myapplication.Helpers.ImageViewReceiver;
import com.example.boberkowy.myapplication.Helpers.MusicReceiver;
import com.example.boberkowy.myapplication.Helpers.MusicSingleton;
import com.example.boberkowy.myapplication.R;

/**
 * Implementation of App Widget functionality.
 */
public class MyFirstWidgetEver extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_first_widget_ever);
        setGoToWebButton(context, views);
        setMusicButtons(context, views);
        MusicSingleton.Initialize();
        setImageViewButton(context, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void setGoToWebButton(Context context, RemoteViews views) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("http://www.google.com"));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
    }

    private static void setMusicButtons(Context context, RemoteViews views) {
        Intent stopIntent = new Intent(context, MusicReceiver.class);
        Intent playIntent = new Intent(context, MusicReceiver.class);
        Intent nextIntent = new Intent(context, MusicReceiver.class);
        Intent pauseIntent = new Intent(context, MusicReceiver.class);

        stopIntent.setAction("stop");
        playIntent.setAction("play");
        nextIntent.setAction("next");
        pauseIntent.setAction("pause");

        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(context, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.widget_stop, stopPendingIntent);
        views.setOnClickPendingIntent(R.id.widget_play, playPendingIntent);
        views.setOnClickPendingIntent(R.id.widget_next, nextPendingIntent);
        views.setOnClickPendingIntent(R.id.widget_pause, pausePendingIntent);

    }

    private static void setImageViewButton(Context context, RemoteViews views) {
        Intent nextImageIntent = new Intent(context, ImageViewReceiver.class);
        nextImageIntent.setAction("next_image");

        PendingIntent imagePendingIntent = PendingIntent.getBroadcast(context, 0, nextImageIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setOnClickPendingIntent(R.id.imageView, imagePendingIntent);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.my_first_widget_ever);

        if (intent.getAction().equals("some_intent")) {
            appWidgetManager.updateAppWidget(new ComponentName(context, MyFirstWidgetEver.class), views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

