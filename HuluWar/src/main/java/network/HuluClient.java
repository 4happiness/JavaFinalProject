package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class HuluClient {
    public static Socket socket=null;
    public void start() {
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 55533;
        // 与服务端建立连接
        try {
            socket = new Socket(host, port);
        }
        catch (ConnectException e){
            System.err.println("您尚未启动服务器！");
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(socket!=null){
            System.out.println("玩家2已准备就绪");
        }
        // 建立连接后获得输出流
        new Thread(new SendMessageThread(socket)).start();
        new Thread(new HandlerThread(socket)).start();
    }
}
