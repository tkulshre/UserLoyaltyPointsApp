package com.user;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;

public class UserCache {
  private static LinkedHashMap<String, Integer> cache;
  private static LinkedHashMap<String, ArrayList<Integer>> transferCache;

  @SuppressWarnings("serial")
  public UserCache(final int CACHE_MAX_SIZE) {
    this.cache = new LinkedHashMap<String, Integer>(CACHE_MAX_SIZE, 0.75f, true) {
      protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
          return size() > CACHE_MAX_SIZE;
        }
    };

    this.transferCache = new LinkedHashMap<String, ArrayList<Integer>>(CACHE_MAX_SIZE, 0.75f, true) {
      protected boolean removeEldestEntry(Map.Entry<String, ArrayList<Integer>> eldest) {
        return size() > CACHE_MAX_SIZE;
      }
    };
  }

  public LinkedHashMap<String, Integer> getCache(){
    return cache;
  }

  public LinkedHashMap<String, ArrayList<Integer>> getTransferCache(){
    return transferCache;
  }
}