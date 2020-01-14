package dronegui;

import javafx.scene.image.Image;

import java.util.Random;

public class Drone extends Entity{
    private double mAngle, mSpeed;

    public Drone(double x, double y) {
        GlobalEntityId++;
        mId = GlobalEntityId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("drone.png"));
        mRadius = 25;
        Random random = new Random();
        mAngle = random.nextInt(360);
        mSpeed = 1;
    }

    public double getAngle(){
        return mAngle;
    }

    public void setAngle(double angle){
        mAngle = angle;
    }

    public String toString(){
        return String.format("Entity %d (Drone) is at %.1f, %.1f", mId, mX, mY);
    }

    public void checkDrone(MyCanvas mc) {
        if (mX < mRadius || mX > mc.getXCanvasSize() - mRadius) mAngle = 180 - mAngle;
        // if the position is less than the radius of the drone or if drone hits a wall
        // if drone hit (tried to go through) left or right walls, set mirror angle, being 180-angle
        if (mY < mRadius || mY > mc.getYCanvasSize() - mRadius) mAngle = -mAngle;
        // if drone hit (tried to go through) top or bottom walls, set mirror angle, being -angle
    }

    public void adjustDrone() {
        double radAngle = mAngle*Math.PI/180;	// put angle in radians
        mX += mSpeed * Math.cos(radAngle);		// new X position
        mY += mSpeed * Math.sin(radAngle);		// new Y position
    }

    public void interactWithDrone(Drone e){ //just do this to change the drone's behaviour
        Random random = new Random();
        if (!this.equals(e) && this.getBounds().intersects(e.getBounds())) {
            this.setAngle(random.nextInt( 360));
            e.setAngle(random.nextInt(360));
        }
    }
}
