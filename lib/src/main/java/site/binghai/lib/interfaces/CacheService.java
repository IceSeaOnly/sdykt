package site.binghai.lib.interfaces;

public interface CacheService<K,V> {
    long SECOND = 1000l;
    long MINUTE = 60 * SECOND;
    long HOUR = 60 * MINUTE;
    long DAY = 24 * HOUR;
    long WEEK = 7 * DAY;
    long MONTH = 31 * DAY;

    long expire();
    V get(K key);
    void onRefresh();
    void onInit();
    void onDestory();
}
