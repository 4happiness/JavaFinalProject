
package playback;

import creature.Creature;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import ui.BatteleField;
import ui.GameState;
import ui.Map;
import ui.UIController;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LogManager {
    public static ObjectInputStream in;
    public static ObjectOutputStream out;
    GraphicsContext gc;
    Map map;
    ArrayList<Image> images;        //由于Image无法序列化，所以只能在这边又记录一下Image
    ArrayList<CreatureLog> creatureLogs;
    Timeline timeline;
    //static int num = 0;
    int res = 2;
    public LogManager(GraphicsContext gc){
        this.gc = gc;
        map = new Map(gc);
        images = new ArrayList<>();
        URL url;
        url=this.getClass().getClassLoader().getResource("picture/Grandpa.png");
        images.add(new Image(url.toString()));
        for(int i=1;i<8;i++){
            url=this.getClass().getClassLoader().getResource("picture/"+i+".png");
            images.add(new Image(url.toString()));
        }
        url=this.getClass().getClassLoader().getResource("picture/Pangolin.jfif");
        images.add(new Image(url.toString()));
        url=this.getClass().getClassLoader().getResource("picture/Scorpion.png");
        images.add(new Image(url.toString()));
        url=this.getClass().getClassLoader().getResource("picture/Snake.png");
        images.add(new Image(url.toString()));
        url=this.getClass().getClassLoader().getResource("picture/LittleMonster.png");
        images.add(new Image(url.toString()));

        url=this.getClass().getClassLoader().getResource("picture/RIP.png");
        images.add(new Image(url.toString()));
        url=this.getClass().getClassLoader().getResource("picture/death.jpg");
        images.add(new Image(url.toString()));

    }


    public static void closeOutputStream() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLog(BattleLog battleLog) throws IOException {
        out.writeObject(battleLog);
    }
    public BattleLog readLog() throws IOException {
        BattleLog log1=null;
        try {
            log1=(BattleLog) in.readObject();
            //System.out.println("爷爷的位置：("+log1.creatureLogs.get(7).x+" "+log1.creatureLogs.get(7).y+")"+":"+log1.creatureLogs.get(7).livevalue);
            //num++;
        }
        catch (IOException e) {
            timeline.stop();
            in.close();
            drawOver();
            GameState.state = UIController.statebefore;
            //System.out.println(num);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return log1;
    }
    private void drawcreatures(){
        for(CreatureLog creatureLog:creatureLogs){
            int x = creatureLog.x;
            int y = creatureLog.y;
            if(creatureLog.chosen){
                Map.drawblock(x,y);
            }
            if(creatureLog.livevalue>0) {
                gc.drawImage(images.get(creatureLog.rank), x * BatteleField.block_x_len, y * BatteleField.block_y_len, BatteleField.block_x_len, BatteleField.block_y_len);
                int livevalue = creatureLog.livevalue;
                int maxlivevalue = creatureLog.maxlivevalue;
                double breakpoint = ((double) livevalue / maxlivevalue)*BatteleField.block_x_len;
                gc.setStroke(Color.LIGHTGREEN);
                gc.setLineWidth(3);
                gc.strokeLine(x*BatteleField.block_x_len,y*BatteleField.block_y_len+2,x*BatteleField.block_x_len+breakpoint,y*BatteleField.block_y_len+2);
                if(livevalue!=maxlivevalue){
                    gc.setStroke(Color.RED);
                    gc.strokeLine(x*BatteleField.block_x_len+breakpoint,y*BatteleField.block_y_len+2,(x+1)*BatteleField.block_x_len,y*BatteleField.block_y_len+2);
                }
                gc.setLineWidth(1);
            }
            else{
                if(creatureLog.campid==0){
                    gc.drawImage(images.get(12),x* BatteleField.block_x_len,y*BatteleField.block_y_len,BatteleField.block_x_len,BatteleField.block_y_len);
                }
                else{
                    gc.drawImage(images.get(13),x* BatteleField.block_x_len,y*BatteleField.block_y_len,BatteleField.block_x_len,BatteleField.block_y_len);
                }
            }

        }
    }
    public void reviewfiled(){
        map.drawBackgroud();
        map.drawMap();
        drawcreatures();
    }
    public void drawOver(){
        URL url;
        if(res==1){
            url=this.getClass().getClassLoader().getResource("picture/GoodGuyWins.png");
        }
        else if(res==0){
            url=this.getClass().getClassLoader().getResource("picture/BadGuyWins.png");
        }
        else{
            url = null;
        }
        Image overimage = new Image(url.toString());
        gc.drawImage(overimage,0,200,881,150);
    }
    public void review() throws IOException {

        timeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        event1 -> {
                            try {
                                BattleLog log=readLog();
                                if(log!=null){
                                    creatureLogs = log.creatureLogs;
                                    res = log.result;
                                    reviewfiled();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }),
                new KeyFrame(Duration.millis(10))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

//        BattleLog log = readLog();
//        while(log!=null){
//            creatureLogs = log.creatureLogs;
//            //System.out.println("第一个葫芦娃的位置：("+creatures.get(7).getLoc().x+" "+creatures.get(7).getLoc().y+")"+":"+creatures.get(7).getLivevalue());
//            log = readLog();
//        }
//        System.out.println(num);
    }

}
