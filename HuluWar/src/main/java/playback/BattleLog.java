package playback;

import creature.Creature;
import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;
import ui.BatteleField;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.Serializable;
import java.util.ArrayList;

//public class BattleLog implements Serializable {
//    ArrayList<CreatureLog> creatureLogs;
//    public BattleLog(){
//        creatureLogs = new ArrayList<>();
//    }
//   public void addCreatureLog(ArrayList<Creature> creatures){
//        for(int i=0;i<creatures.size();i++){
//            if(i<7)
//                creatureLogs.add(new CreatureLog(creatures.get(i), 0));
//            else if(i>8&&i<16)
//                creatureLogs.add(new CreatureLog(creatures.get(i), 1));
//            else
//                creatureLogs.add(new CreatureLog(creatures.get(i), i));
//
//        }
//    }
//}
public class BattleLog implements Serializable{
    ArrayList<CreatureLog> creatureLogs;
    int result;
    public BattleLog(ArrayList<Creature> creatures){
        creatureLogs = new ArrayList<>();
        for(Creature creature:creatures){
            creatureLogs.add(new CreatureLog(creature));
        }
        if(Creature.goodcreaturenum==0){
            result = 0;
        }
        else if(Creature.badcreaturenum==0){
            result = 1;
        }
        else{
            result = 2;
        }
    }
}
