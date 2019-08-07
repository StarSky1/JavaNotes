//创建rmi服务
try {
    //创建一个远程对象
    RmiServiceImpl rmiService = new RmiServiceImpl();
    //本地主机上的远程对象注册表Registry的实例，并指定端口为8888，这一步必不可少（Java默认端口是1099），
    // 必不可缺的一步，缺少注册表创建，则无法绑定对象到远程注册表上
    LocateRegistry.createRegistry(Constants.RMI_SERVER_PORT);

    //把远程对象注册到RMI注册服务器上，并命名为RHello
    //绑定的URL标准格式为：rmi://host:port/name(其中协议名可以省略，下面两种写法都是正确的）
    //rmi://127.0.0.1:8888/RHello
    Naming.bind(Constants.RMI_SERVER_URL, rmiService);
    LOGINFO.info("launch rmi server successful.....");
} catch (Exception e) {
    e.printStackTrace();
    LOGINFO.info(e.getLocalizedMessage());
    LOGERROR.error(e.getLocalizedMessage());
}

public interface RmiService extends Remote {

    public boolean sayHello(String name) throws RemoteException;
}

public class RmiServiceImpl extends UnicastRemoteObject implements RmiService {
    public RmiServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean sayHello(String name) throws RemoteException {
        System.out.println("Hello,"+name);
        return true;
    }

}

//连接rmi服务
try {
    //通过rmi远程调用接口方法，sayHello
    RmiService rmiService = (RmiService) Naming.lookup(Constants.RMI_SERVER_URL);
    boolean flag = rmiService.sayHello("peter");
    LOGINFO.info("rmi接口调用返回值：" + flag);
} catch (Exception e) {
    e.printStackTrace();
    LOGINFO.info("rmi调用异常信息：" + e.getLocalizedMessage());
    LOGERROR.error("rmi调用异常信息：" + e.getLocalizedMessage());
}

public interface RmiService extends Remote {

    public boolean sayHello(String name) throws RemoteException;
}
