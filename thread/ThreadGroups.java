

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class ThreadGroups {
    private static final ConcurrentHashMap<String, ExecutorService> THREAD_GROUP = new ConcurrentHashMap<>();

    //初始化单线程池group，为每一个触屏构建一个单线程池
    public static void initThreadGroups() {
        if (!screenInfoBeanArrayList.isEmpty()) {
            LOGINFO.info("init ThreadGroups");
            for (ScreenInfoBean screenInfoBean : screenInfoBeanArrayList) {
                ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
                THREAD_GROUP.put(screenInfoBean.getDisplayID(), singleThreadPool);
            }
        } else {
            LOGINFO.info("screenInfoBeanArrayList is empty,exit server");
            System.exit(0);
        }
    }

    public static void addTask(String displayId, DataStoreThread dataStoreThread) {
        if (THREAD_GROUP.get(displayId) == null) {
            LOGINFO.info("the displayId:" + dataStoreThread.getSafeBean_all().getDeviceID() + " not exist in system ,don't start dataStoreThread");
        } else {
            LOGINFO.info("the displayId:" + dataStoreThread.getSafeBean_all().getDeviceID() + " exist,now start dataStoreThread");
            THREAD_GROUP.get(displayId).execute(dataStoreThread);
        }
    }

    public static void addSingleThreadPool(String displayId) {
        if (THREAD_GROUP.get(displayId) == null) {
            LOGINFO.info("displayID:" + displayId + " in THREAD_GROUP not exist,now add a new singleThreadPool into");
            ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
            THREAD_GROUP.put(displayId, singleThreadPool);
        } else {
            LOGINFO.info("displayID:" + displayId + " in THREAD_GROUP already exist,do nothing");
        }
    }

    public static void removeSingleThreadPool(String displayId) {
        LOGINFO.info("remove displayId:" + displayId + " from THREAD_GROUP");
        try {
            //关闭线程池，线程池不再接收新的任务，等到线程池里面的线程执行完毕后，关闭线程池
            THREAD_GROUP.get(displayId).shutdown();
            //移除单线程池对象
            THREAD_GROUP.remove(displayId);
        } catch (Exception e) {
            e.printStackTrace();
            LOGINFO.info(e.getLocalizedMessage());
            LOGERROR.error(e.getLocalizedMessage());
        }
    }
}
