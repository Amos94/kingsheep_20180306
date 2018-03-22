package kingsheep.team.anecul;

import javafx.util.Pair;
import kingsheep.Creature;
import kingsheep.Simulator;
import kingsheep.Type;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by kama on 04.03.16.
 */
public abstract class UzhShortNameCreature extends Creature {

    public UzhShortNameCreature(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
    }

    //Calculate Manhattan Distance
    public int calculateManhattanDistance(int startX, int startY, int endX, int endY){

        return Math.abs(startX - endX) + Math.abs(startY - endY);
    }

    //Get the manhattan distance between a creature, i.e. sheep/ wolf, and all the objectives, i.e. grass/ mushroom/ adversary sheep.
    public HashMap<Pair<Integer, Integer>, Integer> getObjectiveDistances(ArrayList<Pair<Integer, Integer>> objCoordinates){

        HashMap<Pair<Integer, Integer>, Integer> distanceToObjectives = new HashMap<Pair<Integer,Integer>, Integer>();

        for(Pair<Integer, Integer> objective:objCoordinates){
            distanceToObjectives.put(objective, calculateManhattanDistance(this.x, this.y, objective.getKey(), objective.getValue()));
        }

        return distanceToObjectives;
    }





    public String getNickname(){
        //TODO change this to any nickname you like. This should not be your uzh_shortname. That way you can stay anonymous on the ranking list.
        return "A/N";
    }
}
