package io.aligh.ihttp.interfaces;

import org.json.JSONObject;

public interface OnJsonObjReceive extends Web {
    void jsonObj(JSONObject response, String source);
}
