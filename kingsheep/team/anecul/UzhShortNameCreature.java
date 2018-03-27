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

    public Type [][] currentMap;

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
    protected Type[][] getCurrentMap(){
        return this.currentMap;
    }

    //current state of the map
    protected Type[][] currentStateOfTheMap(Type[][] map){
        return map;
    }

    protected Type getNodeType(int nodeTypeXPos, int nodeTypeYPos){
        return this.currentMap[nodeTypeYPos][nodeTypeXPos];
    }

/*
    //implementation of A* algorithm (hopefully for the win)
    protected class AStar{
        Type [][] m;
        public int xSize;
        public int ySize;
        protected AStar(){
            System.out.println("");
        }

        protected AStar(Type[][] map, int xSize, int ySize){
            this.m = map;
            this.xSize = xSize;
            this.ySize = ySize;

//            for(int i=0; i<m.length;++i){
//                for(int j=0; j<m[0].length; ++j){
//                    System.out.print(m[i][j]+" ");
//                }
//                System.out.println("");
//            }
//            System.out.println("");                System.out.println("");                System.out.println("");
        }
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
//
//        int ySize = this.ySize;//getYSize(m); //rows
//        int xSize = this.xSize;//getXSize(m); //columns

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
//            System.out.println("x: " + xPos);
//
//            System.out.println("y "+yPos);
//            System.out.println("------");
            map[xPos][yPos] = null;
        }

        protected void checkAndUpdateCost(Square current, Square newSquare, int cost){
            if(newSquare.getType() == Type.FENCE || closed[newSquare.yPos][newSquare.xPos]) return;

            //set the heuristic cost of square
            //newSquare.heuristicCost = calculateManhattanDistance(newSquare.xPos, newSquare.yPos, endXPosition, endYPosition);

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
                    //System.out.println("UP\n"+"X POS: "+ s.xPos+",\nY POS: "+s.yPos+",\nTYPE: "+s.type+"\n-----");
                    checkAndUpdateCost(current, s, current.finalCost+costPerMove);
                }

                //DOWN
                if(current.yPos+1<map.length) {
                    s = map[current.yPos + 1][current.xPos];
                    //System.out.println("DOWN\n"+"X POS: "+ s.xPos+",\nY POS: "+s.yPos+",\nTYPE: "+s.type+"\n-----");
                    checkAndUpdateCost(current, s, current.finalCost + costPerMove);
                }

                //LEFT
                if(current.xPos-1>=0){
                    s = map[current.yPos][current.xPos-1];
                    //System.out.println("LEFT\n"+"X POS: "+ s.xPos+",\nY POS: "+s.yPos+",\nTYPE: "+s.type+"\n-----");
                    checkAndUpdateCost(current, s, current.finalCost+costPerMove);
                }

                //RIGHT
                if(current.xPos+1<map[0].length){
                    s = map[current.yPos][current.xPos+1];
                    //System.out.println("RIGHT\n"+"X POS: "+ s.xPos+",\nY POS: "+s.yPos+",\nTYPE: "+s.type+"\n-----");
                    checkAndUpdateCost(current, s, current.finalCost+costPerMove);
                }
            }

        }

        public ArrayList<Square> searchWithAStar(Type[][] currentStateOfMap, int currentStartXPos, int currentStartYPos, int endXPos, int endYPos, int[][] blocked){

            ArrayList<Square> toReturn = new ArrayList<Square>();
            //Initialize a new search


//            System.out.println("Start X POS: "+ currentStartXPos);
//            System.out.println("Start Y POS: "+currentStartYPos);
//            System.out.println("END X POS: "+endXPos);
//            System.out.println("END Y POS: "+endYPos);

            map = new Square[currentStateOfMap.length][currentStateOfMap[0].length];
            closed = new boolean[currentStateOfMap.length][currentStateOfMap[0].length];

            openQueue = new PriorityQueue<>((Object o1, Object o2) -> {
                Square c1 = (Square)o1;
                Square c2 = (Square)o2;

                return c1.finalCost<c2.finalCost?-1:
                        c1.finalCost>c2.finalCost?1:0;
            });

            //Set start positions
            setStartPositions(currentStartXPos, currentStartYPos);  //Setting to 0,0 by default. Will be useful for the UI part

            //Set destinations
            setDestination(endXPos, endYPos);

            for(int i=0;i<currentStateOfMap.length;++i){
                for(int j=0;j<currentStateOfMap[0].length;++j){

                    map[i][j] = new Square(i, j, currentStateOfMap[i][j]);
                    map[i][j].heuristicCost = Math.abs(i-endYPosition)+Math.abs(j-endXPosition);
                    //System.out.print("HEURISTIC COST: "+map[i][j].heuristicCost+" ");
                }

            }
//            System.out.println("si:" + si);
//            System.out.println("sj" + sj);
//            System.out.println("----");
            map[currentStartYPos][currentStartXPos].finalCost = 0;


//             Set blocked cells. Simply set the cell values to null
//             for blocked cells.

            for(int i=0;i<blocked.length;++i){
                for(int j=0; j<blocked[0].length; ++j)
                    if(blocked[i][j] == -1)
                        setBlocked(i, j);
            }

            //Display initial map
//            System.out.println("Grid: ");
//            for(int i=0;i<currentStateOfMap.length;++i){
//                for(int j=0;j<currentStateOfMap[0].length;++j){
//                    if(i==currentStartYPos&&j==currentStartXPos)System.out.print("SO  "); //Source
//                    else if(i==endYPos && j==endXPos)System.out.print("DE  ");  //Destination
//                    else if(map[i][j]!= null)System.out.printf("%-3d ", 0);
//                    else System.out.print("BL  ");
//                }
//                System.out.println();
//            }
//            System.out.println();

            AStarSearch();
//            System.out.println("\nScores for cells: ");
//            for(int i=0;i<currentStateOfMap.length;++i){
//                for(int j=0;j<currentStateOfMap[0].length;++j){
//                    if(map[i][j]!=null)System.out.printf("%-3d ", map[i][j].finalCost);
//                    else System.out.print("BL  ");
//                }
//                System.out.println();
//            }
//            System.out.println();

            if(closed[endYPosition][endXPosition]){
                //Trace back the path
                //System.out.println("Path: ");
                Square current = map[endYPosition][endXPosition];
                //System.out.print("XPOS: "+current.parent.xPos+" YPOS: "+current.yPos+" TYPE: "+current.type);
                while(current.parent!=null){
                    //System.out.print(" -> XPOS: "+current.parent.xPos+" YPOS: "+current.yPos+" TYPE: "+current.type);
                    toReturn.add(current.parent);
                    current = current.parent;
                }
                System.out.println();
            }else System.out.println("No possible path");

            return toReturn;
        }

    }

*/

    public class AStarNode implements Comparable {

        AStarNode pathParent;
        float costFromStart;
        float estimatedCostToGoal;
        protected int xPos;
        protected int yPos;
        protected Type nodeType;
        protected Type[][] currentMapState;

        public AStarNode(int x, int y, Type type, Type[][] currentMapState){
            this.xPos = x;
            this.yPos = y;
            this.nodeType = type;
            this.currentMapState = currentMapState;

        }


        public float getCost() {
            return costFromStart + estimatedCostToGoal;
        }


        public int compareTo(Object other) {
            float thisValue = this.getCost();
            float otherValue = ((AStarNode)other).getCost();

            float v = thisValue - otherValue;
            return (v>0)?1:(v<0)?-1:0; // sign function
        }


        /**
         Gets the cost between this node and the specified
         adjacent (AKA "neighbor" or "child") node.
         */
        public float getCost(AStarNode node){
            return costFromStart + 1;
        }


        /**
         Gets the estimated cost between this node and the
         specified node. The estimated cost should never exceed
         the true cost. The better the estimate, the more
         effecient the search.
         */
        public float getEstimatedCost(AStarNode node){
            return calculateManhattanDistance(this.xPos, this.yPos, node.xPos, node.yPos);
        }


        /**
         Gets the children (AKA "neighbors" or "adjacent nodes")
         of this node.
         */
        public List getNeighbors(){
            List<AStarNode> toReturn = new ArrayList<AStarNode>();

            if(this.yPos-1 > 0) {
                Type typeOfTheNeighbourNode = this.currentMapState[this.yPos-1][this.xPos];
                toReturn.add(new AStarNode(this.xPos, this.yPos - 1, typeOfTheNeighbourNode, this.currentMapState));
            }

            if(this.xPos-1 > 0) {
                Type typeOfTheNeighbourNode = this.currentMapState[this.yPos][this.xPos-1];
                toReturn.add(new AStarNode(this.xPos-1, this.yPos, typeOfTheNeighbourNode, this.currentMapState));
            }

            if(this.yPos < this.currentMapState.length) {
                Type typeOfTheNeighbourNode = this.currentMapState[this.yPos+1][this.xPos];
                toReturn.add(new AStarNode(this.xPos, this.yPos + 1, typeOfTheNeighbourNode, this.currentMapState));
            }

            if(this.xPos+1 < this.currentMapState[0].length) {
                Type typeOfTheNeighbourNode = this.currentMapState[this.yPos][this.xPos+1];
                toReturn.add(new AStarNode(this.xPos + 1, this.yPos, typeOfTheNeighbourNode, this.currentMapState));
            }



            return toReturn;
        }
    }

    public class AStarSearch {


        public AStarSearch(){

        }
        /**
         A simple priority list, also called a priority queue.
         Objects in the list are ordered by their priority,
         determined by the object's Comparable interface.
         The highest priority item is first in the list.
         */
        public class PriorityList extends LinkedList {

            public void add(Comparable object) {
                for (int i=0; i<size(); i++) {
                    if (object.compareTo(get(i)) <= 0) {
                        add(i, object);
                        return;
                    }
                }
                addLast(object);
            }
        }


        /**
         Construct the path, not including the start node.
         */
        protected List constructPath(AStarNode node) {
            LinkedList path = new LinkedList();
            while (node.pathParent != null) {
                path.addFirst(node);
                node = node.pathParent;
            }
            return path;
        }


        /**
         Find the path from the start node to the end node. A list
         of AStarNodes is returned, or null if the path is not
         found.
         */
        public List findPath(AStarNode startNode, AStarNode goalNode) {

            PriorityList openList = new PriorityList();
            LinkedList<AStarNode> closedList = new LinkedList();

            startNode.costFromStart = 0;
            startNode.estimatedCostToGoal = startNode.getEstimatedCost(goalNode);
//            System.out.println("Estimated cost = "+startNode.estimatedCostToGoal);
            startNode.pathParent = null;
            openList.add(startNode);


            while (!openList.isEmpty()) {
                AStarNode node = (AStarNode)openList.removeFirst();


                if (node == goalNode) {
                    // construct the path from start to goal
                    System.out.println("here");
                    return constructPath(goalNode);
                }

                List neighbors = node.getNeighbors();
                for (int i=0; i<neighbors.size(); i++) {
                    AStarNode neighborNode =
                            (AStarNode)neighbors.get(i);
                    boolean isOpen = openList.contains(neighborNode);
                    boolean isClosed =
                            closedList.contains(neighborNode);
                    float costFromStart = node.costFromStart +
                            node.getCost(neighborNode);


                    // check if the neighbor node has not been
                    // traversed or if a shorter path to this
                    // neighbor node is found.
                    if ((!isOpen && !isClosed) ||
                            costFromStart < neighborNode.costFromStart)
                    {
                        neighborNode.pathParent = node;
                        neighborNode.costFromStart = costFromStart;
                        neighborNode.estimatedCostToGoal = neighborNode.getEstimatedCost(goalNode);
                        if (isClosed) {
                            closedList.remove(neighborNode);
                        }
                        if (!isOpen) {
                            openList.add(neighborNode);
                        }
                    }
                }
                closedList.add(node);

            }

            // no path found
            return null;
        }



    }



    public String getNickname(){
        //TODO change this to any nickname you like. This should not be your uzh_shortname. That way you can stay anonymous on the ranking list.
        return "A/N";
    }
}
