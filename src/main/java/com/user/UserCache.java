package com.user;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Collections;

public class UserCache {
  private static Map<String, Integer> cache;
  private static Map<String, ArrayList<Integer>> transferCache;

  @SuppressWarnings("serial")
  public UserCache(final int CACHE_MAX_SIZE) {
    this.cache = Collections.synchronizedMap(new LinkedHashMap<String, Integer>(CACHE_MAX_SIZE, 0.75f, true) {
      protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
        return size() > CACHE_MAX_SIZE;
        }
    });
    this.transferCache = Collections.synchronizedMap(new LinkedHashMap<String, ArrayList<Integer>>(CACHE_MAX_SIZE, 0.75f, true) {
      protected boolean removeEldestEntry(Map.Entry<String, ArrayList<Integer>> eldest) {
        return size() > CACHE_MAX_SIZE;
      }
    });
  }

  public Map<String, Integer> getCache(){
    return cache;
  }

  public Map<String, ArrayList<Integer>> getTransferCache(){
    return transferCache;
  }
}