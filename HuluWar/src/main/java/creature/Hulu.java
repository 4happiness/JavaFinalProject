package creature;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ui.BatteleField;

import java.net.URL;

public class Hulu extends Creature {
    public Hulu(){}
    public Hulu(int x,int y,int rank){
        super(x,y);
        this.rank=rank;
        goodcreaturenum++;
        livevalue = 200;
        speed=30;
        attackvalue=100;
        defence = 0.5;
        camp= Camp.GOOD;
        URL url=this.getClass().getClassLoader().getResource("picture/"+rank+".png");
        aliveimage = new Image(url.toString());
        campid = 0;
        maxlivevalue = livevalue;
    }

    @Override
    public void drawMyself(GraphicsContext gc) {
        if(isalive())
        {
            gc.drawImage(aliveimage,loc.x* BatteleField.block_x_len,loc.y*BatteleField.block_y_len,BatteleField.block_x_len,BatteleField.block_y_len);
            drawBlood(gc);
        }
        else
            drawdeath(gc);
    }
}
