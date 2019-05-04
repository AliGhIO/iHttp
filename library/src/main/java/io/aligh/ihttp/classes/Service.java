package io.aligh.ihttp.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import io.aligh.ihttp.enums.Logs;
import io.aligh.ihttp.enums.Methods;
import io.aligh.ihttp.models.Get;
import io.aligh.ihttp.models.Header;
import io.aligh.ihttp.models.Post;

final class Service {

    protected JSONObject jsonObject;

//    private final String TAG = this.getClass().getName();

    protected int retry = 1;
    protected int tried = 0;

    protected List<Post> posts;
    protected List<Header> headers;
    protected List<Get> gets;

    protected boolean cacheRAM = false;
    protected boolean cacheSP = false;
    protected boolean cacheSQLite = false;
    protected boolean modeOffline = false;
    protected boolean cacheHTTP = true;
    protected boolean encryptCache = false;

    protected String cacheSQLiteKey = "";
    protected String cacheSPKey = "";

    protected String url = "";
    protected String method = "POST";
    protected String token = null;

    protected Context context;
    protected String dataPOST = "";

    protected int connectTimeOut = 5000;
    protected int readTimeOut = 5000;

    private iHttp iHttp;
    private Events Events;

    private long timeSTART = 0;
    private long timeELAPSED = 0;
    private long timeRESPONSE = 0;

    private CacheRAM classCacheRAM;
    private CacheSP classCacheSP;
    private CacheSQLite classCacheSQLite;

    private String hash_key = "";

    protected Proxy proxy;

    protected boolean is_retryTime_set = false;
    protected boolean is_url_set = false;
    protected boolean is_method_set = false;
    protected boolean is_token_set = false;
    protected boolean is_connectTimeOut_set = false;
    protected boolean is_readTimeOut_set = false;
    protected boolean is_proxy_set = false;
    protected boolean is_paramsPost_set = false;
    protected boolean is_paramsHeader_set = false;
    protected boolean is_paramsGet_set = false;
    protected boolean is_cacheRAM_set = false;
    protected boolean is_jsonObj = false;
    protected boolean is_cacheSP_set = false;
    protected boolean is_cacheSQLite_set = false;
    protected boolean is_modeOffline_set = false;
    protected boolean is_cacheHTTP_set = false;
    protected boolean is_cacheEncrypt_set = false;


    private String lastResponse = "";


    Service(Context context, iHttp iHttp, Events iHttpEvents) {
        this.jsonObject = new JSONObject();
        this.posts = new ArrayList<>();
        this.headers = new ArrayList<>();
        this.gets = new ArrayList<>();
        this.Events = iHttpEvents;
        this.iHttp = iHttp;
        this.context = context;
        loadDefaults();
    }

    private void loadDefaults() {
        Config.init();
        if (!is_retryTime_set) {
            this.retry = Config.retry;
        }

        if (!is_url_set) {
            this.url = Config.url;
        }
        if (!is_method_set) {
            this.method = Config.method;
        }
        if (!is_token_set) {
            this.token = Config.token;
        }
        if (!is_connectTimeOut_set) {
            this.connectTimeOut = Config.connectionTimeOut;
        }
        if (!is_readTimeOut_set) {
            this.readTimeOut = Config.readTimeOut;
        }
        if (!is_proxy_set) {
            this.proxy = Config.proxy;
        }
        if (!is_paramsPost_set) {
            this.posts = Config.posts;
        }
        if (!is_paramsHeader_set) {
            this.headers = Config.headers;
        }
        if (!is_paramsGet_set) {
            this.gets = Config.gets;
        }
        if (!is_cacheRAM_set) {
            this.cacheRAM = Config.cacheRAM;
        }
        if (!is_cacheSP_set) {
            this.cacheSP = Config.cacheSP;
        }
        if (!is_cacheSQLite_set) {
            this.cacheSQLite = Config.cacheSQLite;
        }
        if (!is_modeOffline_set) {
            this.modeOffline = Config.modeOffline;
        }
        if (!is_cacheHTTP_set) {
            this.cacheHTTP = Config.cacheHTTP;
        }
        if (!is_cacheEncrypt_set) {
            this.encryptCache = Config.cacheEncryption;
        }
    }

