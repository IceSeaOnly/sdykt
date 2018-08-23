package site.binghai.lib.interfaces;

/**
 * 标记可能需要保存在Session中的对象，如User、Admin
 * 以便使用 getSessionPersistent 获取session的该对象
 * */
public interface SessionPersistent {
    String sessionTag();
}
