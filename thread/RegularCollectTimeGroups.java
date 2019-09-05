

import java.util.concurrent.ConcurrentHashMap;


public class RegularCollectTimeGroups {
    //key--displayId
    private static final ConcurrentHashMap<String, Long> REGULAR_COLLECT_TIME = new ConcurrentHashMap<>();

    //缓存下一次定点采集时间
    public static void addNextCollectTime(String displayId, Long nextCollectTime) {
        if (REGULAR_COLLECT_TIME.get(displayId) == null) {
            REGULAR_COLLECT_TIME.put(displayId, nextCollectTime);
        } else {
            REGULAR_COLLECT_TIME.remove(displayId);
            REGULAR_COLLECT_TIME.put(displayId, nextCollectTime);
        }
    }

    public static boolean contains(String displayId) {
        boolean flag = false;
        if (REGULAR_COLLECT_TIME.get(displayId) != null) {
            flag = true;
        }
        return flag;
    }

    //Long属于对象类型，其不能通过 >、=、< 来进行数值大小比较，通过Long.longValue可以进行直接比较
    public static long getNextCollectTime(String displayId) {
        return REGULAR_COLLECT_TIME.get(displayId).longValue();
    }

    public static void remove(String displayId) {
        LOGINFO.info("remove displayId:" + displayId + " from REGULAR_COLLECT_TIME");
        REGULAR_COLLECT_TIME.remove(displayId);
    }
}
