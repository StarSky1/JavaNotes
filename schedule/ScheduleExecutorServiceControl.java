package com.schedule;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 新建定时线程，定时检测，符合条件则终止该线程
 */
public class ScheduleExecutorServiceControl {
    private static org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(ScheduleExecutorServiceControl.class);
    private ScheduledExecutorService scheduExec;
    private static long ONEDAY = 24 * 60 * 60 * 1000;
    private static long testPerTime = 10 * 1000;
    private int count = 0;

    public ScheduleExecutorServiceControl() {
        this.scheduExec = Executors.newScheduledThreadPool(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            scheduExec.shutdownNow();
            log.info("finished!");
        }));
    }


    /**
     * 每天执行一次
     * 每天定时安排任务进行执行
     */
    private void executeAt6PerDay() {
        scheduExec.scheduleAtFixedRate(() -> {
            log.info("execute at 6");
        }, getDelay("6:30:00"), testPerTime, TimeUnit.MILLISECONDS);
    }

    private void executeAt7PerDay() {
        scheduExec.scheduleAtFixedRate(() -> {
            log.info("execute at 7");
            try {

            }catch (Exception e){
                e.printStackTrace();
            }

        },  getDelay("7:00:00"), ONEDAY, TimeUnit.MILLISECONDS);
    }

    private long getDelay(String time){
        long initDelay  = DateUtil.getTimeMillis(time) - System.currentTimeMillis();
        return initDelay > 0 ? initDelay : ONEDAY + initDelay;
    }

    public void func(){
        //定时任务
//        executeAt6PerDay();
        executeAt7PerDay();

    }

    public static void main(String[] args)throws Exception{
        System.out.println("begin.");
        ScheduleExecutorServiceControl test = new ScheduleExecutorServiceControl();
        test.func();
        System.out.println("over.");
    }
}
