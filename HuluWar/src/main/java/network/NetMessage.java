package network;

import creature.Creature;
import ui.BatteleField;
import ui.UIController;

import java.io.Serializable;
import java.util.ArrayList;

class CreatureMessage implements Serializable {
    int x;
    int y;
    int living;
    CreatureMessage(Creature creature,int mode){
        if(mode==1){
            x = creature.getLoc().x;
            y = creature.getLoc().y;
            living = -1;
        }else{
            x = -1;
            y = -1;
            living = creature.getLivevalue();
        }

    }
}
public class NetMessage implements Serializable{
    ArrayList<CreatureMessage> creatureMessages;
    NetMessage(ArrayList<Creature> creatures){
        creatureMessages = new ArrayList<>();
        if(UIController.clientID ==1){
            for(int i=0;i<18;i++){
                if(i<9){
                    creatureMessages.add(new CreatureMessage(creatures.get(i), 1));
                }
                else{
                    creatureMessages.add(new CreatureMessage(creatures.get(i), 2));
                }
            }
        }
        else{
            for(int i=0;i<18;i++){
                if(i<9){
                    creatureMessages.add(new CreatureMessage(creatures.get(i), 2));
                }
                else{
                    creatureMessages.add(new CreatureMessage(creatures.get(i), 1));
                }
            }
        }
    }
}
