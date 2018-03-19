package kingsheep.team.anecul;

import kingsheep.*;

public class Sheep extends UzhShortNameCreature {


    public Sheep(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);

    }

    /*
       1) This method checks if the squares adjcent to the sheep/wolf can be a valid move, returns all valid moves
       0 = UP
       1 = RIGHT
       2 = DOWN
       3 = LEFT

     */
    protected boolean[] checkAdjcentSquares(Type map[][]){



        //create the return array
        boolean[] toReturn = new boolean[4];
        //initialize the return array with false
        toReturn[0] = toReturn[1] = toReturn[2] = toReturn[3] = false;

        //bounderies of the map
//        //UP
//        if(x-1 < 0)
//            toReturn[0] = false;
//        //DOWN
//        if(y+1 == map[0].length)
//            toReturn[1] = false;
//        //RIGHT
//        if(x+1 == map.length)
//            toReturn[2] = false;
//        //left
//        if(y-1 < 0)
//            toReturn[3] = false;


        //if the move is acceptable, then => change the element of the array to true
        //else, leave it false

        // UP => x-1, y => y-1
        //RIGHT => x, y+1 => x+1
        //DOWN => x+1, y => y+1
        //LEFT => x, y-1 => x-1

        if(x-1 >= 0 && isSafe(map, x-1, y))
            toReturn[0] = true;
        if(y+1 < map[0].length && isSafe(map, x, y+1))
            toReturn[1] = true;
        if(x+1 < map.length && isSafe(map, x+1, y))
            toReturn[2] = true;
        if(y-1 >= 0 && isSafe(map, x, y-1))
            toReturn[3] = true;



        return toReturn;
    }

     //check if a particular square is safe, i.e. no wolf, sheep, fence is on that square
    protected boolean isSafe(Type[][] map, int x, int y){

        Type type = map[x][y];

        if(type == type.FENCE || type == type.WOLF2 || type == type.SHEEP2 || type == type.WOLF1)
            return false;

        return true;
    }


    protected void think(Type map[][]) {

        //Set the objectives for the sheep
        char objectives[] = new char[2];
        objectives[0] = 'g';
        objectives[1] = 'r';

        System.out.println("UP: "+checkAdjcentSquares(map)[0] + "\tRIGHT: "+checkAdjcentSquares(map)[1]+ "\tDOWN: "+checkAdjcentSquares(map)[2]+ "\tLEFT: "+checkAdjcentSquares(map)[3]);

		/*
		TODO
		YOUR SHEEP CODE HERE
		
		BASE YOUR LOGIC ON THE INFORMATION FROM THE ARGUMENT map[][]
		
		YOUR CODE NEED TO BE DETERMINISTIC. 
		THAT MEANS, GIVEN A DETERMINISTIC OPPONENT AND MAP THE ACTIONS OF YOUR SHEEP HAVE TO BE REPRODUCIBLE
		
		SET THE MOVE VARIABLE TO ONE TOF THE 5 VALUES
        move = Move.UP;
        move = Move.DOWN;
        move = Move.LEFT;
        move = Move.RIGHT;
        move = Move.WAIT;
		*/
		try {
            if (checkAdjcentSquares(map)[3])
                move = Move.LEFT;
            else
                System.out.println("NOT SAFE TO MOVE UP");
            //move = Move.UP;
        }catch(Exception ex){
		    //Don't go outside the map
            //System.out.println("Don't go outside the map");
        }
    }
}
