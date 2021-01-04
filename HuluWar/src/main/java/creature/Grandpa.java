package creature;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import ui.BatteleField;

import java.net.URL;

public class Grandpa extends Creature {
    public Grandpa(){}
    public Grandpa(int x, int y) {
        super(x, y);
        goodcreaturenum++;
        livevalue = 30;
        speed=10;
        attackvalue=20;
        defence=1;
        camp= Camp.GOOD;
        URL url=this.getClass().getClassLoader().getResource("picture/Grandpa.png");
        aliveimage = new Image(url.toString());
        campid = 0;
        rank = 0;
        maxlivevalue = livevalue;
    }

    @Override
    public void drawMyself(GraphicsContext gc) {
        if(isalive())
        {
            gc.drawImage(aliveimage,loc.x* BatteleField.block_x_len,loc.y*BatteleField.block_y_len,BatteleField.block_x_len,BatteleField.block_y_len);
            drawBlood(gc);
        }
        else{
            drawdeath(gc);
        }

    }

}
