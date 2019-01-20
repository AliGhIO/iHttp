package io.aligh.ihttp.interfaces;

import org.json.JSONArray;

public interface OnJsonArrayReceive extends Web {
    void jsonArray(JSONArray response, String source);
}
