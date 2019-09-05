


public class DataFlowStoreThread extends Thread{
    private String collectTime;
    private DataBean_Flow dataBeanFlow;

    public DataFlowStoreThread(String collectTime, GasDataBean_Flow dataBeanFlow) {
        this.collectTime = collectTime;
        this.dataBeanFlow = dataBeanFlow;
    }

    @Override
    public void run() {
        //将定时采集的数据录入数据库
        SQLServerDao sqlServerDao = new SQLServerDao();
        boolean result = sqlServerDao.insertDataBeanFlow(dataBeanFlow);
        if (result) {
            LOGINFO.info("在定点时间：" + collectTime + " 录入数据:" + dataBeanFlow.toString());
        }
    }
}
