
/**
 * Created by Administrator on 2017/4/17 0017.
 */
public class WebsocketData {
    private int protocol;  //请求数据的协议号
    private String jsonString;  //请求返回的数据

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
