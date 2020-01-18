package dronegui;

import javafx.scene.image.Image;
import java.util.ArrayList;

public class PlayerDrone extends Drone {

    //there can only be one instance of playerdrone in the arena.

    private boolean mAlive;

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

    public boolean isAlive() {
        return mAlive;
    }

    public void setAlive(boolean mAlive) {
        this.mAlive = mAlive;
    }

    public String toString(){
        return String.format("Entity %d (Player) is at %.1f, %.1f", mId, mX, mY);
    }

    public boolean tryToMove(ArrayList<Wall> walls, String direction){ //checks if it intersects with walls
        double tempX = mX;
        double tempY = mY;
        boolean canMove = true;
        if(walls != null){
            switch(direction){
                case "up":
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
            for(Wall w: walls){
                if(getFutureBounds(tempX, tempY).intersects(w.getBounds())){
                    canMove = false;
                }
            }
        }
        return canMove;
    }
}
