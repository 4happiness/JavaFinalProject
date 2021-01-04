package creature;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.BatteleField;

import java.net.URL;

public class Snake extends Creature {
    public Snake(){}
    public Snake(int x, int y) {
        super(x, y);
        badcreaturenum++;

        livevalue = 600;
        speed = 40;
        attackvalue = 200;
        defence = 0.6;
        camp = Camp.BAD;
        URL url=this.getClass().getClassLoader().getResource("picture/Snake.png");
        aliveimage = new Image(url.toString());
        campid = 1;
        rank = 10;
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
