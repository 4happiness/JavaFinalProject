package network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HuluServer {
    static public final String ip = "127.0.0.1";
    static ServerSocket server;
    public static Socket socket;
    public static void start(){
        // 监听指定的端口
        int port = 55533;
        try{
            server = new ServerSocket(port);

            // server将一直等待连接的到来
            System.out.println("玩家1已准备就绪");
            socket = server.accept();
            // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(new SendMessageThread(socket)).start();
        new Thread(new HandlerThread(socket)).start();
    }
}
