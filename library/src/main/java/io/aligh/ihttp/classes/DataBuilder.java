package io.aligh.ihttp.classes;

import android.util.Log;

import java.net.URLEncoder;
import java.util.List;

import io.aligh.ihttp.models.Post;

final class DataBuilder {
    private final String TAG = this.getClass().getName();

    public String buildPostData(List<Post> bodies) {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (int i = 0; i < bodies.size(); i++) {
            Post post = bodies.get(i);
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            try {
                result.append(URLEncoder.encode(post.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(post.getValue(), "UTF-8"));
            }catch (Exception e){
                Log.e(TAG, "buildPostData: " + e );
            }

        }

        Log.i(TAG, "buildPostData: " + result);
        return result.toString();
    }
}
