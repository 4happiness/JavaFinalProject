package network;

import ui.BatteleField;
import ui.GameState;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

public class SendMessageThread implements Runnable{
    Socket socket;
    ObjectOutputStream out;
    SendMessageThread(Socket socket){this.socket=socket;}
    public void sendLastMessage(){
        try {
            out.writeObject(new NetMessage(BatteleField.creatures));
        } catch (IOException e) {
            e.printStackTrace();
      }
//        finally {
//            out.close();
//        }

    }
    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            while(GameState.state== GameState.Gamestate.NOTSTART|| GameState.state==GameState.Gamestate.REVIEW){
                TimeUnit.MILLISECONDS.sleep(100);
            }
            while(GameState.state== GameState.Gamestate.INGAME){
                out.writeObject(new NetMessage(BatteleField.creatures));
                TimeUnit.MILLISECONDS.sleep(10);
            }
            out.writeObject(new NetMessage(BatteleField.creatures));
        }
        catch (SocketException e){
            //说明
        }
        catch (IOException e){
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