    //3
    protected void begin() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                configs();
                return null;
            }
        };
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    //4
    private void configs() {
        dataPOST = new DataBuilder().buildPostData(posts);

        hash_key = new HashKey(url + dataPOST).build();

        if (cacheRAM)
            classCacheRAM = new CacheRAM(hash_key);

        if (cacheSP)
            classCacheSP = new CacheSP(context, cacheSPKey.equals("") ? hash_key : cacheSPKey);

        if (cacheSQLite)
            classCacheSQLite = new CacheSQLite(context, cacheSQLiteKey.equals("") ? hash_key : cacheSQLiteKey);

        setup();
    }


    //5
    private void setup() {
        timeSTART = System.currentTimeMillis();

        String source = "";
        String data = "";

        if (cacheRAM && classCacheRAM.isExist()) {
            source = io.aligh.ihttp.classes.iHttp.RAM;
            data = classCacheRAM.get();
        }

        if (cacheSP && classCacheSP.contains()) {
            source = io.aligh.ihttp.classes.iHttp.SP;
            data = classCacheSP.get();
            if (encryptCache)
                data = new Encrypt().decode(data);
        }

        if (cacheSQLite)
            classCacheSQLite.get(new CacheSQLite.DataReceived() {
                @Override
                public void data(String string) {
                    if (encryptCache)
                        string = new Encrypt().decode(string);
                    onDataReceive(string, io.aligh.ihttp.classes.iHttp.SQLite, 0);

                }
            });

        if (!data.equals("")) {
            onDataReceive(data, source, 0);
        }


        if (!modeOffline)
            execute();
        else
            Events.onLog(Logs.WARN, "Offline Mode => Read from Cache");
    }


    //6
    protected void execute() {
        if (is_jsonObj) {
            Post post = new Post();
            post.setKey("api");
            post.setValue(jsonObject.toString());
            this.posts.add(post);
        }
        openHttpURLConnection();
    }


    //4
    private void openHttpURLConnection() {
        HttpURLConnection httpURLConnection = null;
        try {
            if (gets.size() != 0)
                this.url = buildURI(this.url, gets);

            URL url = new URL(this.url);

            if (proxy != null) {
                httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
                onInfo("Proxy connected to the HttpURLConnection");
            } else
                httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setReadTimeout(readTimeOut);
            httpURLConnection.setConnectTimeout(connectTimeOut);

            httpURLConnection.setRequestMethod(method);

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(!method.equals(Methods.GET.getValue()));
            httpURLConnection.setUseCaches(cacheHTTP);

            httpURLConnection.connect();

            setHeaderData(httpURLConnection);

            writeOnHttpConnection(httpURLConnection);

            String data = readHttpConnection(httpURLConnection);

            onDataReceive(data, io.aligh.ihttp.classes.iHttp.ONLINE, httpURLConnection.getResponseCode());

            httpURLConnection.disconnect();
            onInfo("disconnect httpURLConnection");

        } catch (SocketTimeoutException e) {
            Events.OnTimeOut();
            onError(e.getMessage());
            onRetry();
        } catch (ProtocolException e) {
            onError(e.getMessage());
        } catch (Exception e) {
            onError(e.getMessage());
        } finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
        }
    }

    private String buildURI(String url, List<Get> gets) {
        Uri.Builder builder = Uri.parse(url).buildUpon();
        for (int i = 0; i < gets.size(); i++)
            builder.appendQueryParameter(gets.get(i).getKey(), gets.get(i).getValue());
        return builder.toString();
    }

    private void setHeaderData(HttpURLConnection httpURLConnection) {
        /*httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");*/
        if (token != null)
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);

        for (int i = 0; i < headers.size(); i++)
            httpURLConnection.setRequestProperty(headers.get(i).getKey(), headers.get(i).getValue());
    }

    //6
    private void writeOnHttpConnection(HttpURLConnection httpURLConnection) {
        try {
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.write(dataPOST);
            writer.flush();
            writer.close();
            outputStream.close();
        } catch (IOException e) {
            onError(e.getMessage());
        } catch (Exception e) {
            onError(e.getMessage());
        }
    }

    private void onError(String error) {
        Events.onLog(Logs.ERROR, error);
    }

    private void onInfo(String error) {
        Events.onLog(Logs.INFO, error);
    }

    //6.5
    private void onRetry() {
        tried++;
        if (tried < retry) {
            Events.onRetry(tried, retry);
            onInfo("retry");
            execute();
        }
    }

    //7
    private String readHttpConnection(HttpURLConnection httpURLConnection) {
        final StringBuilder stringBuilder = new StringBuilder();

        int code = 0;
        try {
            code = httpURLConnection.getResponseCode();
            onInfo("onResponse code = " + code + " : " + new HttpCodes().httpCode(code));
            if (code == HttpsURLConnection.HTTP_OK) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line);

                bufferedReader.close();
            } else {
                Events.OnFailed(code);
                onError("httpURLConnection is not 200");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            onError(e.getMessage());
            return "";
        }
    }

    //9 //3
    private void onDataReceive(String response, String source, int code) {

        if (lastResponse.length() != response.length()) {

            onInfo("Response From => " + source);
            lastResponse = response;

            //done();

            Events.onResponseReceive(response, source, done(), code);

            Events.onJsonArrayReceive(response, source);
            Events.onJsonObjReceive(response, source);

            Events.OnXmlReceive(response, source);

            if ((cacheSQLite || cacheSP) && (encryptCache)) {
                Encrypt encryption = new Encrypt();
                String encrypted = encryption.encode(response);
                cache(encrypted, source);
            } else
                cache(response, source);

        } else
            Events.onLog(Logs.WARN, "onResponse not changed from = " + source);
    }

    private void cache(String data, String where) {
        if (where.equals(io.aligh.ihttp.classes.iHttp.ONLINE)) {
            if (cacheSP)
                classCacheSP.put(data);

            if (cacheRAM)
                classCacheRAM.put(data);

            if (cacheSQLite)
                classCacheSQLite.put(data);
        }
    }

    private long done() {
        timeELAPSED = System.currentTimeMillis();
        timeRESPONSE = timeELAPSED - timeSTART;
        Events.onResponseTime(timeRESPONSE);
        return timeRESPONSE;
    }
}
