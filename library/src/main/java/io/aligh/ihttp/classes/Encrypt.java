package io.aligh.ihttp.classes;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;

final class Encrypt {

    String encode(String text) {
        try {
            byte[] data = new byte[0];
            data = text.getBytes("UTF-8");
            text = Base64.encodeToString(data, Base64.DEFAULT);
            return text;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("iHttp", "encode: " + e);
            return "";
        }
    }

    String decode(String text) {
        try {
            byte[] data = Base64.decode(text, Base64.DEFAULT);
            text = new String(data, "UTF-8");
            return text;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e("iHttp", "decode: " + e);
            return "";
        }
    }
}
