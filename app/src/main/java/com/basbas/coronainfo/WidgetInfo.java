package com.basbas.coronainfo;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.basbas.coronainfo.model.ResponseData;
import com.basbas.coronainfo.network.ApiInterface;
import com.basbas.coronainfo.network.ApiService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WidgetInfo extends AppWidgetProvider {

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.view_widget);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM  yyyy   ", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        views.setTextViewText(R.id.tv_date, currentDateandTime);


        // Create a very simple REST adapter which points the GitHub API endpoint.
        ApiInterface client = ApiService.createService(ApiInterface.class);
        // Fetch and print a list of the contributors to this library.
        Call<List<ResponseData>> call = client.getData();

        call.enqueue(new Callback<List<ResponseData>>() {
            @Override
            public void onResponse(Call<List<ResponseData>> call, Response<List<ResponseData>> response) {
                try {
                    List<ResponseData> mResponseData = response.body();
                    views.setTextViewText(R.id.positif, mResponseData.get(0).getPositif());
                    views.setTextViewText(R.id.sembuh, mResponseData.get(0).getSembuh());
                    views.setTextViewText(R.id.meninggal, mResponseData.get(0).getMeninggal());

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<List<ResponseData>> call, Throwable t) {
                Log.e("ERROR", "Error bro " + t.getMessage());
            }
        });

        //untuk ketika widget di klik akan mengarah ke MainActivity
        RemoteViews viewss = new RemoteViews(context.getPackageName(), R.layout.view_widget);
        Intent mIntent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        mIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.rootLayout, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, viewss);
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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_widget);
        ComponentName thisWidget = new ComponentName(context, WidgetInfo.class);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}