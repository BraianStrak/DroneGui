package dronegui;

import javafx.scene.image.Image;
import java.util.ArrayList;

public class PlayerDrone extends Drone {

    //there can only be one instance of playerdrone in the arena.

    private boolean mAlive;

    /**
     * the only constructor used for PlayerDrone, initializes all of the variables
     * @param x the specified x position
     * @param y the specified y position
     */
    public PlayerDrone(double x, double y){
        GlobalEntityId++;
        mId = GlobalEntityId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("playerdrone.png"));
        mSpeed = 0.5;
        mRadius = 25;
        mAlive = true;
    }

    /**
     * the getter for the mAlive variable
     * @return the mAlive variable
     */
    public boolean isAlive() {
        return mAlive;
    }

    /**
     * the setter for the mAlive variable
     * @param mAlive the specified value which mAlive is to be set to
     */
    public void setAlive(boolean mAlive) {
        this.mAlive = mAlive;
    }

    /**
     * returns a string with information about the drone
     * @return returns a string with contains the ID, x and y positions of the drone
     */
    public String toString(){
        return String.format("Entity %d (Player) is at %.1f, %.1f", mId, mX, mY);
    }

    /**
     * the movement method for the drone, it handles movement and does not allow the player to move through walls. if
     * the player is not about to hit a wall in the next movement, then the position gets changed by five.
     * @param walls an arraylist of all walls in the arena to compare against
     * @param direction //the direction is the key which was pressed so that we know which future position to check.
     * @return returns whether the drone can move or not
     */
    public boolean tryToMove(ArrayList<Wall> walls, String direction){ //checks if it intersects with walls
        double tempX = mX; //creates temporary values used to check the future positions of the drone.
        double tempY = mY;
        boolean canMove = true; //return value
        if(walls != null){ //if walls are in the arena
            switch(direction){
                case "up": //set next provisional movement position
                    tempY -= 5;
                    break;
                case "down":
                    tempY += 5;
                    break;
                case "left":
                    tempX -= 5;
                    break;
                case "right":
                    tempX += 5;
                    break;
                default:
            }
            for(Wall w: walls){ //if the movement intersects with any walls
                if(getFutureBounds(tempX, tempY).intersects(w.getBounds())){
                    canMove = false; //set that the player can not move.
                }
            }
        }
        return canMove;
    }
}
