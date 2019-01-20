package io.aligh.ihttp.classes;

import android.content.Context;
import android.content.SharedPreferences;

final class CacheSP {

    private final SharedPreferences sp;

    private String key = "";

    CacheSP(Context context, String key) {
        this.key = key;
        String tag = "iHttp";
        sp = context.getSharedPreferences(tag, Context.MODE_PRIVATE);
    }

    void put(String value) {
        if (count() >= 50)
            getEditor().clear();
        if (!contains())
            getEditor().putString(key, String.valueOf(value)).apply();
    }

    protected String get() {
        return sp.getString(key, null);
    }

    void clear(){
        getEditor().clear();
    }

    boolean contains() {
        return sp.contains(key);
    }


    private long count() {
        return sp.getAll().size();
    }

    private SharedPreferences.Editor getEditor() {
        return sp.edit();
    }
}
