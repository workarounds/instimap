package com.mrane.database.models;

import com.orm.SugarRecord;

import java.util.List;

public class ExtendedSugarRecord<T extends SugarRecord<T>> extends SugarRecord<T>{
    public void saveOrUpdate(Class type, long dbId) {
        List<T> entries = (List<T>) this.find(type, "db_id = ?", Long.toString(dbId));

        if(!entries.isEmpty()){
            long id = entries.get(0).getId();
            this.setId(id);
            this.save();
        }
        else{
            this.save();
        }
    }
}
