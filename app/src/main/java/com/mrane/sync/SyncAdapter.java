package com.mrane.sync;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrane.models.Notice;
import com.mrane.models.Venue;
import com.mrane.parser.CustomGson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    @SuppressLint("NewApi")
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /*
         * Specify the code you want to run in the sync adapter. The entire
         * sync adapter runs in a background thread, so you don't have to set
         * up your own background processing.
         */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
    /*
     * Put the data transfer code here.
     */
        Log.d("SyncAdapter", "Yo sync running");
        this.syncNotices();
        this.syncVenues();
    }

    private void syncNotices() {
        List<Notice> maxNotices = Notice.find(Notice.class, null, null, null, "db_id DESC", "1");
        long maxId = 0;
        if(!maxNotices.isEmpty()) {
            maxId = maxNotices.get(0).getDbId();
        }
        List<Notice> fromNotices = Notice.find(Notice.class, null, null, null, "modified DESC", "1");
        Date from = null;
        if(!fromNotices.isEmpty()) {
            from = fromNotices.get(0).getModified();
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("SyncAdapter", "maxId: " + maxId);
        ServiceHandler serviceHandler = new ServiceHandler();
        String data = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "notices/modified";
        params.add(new BasicNameValuePair("id", Long.toString(maxId)));
        if(from != null) {
            Log.d("SyncAdapter", "modified: " + df.format(from));
            params.add(new BasicNameValuePair("from", df.format(from)));
        }
        data = serviceHandler.getExtractedResponse(url, ServiceHandler.GET, params);
        Log.d("SyncAdapter", data);
        CustomGson customGson = new CustomGson();
        Gson gson = customGson.getGson();
        Type listType = new TypeToken<ArrayList<Notice>>() {}.getType();
        List<Notice> notices = gson.fromJson(data, listType);
        for (Notice n: notices) {
            n.saveOrUpdate(Notice.class, n.getDbId());
        }
    }

    private void syncVenues() {
        List<Venue> maxVenues = Venue.find(Venue.class, null, null, null, "db_id DESC", "1");
        long maxId = 0;
        if(!maxVenues.isEmpty()) {
            maxId = maxVenues.get(0).getDbId();
        }
        List<Venue> fromVenues = Venue.find(Venue.class, null, null, null, "modified DESC", "1");
        Date from = null;
        if(!fromVenues.isEmpty()) {
            from = fromVenues.get(0).getModified();
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Log.d("SyncAdapter", "maxId: " + maxId);
        ServiceHandler serviceHandler = new ServiceHandler();
        String data = null;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String url = "venues/modified";
        params.add(new BasicNameValuePair("id", Long.toString(maxId)));
        if(from != null) {
            Log.d("SyncAdapter", "modified: " + df.format(from));
            params.add(new BasicNameValuePair("from", df.format(from)));
        }
        data = serviceHandler.getExtractedResponse(url, ServiceHandler.GET, params);
        Log.d("SyncAdapter", data);
        CustomGson customGson = new CustomGson();
        Gson gson = customGson.getGson();
        Type listType = new TypeToken<ArrayList<Venue>>() {}.getType();
        List<Venue> venues = gson.fromJson(data, listType);
        for (Venue v: venues) {
            v.saveOrUpdate(Venue.class, v.getDbId());
        }
    }

}
