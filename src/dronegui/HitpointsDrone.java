package dronegui;

import javafx.scene.image.Image;

import java.util.Random;

public class HitpointsDrone extends Drone {
    private int mHitPoints;

    /**
     * This constructor is called whenever the HP drone is loaded into the arena through clicking
     * @param x the specified x position of the drone
     * @param y the specified y position of the drone
     */
    public HitpointsDrone(double x, double y){
        GlobalEntityId++;
        mId = GlobalEntityId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("hpdrone.png"));
        mSpeed = 2;
        mRadius = 25;
        mHitPoints = 100;
    }

    /**
     * This constructor is called whenever the HP drone is loaded in from a file
     * @param x the specified x position
     * @param y the specified y position
     * @param hp the health of the drone when the game was saved
     */
    public HitpointsDrone(double x, double y, int hp){
        GlobalEntityId++;
        mId = GlobalEntityId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("hpdrone.png"));
        mSpeed = 2;
        mRadius = 25;
        mHitPoints = hp;
    }

    /**
     * the getter for the hitpoints of the drone
     * @return the mHitpoints variable
     */
    public int getHitPoints(){
        return mHitPoints;
    }

    /**
     * creates a string with information about the drone
     * @return a formatted string with the id, x, y, and hitpoints
     */
    public String toString(){
        return String.format("Entity %d (HP Drone) is at %.1f, %.1f, HP %d", mId, mX, mY, mHitPoints);
    }

    /**
     * the interact method which handles collision, same as drone except health gets lowered when it collides
     * @param e     Drone   this is the individual drone which gets passed in and the intersection between this and drone e is checked
     */
    public void interact(Drone e){
        Random random = new Random();
        if (!this.equals(e) && this.getBounds().intersects(e.getBounds())) { //if this drone intersects the passed one
            mHitPoints -= random.nextInt(20); //set new health value
            this.setAngle(random.nextInt(360)); //set new angle for both drones
            e.setAngle(random.nextInt(360));
        }
    }
}
