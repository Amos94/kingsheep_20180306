package kingsheep.team.anecul;

import javafx.util.Pair;
import kingsheep.Creature;
import kingsheep.Simulator;
import kingsheep.Type;

import java.util.*;

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

    //sort the HashMap ASC
    public HashMap<Pair<Integer, Integer>, Integer> sortHashMap(HashMap<Pair<Integer, Integer>, Integer> objCoordinates){

        HashMap<Pair<Integer, Integer>, Integer> mapToReturn = new HashMap<Pair<Integer, Integer>, Integer>();

        //get the values
        List<Integer> values = new ArrayList<>();
        values.addAll(objCoordinates.values());

        //sort ASC the list
        Collections.sort(values);

        for(Pair<Integer,Integer> key:objCoordinates.keySet()){
            for(Integer value:values){
                if(objCoordinates.get(key) == value){
                    mapToReturn.put(key, value);
                }
            }
        }

        return mapToReturn;
    }

    protected class AStar{

        protected class Square{

            //The coordinates
            int xPos, yPos;

            //parent of the current square
            Square parent;

            //The heuristic I use is Manhattand distance
            int heuristicCost = 0;
            //The Final cost is the function F = G + H (Manhattan Distance)
            int finalCost = 0;

            protected Square(int xPos, int yPos){
                this.xPos = xPos;
                this.yPos = yPos;
            }

            protected Pair<Integer, Integer> getPositionPair(){
                return new Pair<Integer, Integer>(xPos,yPos);
            }

            protected String getPosition(){
                return "xPos: "+this.xPos+", yPos: "+this.yPos;
            }

            
        }
    }





    public String getNickname(){
        //TODO change this to any nickname you like. This should not be your uzh_shortname. That way you can stay anonymous on the ranking list.
        return "A/N";
    }
}
