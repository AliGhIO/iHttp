package io.aligh.ihttp.classes;

import android.content.Context;

import org.json.JSONException;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;

import io.aligh.ihttp.enums.Methods;
import io.aligh.ihttp.interfaces.OnFail;
import io.aligh.ihttp.interfaces.OnJsonArrayReceive;
import io.aligh.ihttp.interfaces.OnJsonObjReceive;
import io.aligh.ihttp.interfaces.OnLog;
import io.aligh.ihttp.interfaces.OnResponse;
import io.aligh.ihttp.interfaces.OnRetry;
import io.aligh.ihttp.interfaces.OnTimeOut;
import io.aligh.ihttp.interfaces.OnXmlReceive;
import io.aligh.ihttp.interfaces.ResponseTime;
import io.aligh.ihttp.models.Get;
import io.aligh.ihttp.models.Header;
import io.aligh.ihttp.models.Post;

public class iHttp {

    static String ONLINE = "online";
    static String OFFLINE = "offline";
    static String SQLite = "sqlite";
    static String RAM = "ram";
    static String SP = "sp";

    private Service service;
    private Events events;

    public iHttp(Context context) {
        events = new Events(context);
        service = new Service(context, this, events);
    }

//    public static iHttp with(Context context) {
//    }


    public iHttp url(String url) {
        service.url = url;
        service.is_url_set = true;
        return this;
    }


