package dronegui;

import javafx.scene.image.Image;

import java.util.Random;

public class PredatorDrone extends Drone {
    private Drone mClosestDrone;
    private double mClosestDroneDistance;
    private boolean mHasPrey;

    public PredatorDrone(double x, double y){
        GlobalEntityId++;
        mId = GlobalEntityId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("predator.png"));
        mRadius = 30;
        mAlive = true;
        Random random = new Random();
        mAngle = random.nextInt(360);
        mSpeed = 0.6;
        mIsPrey = false;
    }
    //have new methods but override the interact method to put everything together. This way dronearena works properly.

    public String toString(){
        return String.format("Entity %d (Predator) is at %.1f, %.1f", mId, mX, mY);
    }

    //goes towards dead prey, this is because the closest drone doesn't get changed after it dies
    public void interact(Drone e){

        if(e.isPrey()) {
            //get the closest target, after that, query for targets within e.g. 50 pixels and if theres one that close then go for that one.
            double currentDroneDistance;

            //after it dies i'm checking for null but it's not null because it gets removed in the Arena but not here
            //if there isn't a closest drone set the current to be the closest
            if (mClosestDrone == null && e != null) {
                mClosestDrone = e;
            }

            //calculate distance to drone
            currentDroneDistance = Math.sqrt(Math.pow(e.getXPosition(), mX) + Math.pow(e.getYPosition(), mY));
            //calculate distance to current drone
            mClosestDroneDistance = Math.sqrt(Math.pow(mClosestDrone.getXPosition(), mX)
                    + Math.pow(mClosestDrone.getYPosition(), mY));

            //if the other drone is closer
            if (currentDroneDistance < mClosestDroneDistance) {
                //set it to be the closest drone
                mClosestDrone = e;
            }

            //move towards the closest drone
            mAngle = Math.toDegrees(Math.atan2(this.mY - mClosestDrone.getYPosition(), this.mX - mClosestDrone.getXPosition()) * 180 / Math.PI);

            //if this drone is close to another then kill it
            if (!this.equals(mClosestDrone) && this.getBounds().intersects(mClosestDrone.getBounds())) {
                mClosestDrone.setAlive(false);
                mClosestDrone = null;
            }
        } else if (mClosestDrone == null){
            super.interact(e);
        }
        //later on check if is prey, if yes kill if not then bounce into it. Prey needs to move away from walls too.
    }
}
