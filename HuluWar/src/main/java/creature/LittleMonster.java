package creature;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.BatteleField;

import java.net.URL;

public class LittleMonster extends Creature {
    public LittleMonster(){}
    public LittleMonster(int x, int y) {
        super(x, y);
        badcreaturenum++;

        livevalue = 50;
        speed=20;
        attackvalue=50;
        defence=0.8;
        camp= Camp.BAD;
        URL url=this.getClass().getClassLoader().getResource("picture/LittleMonster.png");
        aliveimage = new Image(url.toString());
        campid = 1;
        rank = 11;
        maxlivevalue = livevalue;
    }

    @Override
    public void drawMyself(GraphicsContext gc) {
        if(isalive()) {
            gc.drawImage(aliveimage,loc.x* BatteleField.block_x_len,loc.y*BatteleField.block_y_len,BatteleField.block_x_len,BatteleField.block_y_len);
            drawBlood(gc);
        }
        else{
            drawdeath(gc);
        }

    }
}
