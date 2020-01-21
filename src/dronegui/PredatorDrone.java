package dronegui;

import javafx.scene.image.Image;

import java.util.Random;

public class PredatorDrone extends Drone {
    private PlayerDrone mTarget;
    private boolean hasTarget = false;

    /**
     * the only constructor for the PredatorDrone class, sets all of the variables up.
     * @param x the specified x position
     * @param y the specified y posttion
     */
    public PredatorDrone(double x, double y){
        GlobalEntityId++;
        mId = GlobalEntityId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("predator.png"));
        mRadius = 30;
        Random random = new Random();
        mAngle = random.nextInt(360);
        mSpeed = 1.6;
    }

    /**
     * getter for the mTarget variable
     * @return the value of mTarget
     */
    public Drone getTarget() {
        return mTarget;
    }

    /**
     * setter for the mTarget variable
     * @param pd the playerdrone which is to be the target
     */
    public void setTarget(PlayerDrone pd){
        mTarget = pd;
    }

    /**
     * setter for the hasTarget boolean
     * @param hasTarget the value of hasTarget
     */
    public void setHasTarget(boolean hasTarget){
        this.hasTarget = hasTarget;
    }

    /**
     * formats a string with information about the drone
     * @return a formatted string with the ID, x and y positions of the drone
     */
    public String toString(){
        return String.format("Entity %d (Predator) is at %.1f, %.1f", mId, mX, mY);
    }

    /**
     * this interact method moves towards the target drone if the predator has a target, and acts as a normal drone if it does not
     * @param e     Drone   this is the individual drone which gets passed in and the predatordrone chases
     */
    public void interact(Drone e){
            if(hasTarget){ //if the drone has a target
                //angle towards the target drone
                mAngle = Math.toDegrees(Math.atan2(this.mY - mTarget.getYPosition(), this.mX - mTarget.getXPosition()) * 180 / Math.PI);

                //if this drone is close to another then kill it
                if (this.getBounds().intersects(mTarget.getBounds())) {
                    mTarget.setAlive(false);
                    mTarget = null;
                    hasTarget = false;
                }
            } else {
                super.interact(e); //if there is no target then behave like a normal drone.
            }
    }
}
