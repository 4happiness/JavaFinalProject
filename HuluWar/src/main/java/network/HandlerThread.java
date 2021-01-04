package network;

import creature.Creature;
import ui.BatteleField;
import ui.GameState;
import ui.UIController;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class HandlerThread implements Runnable {
    Socket socket;
    ObjectInputStream in;
    HandlerThread(Socket socket){this.socket=socket;}
    @Override
    public void run() {
        try {
            in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));

            NetMessage netMessage;
            while(GameState.state== GameState.Gamestate.NOTSTART|| GameState.state==GameState.Gamestate.REVIEW){
                TimeUnit.MILLISECONDS.sleep(100);
            }
            while((netMessage= (NetMessage) in.readObject())!=null&& GameState.state==GameState.Gamestate.INGAME){
                ArrayList<CreatureMessage> creatureMessages = netMessage.creatureMessages;
                synchronized (BatteleField.creatures){
                    for(int i=0;i<18;i++){
                        int x = creatureMessages.get(i).x;
                        int y = creatureMessages.get(i).y;
                        int living = creatureMessages.get(i).living;
                        if(x==-1){
                            if(BatteleField.creatures.get(i).getLivevalue()!=living){
                                BatteleField.creatures.get(i).setLivevalue(living);
                                if(living<=0){
                                    if(i<9){
                                        Creature.goodcreaturenum--;
                                    }
                                    else{
                                        Creature.badcreaturenum--;
                                    }
                                    if(Creature.goodcreaturenum==0||Creature.badcreaturenum==0){
                                        synchronized (UIController.gamestate) {
                                            UIController.gamestate.notifyAll();    //唤醒监听的线程
                                        }
                                    }
                                }
                            }
                        }
                        else{
                            int xbefore = BatteleField.creatures.get(i).getLoc().x;
                            int ybefore = BatteleField.creatures.get(i).getLoc().y;
                            if(xbefore!=x||ybefore!=y){
                                synchronized (BatteleField.field){
                                    BatteleField.field[xbefore][ybefore] = null;
                                    BatteleField.creatures.get(i).setLoc(x,y);
                                    BatteleField.field[x][y] = BatteleField.creatures.get(i);
                                }
                            }

                        }
                    }
                }
            }
        }
        catch(EOFException| SocketException e){
        }
        catch (IOException e){
        }
        catch (ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
