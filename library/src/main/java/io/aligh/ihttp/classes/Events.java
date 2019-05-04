package io.aligh.ihttp.classes;

import android.content.Context;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.aligh.ihttp.enums.Logs;
import io.aligh.ihttp.interfaces.OnFail;
import io.aligh.ihttp.interfaces.OnJsonArrayReceive;
import io.aligh.ihttp.interfaces.OnJsonObjReceive;
import io.aligh.ihttp.interfaces.OnLog;
import io.aligh.ihttp.interfaces.OnResponse;
import io.aligh.ihttp.interfaces.OnRetry;
import io.aligh.ihttp.interfaces.OnTimeOut;
import io.aligh.ihttp.interfaces.OnXmlReceive;
import io.aligh.ihttp.interfaces.ResponseTime;

final class Events {
    private static final String TAG = "iHttpEvents";
    private Handler handler;
    protected OnResponse onResponse;
    protected ResponseTime getResponseTime;
    protected OnLog onLog;
    protected OnJsonArrayReceive onJsonArrayReceive;
    protected OnJsonObjReceive onJsonObjReceive;
    protected OnRetry onRetry;
    protected OnTimeOut onTimeOut;
    protected OnFail onFailed;
    protected OnXmlReceive onXmlReceive;


    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    Events(Context context) {
        handler = new Handler(context.getMainLooper());
    }

    void onResponseReceive(final String res, final String where, final long time, final int code) {
        onLog(Logs.INFO, "\treturn onResponse from " + where);
        if (onResponse != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onResponse.onResponse(res, where, time, code);
                }
            });
        }
    }

    void onResponseTime(final long time) {
        onLog(Logs.INFO, "\tonResponse time = " + time);
        if (getResponseTime != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getResponseTime.time(time);
                }
            });
        }
    }

    void onLog(final Logs type, final String log) {
        if (onLog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onLog.onLog(type.getLog(), log);
                }
            });
        }
    }

    void OnXmlReceive(String xml, final String where){
        if (onXmlReceive!=null){
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                final DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                final InputSource inputSource = new InputSource(new StringReader(xml));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            onXmlReceive.onXmlReceive(documentBuilder.parse(inputSource).toString(),where);
                        } catch (SAXException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }catch (Exception e){
                onLog(Logs.ERROR,e.getMessage());
            }

        }
    }

    void OnFailed(final int code){
        if (onFailed!=null){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onFailed.onFail(code);
                }
            });
        }
    }
    void OnTimeOut() {
        if (onTimeOut != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onTimeOut.onTimeOut();
                }
            });
        }
    }

    void onRetry(final int tried_times, final int retry_times) {
        if (onRetry != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRetry.onRetry(tried_times, retry_times);
                }
            });
        }
    }

    void onJsonArrayReceive(final String jsonArray, final String where) {
        if (onJsonArrayReceive != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONArray ja = new JSONArray(jsonArray);
                        onJsonArrayReceive.onJsonArrayReceive(ja, where);
                    } catch (Exception e) {
                        onLog(Logs.ERROR, "failed to parse JsonArray , onResponse from = " + where);
                    }
                }
            });
        }
    }

    void onJsonObjReceive(final String json, final String where) {
        if (onJsonObjReceive != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject ja = new JSONObject(json);
                        onJsonObjReceive.onJsonObjReceive(ja, where);
                    } catch (Exception e) {
                        onLog(Logs.ERROR, "failed to parse jsonObject , onResponse from = " + where);
                    }
                }
            });
        }
    }
}
