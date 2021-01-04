package ui;

import creature.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import playback.BattleLog;
import playback.LogManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.TimeUnit;

public class BatteleField{
    static public int x_block_num=11;
    static public int y_block_num=11;
    static public int block_x_len=80;
    static public int block_y_len=50;
    //二维空间战场的一些参数
    static public Creature field[][];  //生物存在的二维战场
    //生物
    static  public ArrayList<Creature> creatures;

    static ArrayList<Creature> mycreatures;
    static ArrayList<Creature> othercreatures;

    int goodchoosen = 7;
    int badchoosen = 0;
    Map map;

    static private GraphicsContext gc;

    public BatteleField(GraphicsContext gc)
    {
        this.gc = gc;
        map = new Map(gc);
        field = new Creature[x_block_num][y_block_num];
        for(int i=0;i<x_block_num;i++) {
            for(int j=0;j<y_block_num;j++){
                field[i][j] = null;
            }
        }

        creatures= new ArrayList<Creature>();
        mycreatures = new ArrayList<>();
        othercreatures = new ArrayList<>();
        if(UIController.clientID==1){
            for(int i=0;i<7;i++) {
                mycreatures.add(new Hulu(1,2+i,i+1));
            }
            mycreatures.add(new Grandpa(2, 4));
            mycreatures.add(new Pangolin(2,6));

            for(int i=0;i<7;i++){
                othercreatures.add(new LittleMonster(9, 2 + i));
            }
            othercreatures.add(new Snake(8, 4));
            othercreatures.add(new Scorpion(8, 6));
            creatures.addAll(mycreatures);
            creatures.addAll(othercreatures);
        }
        else
        {
            for(int i=0;i<7;i++){
                mycreatures.add(new LittleMonster(9, 2 + i));
            }
            mycreatures.add(new Snake(8, 4));
            mycreatures.add(new Scorpion(8, 6));

            for(int i=0;i<7;i++) {
                othercreatures.add(new Hulu(1,2+i,i+1));
            }
            othercreatures.add(new Grandpa(2, 4));
            othercreatures.add(new Pangolin(2,6));
            creatures.addAll(othercreatures);
            creatures.addAll(mycreatures);
        }


        mycreatures.get(goodchoosen).setChosenTrue();

    }

    public void refreshFiled(){
        //画图的时候就先别动了
        synchronized (creatures){
            //刷新界面
            map.drawBackgroud();
            map.drawMap();
            drawchoosen();
            drawCreatures();
            //记录

            BattleLog battleLog = new BattleLog(BatteleField.creatures);
            //System.out.println("("+battleLog.creatureLogs.get(7).x+" "+battleLog.creatureLogs.get(7).y+")："+temp);

            try {
                UIController.logManager.writeLog(battleLog);
                //LogManager.out.writeObject(BatteleField.creatures);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void drawCreatures(){
        for(Creature creature:creatures){
            if(creature.isalive())
                creature.drawMyself(gc);
            else
                creature.drawdeath(gc);
        }
    }
     public void drawchoosen(){
        int x=mycreatures.get(goodchoosen).getLoc().x;
        int y = mycreatures.get(goodchoosen).getLoc().y;
        map.drawblock(x,y);
    }
    public void shiftcontrl(){
        mycreatures.get(goodchoosen).setChosenFalse();
        goodchoosen = (goodchoosen + 1) % 9;
        while(!mycreatures.get(goodchoosen).isalive()){
            goodchoosen = (goodchoosen + 1) % 9;
        }
        mycreatures.get(goodchoosen).setChosenTrue();
        //batteleField.refreshFiled();
    }
    public void drawOver(){
        URL url;
        if(Creature.badcreaturenum==0){
            url=this.getClass().getClassLoader().getResource("picture/GoodGuyWins.png");
        }
        else{
            url=this.getClass().getClassLoader().getResource("picture/BadGuyWins.png");
        }
        Image overimage = new Image(url.toString());
        gc.drawImage(overimage,0,200,881,150);
    }
//    @Override
//    public void run() {
//        try {
//            while(GameState.state==GameState.Gamestate.INGAME)
//            {
//                refreshFiled();
//                TimeUnit.MILLISECONDS.sleep(500);
//            }
//        } catch (InterruptedException e) {
//
//        }
//    }
}

