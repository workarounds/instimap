package com.mrane.campusmap;

import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mrane.data.Locations;
import com.mrane.data.Marker;
import com.mrane.models.Notice;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.designlabs.instimap.R;

/**
 * Created by manidesto on 29/12/14.
 */
public class BarViewController implements LoaderManager.LoaderCallbacks<List<Marker>>{

    public interface BarViewCallbacks {
        public void onEventListChanged(ArrayList<Marker> eventList);
    }

    public static class VenuesListLoader extends SimpleListLoader<Marker>{
        private Date date;
        private Context context;
        public VenuesListLoader(Context context, Date date) {
            super(context);
            this.date = date;
            this.context = context;
        }

        @Override
        public List<Marker> loadInBackground() {
            return getVenueMarkersList(context, date);
        }
    }

    MapActivity context;
    View barView;
    BarViewCallbacks listener;
    Date date;
    SettingsManager settingsManager;
    public BarViewController(MapActivity context, View barView){
        this.context = context;
        this.barView = barView;

        initialize();
    }

    private void initialize(){
        settingsManager = SettingsManager.getInstance(context);
        date = new Date();
        setDate();

        ImageButton next = (ImageButton) barView.findViewById(R.id.bar_next_button);
        next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToNext();
            }
        });

        ImageButton prev = (ImageButton) barView.findViewById(R.id.bar_prev_button);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPrev();
            }
        });
    }

    private void setDate(){
        if(settingsManager.isInEventsMode()) {
            DateFormat dateFormat = new SimpleDateFormat("MMM dd");
            String dateString = dateFormat.format(date);
            dateString = dateString.toUpperCase();

            TextView dateView = (TextView) barView.findViewById(R.id.date_text_view);
            dateView.setText(dateString);

            context.getSupportLoaderManager().initLoader(0,null,this);
        }
    }

    private static ArrayList<Marker> getVenueMarkersList(Context context, Date date){
        ArrayList<Marker> result = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

        List<Notice> notices = Notice.find(Notice.class, "start_time BETWEEN ? AND ?", Long.toString(startDate.getTime()), Long.toString(endDate.getTime()));
        ArrayList<Long> venueIds = new ArrayList<>();
        for(Notice n: notices){
            if(venueIds.indexOf(n.getVenueId()) == -1) {
                if(n.getVenueId() != 0) {
                    venueIds.add(n.getVenueId());
                }
            }
        }

        Locations locations = Locations.getInstance(context);
        for(Long venueId: venueIds){
            Marker marker = locations.getMarkerById(venueId);
            result.add(marker);
        }

        return result;
    }

    private void goToNext(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        date = calendar.getTime();
        setDate();
    }

    private void goToPrev(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        setDate();
    }

    @Override
    public Loader<List<Marker>> onCreateLoader(int id, Bundle args) {
        return new VenuesListLoader(context, date);
    }

    @Override
    public void onLoadFinished(Loader<List<Marker>> loader, List<Marker> data) {
        if(listener != null){
            listener.onEventListChanged((ArrayList<Marker>)data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Marker>> loader) {

    }

    public void setBarViewCallbackListener(BarViewCallbacks listener){
        this.listener = listener;
    }
}
