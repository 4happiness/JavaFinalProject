package ui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URL;

public class Map {
    static GraphicsContext gc;
    static Image bg;
    public Map(GraphicsContext gc){
        this.gc = gc;
        URL url=this.getClass().getClassLoader().getResource("picture/Background2.png");
        bg = new Image(url.toString());
    }
    public void drawBackgroud(){
        gc.drawImage(bg,0,0,880,550);
    }

    public void drawMap(){
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        for (int i = 80; i < 880; i += 80) {
            gc.strokeLine(i, 0, i , 550);
        }
        for(int i=50;i<550;i+=50) {
            gc.strokeLine(0, i, 880, i);
        }
    }
    public static void drawblock(int x,int y){
        gc.setStroke(Color.YELLOW);
        gc.strokeLine(x*BatteleField.block_x_len,y*BatteleField.block_y_len,(x+1)*BatteleField.block_x_len,y*BatteleField.block_y_len);
        gc.strokeLine(x*BatteleField.block_x_len,y*BatteleField.block_y_len,(x)*BatteleField.block_x_len,(y+1)*BatteleField.block_y_len);
        gc.strokeLine((x+1)*BatteleField.block_x_len,y*BatteleField.block_y_len,(x+1)*BatteleField.block_x_len,(y+1)*BatteleField.block_y_len);
        gc.strokeLine(x*BatteleField.block_x_len,(y+1)*BatteleField.block_y_len,(x+1)*BatteleField.block_x_len,(y+1)*BatteleField.block_y_len);
    }
}
