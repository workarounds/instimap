package com.mrane.models;

import com.orm.SugarRecord;

/**
 * Created by manidesto on 24/12/14.
 */
public class Notice extends SugarRecord<Notice> {
    String title;
    String edition;

    public Notice(){

    }

    public Notice(String title, String edition){
        this.title = title;
        this.edition = edition;
    }

}
