package creature;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.BatteleField;

import java.net.URL;

public class Pangolin extends Creature {

    public Pangolin(){}
    public Pangolin(int x, int y) {
        super(x, y);
        goodcreaturenum++;
        livevalue = 50;
        speed=20;
        attackvalue=50;
        defence=0.8;
        camp= Camp.GOOD;
        URL url=this.getClass().getClassLoader().getResource("picture/Pangolin.jfif");
        aliveimage= new Image(url.toString());
        campid = 0;
        rank = 8;
        maxlivevalue = livevalue;
    }

    @Override
    public void drawMyself(GraphicsContext gc) {
        if(isalive()){
            gc.drawImage(aliveimage,loc.x* BatteleField.block_x_len,loc.y*BatteleField.block_y_len,BatteleField.block_x_len,BatteleField.block_y_len);
            drawBlood(gc);
        }
        else
            drawdeath(gc);

    }
}
