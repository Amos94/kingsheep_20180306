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

    Type [][] currentMap;

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

    //uptate the current state of the map
    public void updateMap(Type[][] map){
        this.currentMap = map;
    }

    //get maximum number of columns
    public int getXSize(Type map[][]){
        return map[0].length;
    }

    //get maximum number of rows
    public int getYSize(Type map[][]){
        return map.length;
    }

    //get current creature x position (column)
    protected int getCurrentXPos(){
        return this.x;
    }

    //get current creature y position (row)
    protected int getCurrentYPos(){
        return this.y;
    }

    //current state of the map
    protected Type[][] currentStateOfTheMap(Type[][] map){
        return map;
    }


    //implementation of A* algorithm (hopefully for the win)
    protected class AStar{

        //this class represents squares of the map
        protected class Square{

            //The coordinates
            int xPos, yPos;
            Type type;

            //parent of the current square
            Square parent;

            //The heuristic I use is Manhattan distance
            int heuristicCost = 0;
            //The Final cost is the function F = G + H (Manhattan Distance)
            int finalCost = 0;

            //constructor
            protected Square(int xPos, int yPos){
                this.xPos = xPos;
                this.yPos = yPos;
            }

            //constructor
            protected Square(int xPos, int yPos, Type type){
                this.xPos = xPos;
                this.yPos = yPos;
                this.type = type;
            }

            protected Pair<Integer, Integer> getPositionPair(){
                return new Pair<Integer, Integer>(xPos,yPos);
            }

            protected String getPosition(){
                return "xPos: "+this.xPos+", yPos: "+this.yPos;
            }

            //set the type of a square
            protected void setType(Type type){
                this.type = type;
            }

            //type of a square
            protected Type getType(){
                return type;
            }


        }

        //remake the map
        //map size
        int ySize = getYSize(currentMap); //rows
        int xSize = getXSize(currentMap); //columns

        Square [][]map = new Square[ySize][xSize];
        boolean closed[][];


        PriorityQueue<Square> openQueue;
        //start positions, initialised with the current position of the animal
        int startXPos = getCurrentXPos();
        int startYPos = getCurrentYPos();

        //desired destination X and Y positions
        int endXPosition, endYPosition;

        //set the destination position
        public void setDestination(int endXPosition, int endYPosition){
            this.endXPosition = endXPosition;
            this.endYPosition = endYPosition;
        }

        //set start positions
        public void setStartPositions(int startXPos, int startYPos){
            this.startXPos = startXPos;
            this.startYPos = startYPos;
        }

        protected void checkAndUpdateCost(Square current, Square newSquare, int cost){
            if(newSquare.getType() == Type.FENCE || closed[newSquare.yPos][newSquare.xPos]) return;

            int newSquareFinalCost = newSquare.heuristicCost + cost;

            boolean isOpen = openQueue.contains(newSquare);

            if(!isOpen || newSquareFinalCost < newSquare.finalCost){
                newSquare.finalCost = newSquareFinalCost;
                newSquare.parent = current;
                if(!isOpen){
                    openQueue.add(newSquare);
                }
            }
        }

    }



    public String getNickname(){
        //TODO change this to any nickname you like. This should not be your uzh_shortname. That way you can stay anonymous on the ranking list.
        return "A/N";
    }
}
