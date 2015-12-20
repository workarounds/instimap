package in.workarounds.instimap.campusmap;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.workarounds.instimap.models.Notice;

import java.util.List;

import in.designlabs.instimap.R;

public class SettingsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Notice>>{



    public static class NoticesLoader extends SimpleListLoader<Notice>{

        public NoticesLoader(Context context){
            super(context);
        }

        @Override
        public List<Notice> loadInBackground() {
            return Notice.listAll(Notice.class);
        }
    }

    @Override
    public Loader<List<Notice>> onCreateLoader(int id, Bundle args) {
        return new NoticesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Notice>> loader, List<Notice> data) {
        for(Notice n: data){
            Log.d("MapActivity", "notice : " + n.getId());
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Notice>> loader) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loadNotices();
        View rootView = inflater.inflate(R.layout.settings, container, false);
        return rootView;
    }

    public void loadNotices(){
        getLoaderManager().initLoader(0, null, this);
    }

}
