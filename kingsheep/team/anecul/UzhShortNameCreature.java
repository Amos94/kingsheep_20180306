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
    protected int calculateManhattanDistance(int startX, int startY, int endX, int endY){

        return Math.abs(startX - endX) + Math.abs(startY - endY);
    }

    //Get the manhattan distance between a creature, i.e. sheep/ wolf, and all the objectives, i.e. grass/ mushroom/ adversary sheep.
    protected HashMap<Pair<Integer, Integer>, Integer> getObjectiveDistances(ArrayList<Pair<Integer, Integer>> objCoordinates){

        HashMap<Pair<Integer, Integer>, Integer> distanceToObjectives = new HashMap<Pair<Integer,Integer>, Integer>();

        for(Pair<Integer, Integer> objective:objCoordinates){
            distanceToObjectives.put(objective, calculateManhattanDistance(this.x, this.y, objective.getKey(), objective.getValue()));
        }

        return distanceToObjectives;
    }

    //sort the HashMap ASC
    protected HashMap<Pair<Integer, Integer>, Integer> sortHashMap(HashMap<Pair<Integer, Integer>, Integer> objCoordinates){

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
    protected void updateMap(Type[][] map){
        this.currentMap = map;
    }

    //get maximum number of columns
    protected int getXSize(Type map[][]){
        return map[0].length;
    }

    //get maximum number of rows
    protected int getYSize(Type map[][]){
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

        //The cost of each move
        int costPerMove = 1;

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
        protected void setDestination(int endXPosition, int endYPosition){
            this.endXPosition = endXPosition;
            this.endYPosition = endYPosition;
        }

        //set start positions
        protected void setStartPositions(int startXPos, int startYPos){
            this.startXPos = startXPos;
            this.startYPos = startYPos;
        }

        protected void setBlocked(int xPos, int yPos){
            map[yPos][xPos] = null;
        }

        protected void checkAndUpdateCost(Square current, Square newSquare, int cost){
            if(newSquare.getType() == Type.FENCE || closed[newSquare.yPos][newSquare.xPos]) return;

            //set the heuristic cost of square
            newSquare.heuristicCost = calculateManhattanDistance(newSquare.xPos, newSquare.yPos, endXPosition, endYPosition);

            //update the final cost
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

        protected void AStarSearch(){
            //to implement the A*
            openQueue.add(map[startYPos][startXPos]);

            Square current;

            while(true){
                current = openQueue.poll();
                if(current.type == Type.FENCE){
                    break;
                }
                closed[current.yPos][current.xPos] = true;

                if(current.equals(map[endYPosition][endXPosition])){
                    return;
                }

                Square s;

                //UP
                if(current.yPos - 1 >= 0){
                    s = map[current.yPos][current.xPos];
                    checkAndUpdateCost(current, s, current.finalCost+costPerMove);
                }

                //DOWN
                if(current.yPos+1<map.length) {
                    s = map[current.yPos + 1][current.xPos];
                    checkAndUpdateCost(current, s, current.finalCost + costPerMove);
                }

                //LEFT
                if(current.xPos-1>=0){
                    s = map[current.yPos][current.xPos-1];
                    checkAndUpdateCost(current, s, current.finalCost+costPerMove);
                }

                //RIGHT
                if(current.xPos+1<map[0].length){
                    s = map[current.yPos][current.xPos+1];
                    checkAndUpdateCost(current, s, current.finalCost+costPerMove);
                }
            }

        }

        public ArrayList<Square> searchWithAStar(int x, int y, int si, int sj, int ei, int ej, int[][] blocked){

            ArrayList<Square> toReturn = new ArrayList<Square>();
            //Initialize a new search
            map = new Square[x][y];
            closed = new boolean[x][y];

            openQueue = new PriorityQueue<>((Object o1, Object o2) -> {
                Square c1 = (Square)o1;
                Square c2 = (Square)o2;

                return c1.finalCost<c2.finalCost?-1:
                        c1.finalCost>c2.finalCost?1:0;
            });

            //Set start positions
            setStartPositions(si, sj);  //Setting to 0,0 by default. Will be useful for the UI part

            //Set destinations
            setDestination(ei, ej);

            for(int i=0;i<x;++i){
                for(int j=0;j<y;++j){
                    map[i][j] = new Square(i, j);
                    map[i][j].heuristicCost = Math.abs(i-endYPosition)+Math.abs(j-endXPosition);
//                  System.out.print(grid[i][j].heuristicCost+" ");
                }
//              System.out.println();
            }
            map[si][sj].finalCost = 0;

           /*
             Set blocked cells. Simply set the cell values to null
             for blocked cells.
           */
            for(int i=0;i<blocked.length;++i){
                setBlocked(blocked[i][0], blocked[i][1]);
            }

            //Display initial map
            System.out.println("Grid: ");
            for(int i=0;i<x;++i){
                for(int j=0;j<y;++j){
                    if(i==si&&j==sj)System.out.print("SO  "); //Source
                    else if(i==ei && j==ej)System.out.print("DE  ");  //Destination
                    else if(map[i][j]!=null)System.out.printf("%-3d ", 0);
                    else System.out.print("BL  ");
                }
                System.out.println();
            }
            System.out.println();

            AStarSearch();
            System.out.println("\nScores for cells: ");
            for(int i=0;i<x;++i){
                for(int j=0;j<x;++j){
                    if(map[i][j]!=null)System.out.printf("%-3d ", map[i][j].finalCost);
                    else System.out.print("BL  ");
                }
                System.out.println();
            }
            System.out.println();

            if(closed[endYPosition][endXPosition]){
                //Trace back the path
                System.out.println("Path: ");
                Square current = map[endYPosition][endXPosition];
                System.out.print(current);
                while(current.parent!=null){
                    System.out.print(" -> "+current.parent);
                    toReturn.add(current.parent);
                    current = current.parent;
                }
                System.out.println();
            }else System.out.println("No possible path");

            return toReturn;
        }

    }







    public String getNickname(){
        //TODO change this to any nickname you like. This should not be your uzh_shortname. That way you can stay anonymous on the ranking list.
        return "A/N";
    }
}
