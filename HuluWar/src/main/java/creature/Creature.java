package creature;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ui.BatteleField;
import ui.GameState;
import ui.UIController;

import java.beans.Transient;
import java.io.*;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

abstract public  class Creature implements Serializable,Runnable{

    //阵营人数
    public static int goodcreaturenum = 0;
    public static int badcreaturenum = 0;
    int livevalue;
    public int maxlivevalue;
    transient int speed;
    transient int attackvalue;
    transient double defence;
    Loc loc;  //所处位置
    boolean chosen=false;
    public enum Camp{GOOD,BAD}
    transient Camp camp;
    public int campid;    //为了回放
    public int rank;
    static Random random=new Random();
    transient Image gooddeathimage;
    transient Image baddeathimage;
    transient Image aliveimage;
    public Creature(){}
    public Creature(int x,int y)
    {
        loc = new Loc();
        loc.x=x;
        loc.y=y;

        BatteleField.field[x][y] = this;
        URL url;
        url=this.getClass().getClassLoader().getResource("picture/RIP.png");
        gooddeathimage = new Image(url.toString());
        url=this.getClass().getClassLoader().getResource("picture/death.jpg");
        baddeathimage = new Image(url.toString());
    }

    abstract public void drawMyself(GraphicsContext gc);
    public boolean isOverstep(){
        return (loc.x < 0 || loc.x > 10 || loc.y < 0 || loc.y > 10);
    }
    public boolean isalive(){return livevalue>0;}
    public void drawBlood(GraphicsContext gc){
        double breakpoint = ((double) livevalue / maxlivevalue)*BatteleField.block_x_len;
        gc.setStroke(Color.LIGHTGREEN);
        gc.setLineWidth(3);
        gc.strokeLine(loc.x*BatteleField.block_x_len,loc.y*BatteleField.block_y_len+2,loc.x*BatteleField.block_x_len+breakpoint,loc.y*BatteleField.block_y_len+2);
        if(livevalue!=maxlivevalue){
            gc.setStroke(Color.RED);
            gc.strokeLine(loc.x*BatteleField.block_x_len+breakpoint,loc.y*BatteleField.block_y_len+2,(loc.x+1)*BatteleField.block_x_len,loc.y*BatteleField.block_y_len+2);
        }
        gc.setLineWidth(1);
    }
    public void drawdeath(GraphicsContext gc){
        if(camp==Camp.GOOD){
            gc.drawImage(gooddeathimage,loc.x* BatteleField.block_x_len,loc.y*BatteleField.block_y_len,BatteleField.block_x_len,BatteleField.block_y_len);
        }
        else{
            gc.drawImage(baddeathimage,loc.x* BatteleField.block_x_len,loc.y*BatteleField.block_y_len,BatteleField.block_x_len,BatteleField.block_y_len);
        }
    }
    public Loc getLoc(){return loc;}
    public boolean getchosen(){return chosen;}
    public int getLivevalue(){return livevalue;}
    public void setLoc(int x,int y){loc.x=x;loc.y=y;}
    public void setLivevalue(int value){livevalue=value;}
    private void attack(Creature attacked){
        attacked.livevalue -= attackvalue * attacked.defence;
//        if(attacked.livevalue==0){
//            System.out.println("真的有碰巧等于0的");
//        }
        if(attacked.livevalue<=0){
            if(attacked.camp==Camp.GOOD){
                goodcreaturenum--;
            }
            else {
                badcreaturenum--;
            }
            if(goodcreaturenum==0||badcreaturenum==0){
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (UIController.gamestate) {
                    UIController.gamestate.notifyAll();    //唤醒监听的线程
                }
            }
        }
    }
    public void move(int direction){
        if(isalive()){
            synchronized (BatteleField.creatures){
                int x = loc.x;
                int y = loc.y;
                int flag=0;
                switch (direction){
                    case 0:
                        if(loc.y>0)
                            y--;
                        break;
                    case 1:
                        if(loc.y<10)
                            y++;
                        break;
                    case 2:
                        if(loc.x>0)
                            x--;
                        break;
                    case 3:
                        if(loc.x<10)
                            x++;
                }
                Creature creature;
                synchronized (BatteleField.field) {
                    creature = BatteleField.field[x][y];
                }
                if(creature!=this&&creature!=null&&creature.isalive()){  //撞到了一个生物
                    if(creature.camp!=this.camp){  //不是同一个阵营，设定原来位置上的生物遭到攻击
                        this.attack(creature);
                    }
                    flag=1;
                }
                if(flag==0){
                    synchronized (BatteleField.field){
                        BatteleField.field[loc.x][loc.y] = null;
                        loc.x=x;
                        loc.y=y;
                        BatteleField.field[loc.x][loc.y] = this;
                    }
                }

            }
        }
    }
   public void setChosenFalse(){chosen=false;}
   public void setChosenTrue(){chosen=true;}

    @Override
    public void run() {
        while(isalive()&& GameState.state==GameState.Gamestate.INGAME){
            if(chosen==false){
                int direction = random.nextInt(4);
                move(direction);
            }
            if(!isalive()){
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
