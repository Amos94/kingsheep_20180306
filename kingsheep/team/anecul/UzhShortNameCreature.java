package kingsheep.team.anecul;

import kingsheep.Creature;
import kingsheep.Simulator;
import kingsheep.Type;

/**
 * Created by kama on 04.03.16.
 */
public abstract class UzhShortNameCreature extends Creature {

    public UzhShortNameCreature(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
    }

    public int getManhattanDistance(int startX, int startY, int endX, int endY){

        return Math.abs(startX - endX) + Math.abs(startY - endY);
    }





    public String getNickname(){
        //TODO change this to any nickname you like. This should not be your uzh_shortname. That way you can stay anonymous on the ranking list.
        return "A/N";
    }
}
