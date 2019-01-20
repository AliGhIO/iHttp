package io.aligh.ihttp.classes;

import java.util.HashMap;

final class CacheRAM {
    private static HashMap<String, String> caches = new HashMap<>();
    private String key = "";
    CacheRAM(String key){
        HashKey hash = new HashKey(key);
        this.key = hash.build();
    }
    void put(String value) {
        if (isExist()){
            caches.remove(key);
        }
        caches.put(key, value);
    }

    protected String get() {
        return caches.get(key);
    }

    boolean isExist(){
        return caches.containsKey(key);
    }
}
