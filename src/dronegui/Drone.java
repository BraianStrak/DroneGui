package dronegui;

import javafx.scene.image.Image;

import java.util.Random;

public class Drone extends Entity{
    protected double mAngle, mSpeed;

    /**
     * Default constructor for the drone class, this is never used
     */
    public Drone(){

    }

    /**
     * Constructor for the drone class, this sets up the basic functionality of drone and sets up all of its parameters,
     * including the x and y positions, the image, the angle and the speed it is to move at
     * @param x     double  x position in the range of 0 .. canvasSize, passed in when user clicks the canvas
     * @param y     double  y position in the range of 0 .. canvasSize, passed in when user clicks the canvas
     */
    public Drone(double x, double y) {
        GlobalEntityId++; //increment global ID
        mId = GlobalEntityId; //set this entity ID
        mX = x; //set x and y positions which were passed through
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("drone.png")); //set image
        mRadius = 25; //set size
        Random random = new Random();
        mAngle = random.nextInt(360); //set random starting angle
        mSpeed = 2; //set speed
    }

    /**
     * getter for the angle parameter
     * @return the value mAngle
     */
    public double getAngle(){
        return mAngle; //return angle
    }

    /**
     * setter for the angle parameter
     * @param angle     double  angle which the drone moves at
     */
    public void setAngle(double angle){
        mAngle = angle; // set the angle with the value which was passed in
    }

    /**
     * returns a formatted string with the information about the drone
     * @return the formatted string with the drone ID, x and y position
     */
    public String toString(){
        //create and return the string using the ID, x and y position.
        return String.format("Entity %d (Drone) is at %.1f, %.1f", mId, mX, mY);
    }

    /**
     * this checks wall collision for the drone, if the drone is hitting the wall then the angle becomes the opposite
     * of the previous angle.
     * @param mc    MyCanvas    this is used to fetch the arena size for the drone collision
     */
    public void checkDrone(MyCanvas mc) {
        if (mX < mRadius || mX > mc.getXCanvasSize() - mRadius) mAngle = 180 - mAngle;
        // if the position is less than the radius of the drone or if drone hits a wall
        // if drone hit (tried to go through) left or right walls, set mirror angle, being 180-angle
        if (mY < mRadius || mY > mc.getYCanvasSize() - mRadius) mAngle = -mAngle;
        // if drone hit (tried to go through) top or bottom walls, set mirror angle, being -angle
    }

    /**
     * This method gets the angle and converts it into an actual direction which the drone can go in
     */
    public void adjustDrone() {
        double radAngle = mAngle*Math.PI/180;	// put angle in radians
        mX += mSpeed * Math.cos(radAngle);		// new X position
        mY += mSpeed * Math.sin(radAngle);		// new Y position
    }

    /**
     * This method handles collision, when the drone gets passed in, if both the drones intersect, they each set their
     * angle to be a random angle between 1 and 360.
     * @param e     Drone   this is the individual drone which gets passed in and the intersection between this and drone e is checked
     */
    public void interact(Drone e){
        Random random = new Random();
        if (!this.equals(e) && this.getBounds().intersects(e.getBounds())) {
            this.setAngle(random.nextInt(360));
            e.setAngle(random.nextInt(360));
        }
    }
}
