package network;

import org.junit.Test;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class HuluClientTest {

    @Test(expected = ConnectException.class)  //测试服务器未打开时连不上
    public void start() throws IOException {
        String host = "127.0.0.1";
        int port = 55533;
        Socket socket = new Socket(host, port);
    }
}