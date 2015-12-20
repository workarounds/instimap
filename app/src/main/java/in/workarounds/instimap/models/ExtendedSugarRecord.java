package in.workarounds.instimap.models;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

public class ExtendedSugarRecord<T extends SugarRecord<T>> extends SugarRecord<T>{
    public void saveOrUpdate(Class type, long dbId) {
        List<T> entries = (List<T>) this.find(type, "db_id = ?", Long.toString(dbId));
        if(!entries.isEmpty()){
            long id = entries.get(0).getId();
            Log.d("ExSugar", "found db_id");
            this.setId(id);
            this.save();
        }
        else{
            this.save();
            Log.d("ExSugar", "couldn't find db_id");
        }
    }
}
