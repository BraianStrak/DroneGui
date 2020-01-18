package dronegui;

import javafx.scene.image.Image;

import java.util.Random;

public class PredatorDrone extends Drone {
    private PlayerDrone mTarget;
    private boolean hasTarget = false;

    public PredatorDrone(double x, double y){
        GlobalEntityId++;
        mId = GlobalEntityId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("predator.png"));
        mRadius = 30;
        Random random = new Random();
        mAngle = random.nextInt(360);
        mSpeed = 0.4;
    }

    public Drone getTarget() {
        return mTarget;
    }

    public void setTarget(PlayerDrone pd){
        mTarget = pd;
    }

    public void setHasTarget(boolean hasTarget){
        this.hasTarget = hasTarget;
    }

    public boolean getHasTarget(){
        return hasTarget;
    }

    public String toString(){
        return String.format("Entity %d (Predator) is at %.1f, %.1f", mId, mX, mY);
    }

    public void interact(Drone e){
            if(hasTarget){
                //angle towards the closest drone
                mAngle = Math.toDegrees(Math.atan2(this.mY - mTarget.getYPosition(), this.mX - mTarget.getXPosition()) * 180 / Math.PI);

                //if this drone is close to another then kill it
                if (this.getBounds().intersects(mTarget.getBounds())) {
                    mTarget.setAlive(false);
                    mTarget = null;
                    hasTarget = false;
                }
            } else {
                super.interact(e);
            }
    }
}
