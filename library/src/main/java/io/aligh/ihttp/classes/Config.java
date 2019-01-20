package io.aligh.ihttp.classes;

import android.view.Display;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import io.aligh.ihttp.enums.Methods;
import io.aligh.ihttp.models.Get;
import io.aligh.ihttp.models.Header;
import io.aligh.ihttp.models.Post;

public class Config {
    public Config() {
        if (posts == null)
            posts = new ArrayList<>();
        if (headers == null)
            headers = new ArrayList<>();
        if (gets == null)
            gets = new ArrayList<>();
    }


    protected static int retry = 1;
    protected static String url = "";
    protected static String method = "POST";
    protected static String token = null;
    protected static int connectionTimeOut = 5000;
    protected static int readTimeOut = 5000;
    protected static Proxy proxy;
    protected static List<Post> posts;
    protected static List<Header> headers;
    protected static List<Get> gets;
    protected static boolean cacheRAM = false;
    protected static boolean cacheSP = false;
    protected static boolean cacheSQLite = false;
    protected static boolean cacheHTTP = true;
    protected static boolean modeOffline = false;
    protected static boolean cacheEncryption = false;


    public int getDefaultRetry() {
        return retry;
    }

    public String getDefaultUrl() {
        return url;
    }

    public String getDefaultMethod() {
        return method;
    }

    public String getDefaultToken() {
        return token;
    }

    public int getDefaultConnectTimeOut() {
        return connectionTimeOut;
    }

    public int getDefaultReadTimeOut() {
        return readTimeOut;
    }

    public Proxy getDefaultProxy() {
        return proxy;
    }

    public List<Post> getDefaultPosts() {
        return posts;
    }

    public List<Header> getDefaultHeaders() {
        return headers;
    }

    public List<Get> getDefaultGets() {
        return gets;
    }

    public boolean getDefaultCacheRAM() {
        return cacheRAM;
    }

    public boolean getDefaultCacheSP() {
        return cacheSP;
    }

    public boolean getDefaultSqlCache() {
        return cacheSQLite;
    }

    public boolean getDefaultModeOffline() {
        return modeOffline;
    }

    public boolean getDefaultCacheHTTP() {
        return cacheHTTP;
    }

    public boolean getDefaultCacheEncrption() {
        return cacheEncryption;
    }


    public Config setDefaultBearerToken(String token) {
        Config.token = token;
        return this;
    }


    public Config setDefaultPosts(List<Post> posts) {
        Config.posts = posts;
        return this;
    }

    public Config setDefaultHeaders(List<Header> headers) {
        Config.headers = headers;
        return this;
    }

    public Config setDefaultGets(List<Get> gets) {
        Config.gets = gets;
        return this;
    }

    public Config setDefaultCacheHTTP(boolean CacheHTTP) {
        Config.cacheHTTP = CacheHTTP;
        return this;
    }

    public Config setDefaultCacheEncryption(boolean CacheEncryption) {
        Config.cacheEncryption = CacheEncryption;
        return this;
    }


    public Config setDefaultUrl(String url) {
        Config.url = url;
        return this;
    }

    public Config setDefaultUrl(Object url) {
        Config.url = url.toString();
        return this;
    }

    public Config setDefaultMethod(Methods method) {
        Config.method = method.getValue();
        return this;
    }


    public Config setDefaultRetryTime(int RetryTime) {
        Config.retry = RetryTime;
        return this;
    }

    public Config setDefaultConnectionTimeOut(int ConnectionTimeOut) {
        Config.connectionTimeOut = ConnectionTimeOut;
        return this;
    }

    public Config setDefaultReadTimeOut(int ReadTimeOut) {
        Config.readTimeOut = ReadTimeOut;
        return this;
    }

    public Config setDefaultCacheRAM(boolean CacheRAM) {
        Config.cacheRAM = CacheRAM;
        return this;
    }

    public Config setDefaultCacheSP(boolean CacheSP) {
        Config.cacheSP = CacheSP;
        return this;
    }

    public Config setDefaultCacheSQLite(boolean CacheSQLite) {
        Config.cacheSQLite = CacheSQLite;
        return this;
    }

    public Config setDefaultModeOffline(boolean ModeOffline) {
        Config.modeOffline = ModeOffline;
        return this;
    }


    public Config setDefaultProxy(String host, int port) {
        Config.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        return this;
    }

    public Config setDefaultProxy(String host, int port, final String username, final String password) {
        Config.proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
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

    public static Config init() {
        return new Config();
    }
}
