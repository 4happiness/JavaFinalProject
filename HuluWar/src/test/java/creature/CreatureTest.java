package creature;

import javafx.scene.canvas.GraphicsContext;
import org.junit.Before;
import org.junit.Test;
import ui.BatteleField;
import ui.Map;
import ui.UIController;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CreatureTest {

    @Before
    public void setup(){
        BatteleField.field=new Creature[BatteleField.x_block_num][BatteleField.y_block_num];

        for(int i=0;i<BatteleField.x_block_num;i++) {
            for(int j=0;j<BatteleField.y_block_num;j++){
                BatteleField.field[i][j] = null;
            }
        };
        BatteleField.creatures = new ArrayList<>();
    }

    @Test
    public void isalive() {
        Hulu hulu = new Hulu();
        hulu.setLivevalue(0);
        assertFalse(hulu.isalive());
    }

    @Test
    public void move1() {
        //testcase1 想要移动的地方未被占领
        Hulu hulu = new Hulu();
        hulu.loc = new Loc();
        hulu.setLoc(0, 0);

        hulu.setLivevalue(100);
        hulu.move(1);
        assertEquals(hulu.getLoc().x,0);
        assertEquals(hulu.getLoc().y,1);
    }
    @Test
    public void move2(){
        //testcase2 想要移动的地方已有同一阵营的生物
        Hulu hulu = new Hulu();
        hulu.loc = new Loc();
        hulu.camp = Creature.Camp.GOOD;
        hulu.setLoc(0, 0);
        hulu.setLivevalue(100);

        Hulu hulu1 = new Hulu();
        hulu1.loc = new Loc();
        hulu1.setLoc(0,1);
        hulu1.setLivevalue(100);
        hulu1.camp = Creature.Camp.GOOD;
        BatteleField.field[0][1] = hulu1;
        hulu.move(1);
        //hulu应该未移动
        assertEquals(hulu.getLoc().x,0);
        assertEquals(hulu.getLoc().y,0);
    }

    @Test
    public void move3(){
        Hulu hulu = new Hulu();
        hulu.loc = new Loc();
        hulu.camp = Creature.Camp.GOOD;
        hulu.setLoc(0, 0);
        hulu.setLivevalue(100);
        hulu.attackvalue = 200;

        Snake snake = new Snake();
        snake.loc = new Loc();
        snake.setLoc(0,1);
        snake.setLivevalue(100);
        snake.camp = Creature.Camp.BAD;
        snake.defence = 1;
        BatteleField.field[0][1] = snake;

        Creature.badcreaturenum = 9;
        Creature.goodcreaturenum = 9;

        hulu.move(1);
        //hulu应该未移动
        assertEquals(hulu.getLoc().x,0);
        assertEquals(hulu.getLoc().y,0);
        //蛇应该受到攻击
        assertTrue(snake.livevalue<100);
    }
}