package io.aligh.ihttp.interfaces;

import org.json.JSONObject;

public interface OnJsonObjReceive extends Web {
    void onJsonObjReceive(JSONObject response, String source);
}
