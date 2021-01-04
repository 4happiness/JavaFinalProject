package playback;

import creature.Creature;

import java.io.Serializable;

public class CreatureLog implements Serializable {
    int livevalue;
    int x;
    int y;
    boolean chosen;
    int campid;
    int rank;
    int maxlivevalue;
    CreatureLog(Creature creature){
        livevalue = creature.getLivevalue();
        x = creature.getLoc().x;
        y = creature.getLoc().y;
        chosen = creature.getchosen();
        campid = creature.campid;
        rank = creature.rank;
        maxlivevalue = creature.maxlivevalue;
    }
}
//    private void writeObject(ObjectOutputStream stream) throws IOException {
//        stream.writeInt(livevalue);
//        stream.writeObject(loc);
//        stream.writeBoolean(chosen);
//        stream.writeInt(campid);
//        stream.writeInt(rank);
//    }
//    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
//        livevalue = stream.readInt();
//        loc = (Loc)stream.readObject();
//        chosen = stream.readBoolean();
//        campid = stream.readInt();
//        rank = stream.readInt();
//    }