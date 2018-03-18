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

        //if the move is acceptable, then => change the element of the array to true
        //else, leave it false

        // UP => x-1, y
        //RIGHT => x, y+1
        //DOWN => x+1, y
        //LEFT => x, y-1

        if(isSafe(map, x-1, y))
            toReturn[0] = true;
        if(isSafe(map, x, y+1))
            toReturn[1] = true;
        if(isSafe(map, x+1, y))
            toReturn[2] = true;
        if(isSafe(map, x, y-1))
            toReturn[3] = true;

        //bounderies of the map
        //UP
        if(x-1 == -1)
            toReturn[0] = false;
        //DOWN
        if(y+1 == map[0].length)
            toReturn[1] = false;
        //RIGHT
        if(x+1 == map.length)
            toReturn[2] = false;
        //left
        if(y-1 == -1)
            toReturn[3] = false;


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
		if(checkAdjcentSquares(map)[2])
            move = Move.DOWN;
		else
		    System.out.println("NOT SAFE TO MOVE DOWN");
        move = Move.DOWN;
    }
}