    public iHttp addJsonPost(Object key, Object value) {
        try {
            service.is_jsonObj = true;
            service.jsonObject.put(key.toString(), value.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public iHttp onResponse(OnResponse onResponse) {
        events.onResponse = (onResponse);
        return this;
    }


    public iHttp onJsonArrayReceive(OnJsonArrayReceive onJsonArrayReceive) {
        events.onJsonArrayReceive = onJsonArrayReceive;
        return this;
    }


    public iHttp onJsonObjectReceive(OnJsonObjReceive onJsonObjReceive) {
        events.onJsonObjReceive = onJsonObjReceive;
        return this;
    }


    public iHttp connect() {
        service.begin();
        return this;
    }

    public iHttp addPost(Object key, Object value) {
        Post post = new Post();
        post.setKey(String.valueOf(key));
        post.setValue(String.valueOf(value));
        service.posts.add(post);
        service.is_paramsPost_set = true;
        return this;
    }

    public iHttp addPostList(List<Post> bodies) {
        service.posts.addAll(bodies);
        service.is_paramsPost_set = true;
        return this;
    }

    public iHttp setPostList(List<Post> bodies) {
        service.posts = bodies;
        service.is_paramsPost_set = true;
        return this;
    }

    public iHttp clearPosts() {
        service.posts.clear();
        return this;
    }

    public iHttp clearGets() {
        service.gets.clear();
        return this;
    }

    public iHttp clearHeaders() {
        service.gets.clear();
        return this;
    }

    public iHttp setHttpCache(boolean httpCache) {
        service.cacheHTTP = httpCache;
        service.is_cacheHTTP_set = true;
        return this;
    }

    public String getUrl() {
        return service.url;
    }

    public iHttp setEncryptCache(boolean encryptCache) {
        service.encryptCache = encryptCache;
        service.is_cacheEncrypt_set = true;

        return this;
    }

    public iHttp setModeOffline(boolean offline) {
        service.modeOffline = offline;
        service.is_modeOffline_set = true;
        return this;
    }

    public iHttp setRAMCache(boolean ramCache) {
        service.is_cacheRAM_set = true;
        service.cacheRAM = ramCache;
        return this;
    }

    public iHttp setSPCache(boolean spCache) {
        service.is_cacheSP_set = true;
        service.cacheSP = spCache;
        return this;
    }

    public iHttp setSPCache(boolean spCache, String key) {
        service.is_cacheSP_set = true;
        service.cacheSP = spCache;
        service.cacheSPKey = key;
        return this;
    }

    public iHttp setSQLiteCache(boolean sqliteCache) {
        service.is_cacheSQLite_set = true;
        service.cacheSQLite = sqliteCache;
        return this;
    }

    public iHttp setSQLiteCache(boolean sqliteCache, String key) {
        service.is_cacheSQLite_set = true;
        service.cacheSQLite = sqliteCache;
        service.cacheSQLiteKey = key;
        return this;
    }

    public iHttp setReadTimeOut(int readTimeOut) {
        service.readTimeOut = readTimeOut;
        service.is_readTimeOut_set = true;
        return this;
    }

    public iHttp setConnectionTimeOut(int connectimeOut) {
        service.connectTimeOut = connectimeOut;
        service.is_connectTimeOut_set = true;
        return this;
    }

    public int getReadTimeOut() {
        return service.readTimeOut;
    }

    public int getConnectTimeOut() {
        return service.connectTimeOut;
    }

    public iHttp setMethod(Methods method) {
        service.method = method.getValue();
        service.is_method_set = true;
        return this;
    }

    public String getMethod() {
        return service.method;
    }

    public iHttp setOnLogListener(OnLog onLog) {
        events.onLog = onLog;
        return this;
    }

    public iHttp getResponseTime(ResponseTime getResponseTime) {
        events.getResponseTime = (getResponseTime);
        return this;
    }

    protected Service getService() {
        return service;
    }

    protected Events getEvents() {
        return this.events;
    }

    String getPost(String key) {
        String value = "";
        for (int i = 0; i < service.posts.size(); i++)
            if (service.posts.get(i).getKey().equals(key)) {
                value = service.posts.get(i).getKey();
                break;
            }
        return value;
    }

    public iHttp setRetryTimes(int retry) {
        service.retry = retry;
        service.is_retryTime_set = true;
        return this;
    }

    public int getRetryRemaining() {
        return service.retry - service.tried;
    }

    public boolean getRAMCache() {
        return service.cacheRAM;
    }

    public boolean getSPCache() {
        return service.cacheSP;
    }

    public boolean getSQLiteCache() {
        return service.cacheSQLite;
    }

    public boolean getModeOffline() {
        return service.modeOffline;
    }

    public boolean getHttpCache() {
        return service.cacheHTTP;
    }

    public boolean getEncryptCache() {
        return service.encryptCache;
    }

    public List<Post> getPosts() {
        return service.posts;
    }

    public Post getPost(int index) {
        return service.posts.get(index);
    }

    public iHttp setProxy(String host, int port) {
        service.is_proxy_set = true;
        service.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        return this;
    }

    public iHttp setProxy(String host, int port, final String username, final String password) {
        service.is_proxy_set = true;
        service.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        System.setProperty("http.proxyHost", host);
        System.setProperty("http.proxyPort", String.valueOf(port));
        System.setProperty("http.proxyUser", username);
        System.setProperty("http.proxyPassword", password);

        Authenticator.setDefault(
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                }
        );
        return this;
    }

    public Proxy getProxy() {
        return service.proxy;
    }

    public iHttp setOnRetry(OnRetry onRetry) {
        events.onRetry = onRetry;
        return this;
    }

    public iHttp setOnFailed(OnFail onFailed) {
        events.onFailed = onFailed;
        return this;
    }

    public iHttp setOnTimeOut(OnTimeOut onTimeOut) {
        events.onTimeOut = onTimeOut;
        return this;
    }

    public iHttp setOnXmlRecevie(OnXmlReceive onXmlRecevie) {
        events.onXmlReceive = onXmlRecevie;
        return this;
    }

    public iHttp addHeader(Object key, Object value) {
        Header header = new Header();
        header.setKey(String.valueOf(key));
        service.is_paramsHeader_set = true;
        header.setValue(String.valueOf(value));
        service.headers.add(header);
        return this;
    }

    public iHttp addHeaderList(List<Header> headers) {
        service.headers.addAll(headers);
        service.is_paramsHeader_set = true;
        return this;
    }

    public iHttp setHeaderList(List<Header> headers) {
        service.headers = headers;
        service.is_paramsHeader_set = true;
        return this;
    }

    public iHttp addGet(Object key, Object value) {
        Get get = new Get();
        service.is_paramsGet_set = true;
        get.setKey(String.valueOf(key));
        get.setValue(String.valueOf(value));
        service.gets.add(get);
        return this;
    }

    public iHttp addGetList(List<Get> gets) {
        service.is_paramsGet_set = true;
        service.gets.addAll(gets);
        return this;
    }

    public iHttp setGetList(List<Get> gets) {
        service.is_paramsGet_set = true;
        service.gets = gets;
        return this;
    }

    public List<Header> getHeaders() {
        return service.headers;
    }

    public iHttp setBearerToken(String bearerToken) {
        service.token = bearerToken;
        service.is_token_set = true;
        return this;
    }

    public String getBearerToken() {
        return service.token;
    }
}
