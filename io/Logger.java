

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;


public class Logger {

    private String logName;
    private Calendar logCalendar = null;
    private PrintWriter printWriter = null;
    private final ReentrantLock lock = new ReentrantLock();
    private final String[] WEEKDAYS = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thusday", "Friday", "Saturday"};

    public Logger(String logName) {
        newLog(logName);
    }

    private void checkDate() {
        Calendar currentCalendar = Calendar.getInstance();
        /* 特别注意，这里不要用 Calendar 类的 CompareTo 方法，因为它仅仅返回 0、1、-1 分别表示无差别
         * 正差别、负差别！所以要得到两个日期之间的时间差值还是用 getTimeInMillis 方法得到换算后的
         * 毫秒值再进行比较。
         */
        long diff = currentCalendar.getTimeInMillis() - logCalendar.getTimeInMillis();
        if (diff > 24 * 60 * 60 * 1000) newLog(this.logName); // 时间间隔超过一天则创建新的日志文件
    }

    private void newLog(String logName) {
        if (logName != null) {
            this.logName = logName;
        } else {
            this.logName = "log";
        }

        if (printWriter != null) {
            printWriter.flush();
            printWriter.close();
            printWriter = null;
        }

        logCalendar = Calendar.getInstance();
        /* 将日志文件日期标志的时分秒都设置为 0，作为比较基准，当前时间与此基准相差
         * 若超过 24 小时则应产生新的日志文件。
         */
        logCalendar.set(Calendar.HOUR_OF_DAY, 0);
        logCalendar.set(Calendar.MINUTE, 0);
        logCalendar.set(Calendar.SECOND, 0);

        //创建日志文件夹 logs/%d-%02d-%02d/
        String fileName=String.format(PathUtil.getWebInfPath()+"logs/%d-%02d-%02d",
                logCalendar.get(Calendar.YEAR), logCalendar.get(Calendar.MONTH) + 1,
                logCalendar.get(Calendar.DAY_OF_MONTH));
        File file=new File(fileName);
        if(!file.exists()){
            file.mkdirs();
        }
        System.out.println("log文件路径==> "+file.getAbsolutePath());
        System.out.println("webinf路径："+ PathUtil.getWebInfPath());
        // 设置日志文件全路径名
        String logFileName = String.format(fileName+"/%s_%d-%02d-%02d.log", this.logName, logCalendar.get(Calendar.YEAR), logCalendar.get(Calendar.MONTH) + 1, logCalendar.get(Calendar.DAY_OF_MONTH));
        try {
            // 创建自动刷新的格式化打印对象
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFileName, true)), true);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    // 可供外部调用的同步方法
    public void record(String logInfo) {
        System.out.printf("%s\n", logInfo);
        lock.lock();

        try {
            checkDate();
            Calendar currentCalendar = Calendar.getInstance();
            printWriter.printf("[%d-%02d-%02d %s %02d:%02d:%02d] %s\n", currentCalendar.get(Calendar.YEAR),
                    currentCalendar.get(Calendar.MONTH) + 1, currentCalendar.get(Calendar.DAY_OF_MONTH),
                    WEEKDAYS[currentCalendar.get(Calendar.DAY_OF_WEEK) - 1], currentCalendar.get(Calendar.HOUR_OF_DAY),
                    currentCalendar.get(Calendar.MINUTE), currentCalendar.get(Calendar.SECOND), logInfo);
        } finally {
            lock.unlock();
        }
    }

    // 可供外部调用的同步方法
    public void newLine() {
        lock.lock();

        try {
            checkDate();
            printWriter.println();
        } finally {
            lock.unlock();
        }
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    // 可供外部调用的同步方法

    public void close() {
        lock.lock();

        try {
            printWriter.flush();
            printWriter.close();
        } finally {
            printWriter = null;
            lock.unlock();
        }
    }

    public boolean isOpen() {
        return (printWriter != null);
    }
}
