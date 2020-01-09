package dronegui;

import javafx.scene.image.Image;

import java.util.Random;

public class Drone {
    private Image mImage;
    private double mDroneSize;
    private double mX, mY;
    public static int GlobalDroneId;
    public int mId;
    private double mRad, mAngle, mSpeed;

    public Drone(double x, double y) {
        GlobalDroneId++;
        mId = GlobalDroneId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("drone.png"));
        mRad = 20;
        Random random = new Random();
        mAngle = random.nextInt(360);
        mSpeed = 1;
        mDroneSize = 25;
    }

    public Image getImage(){
        return mImage;
    }

    public double getXPosition() {
        return mX;
    }

    public double getYPosition() {
        return mY;
    }

    public double getSize(){
        return mDroneSize;
    }

    public void setXPosition(double x){
        mX = x;
    }

    public void setYPosition(double y){
        mY = y;
    }

    public String toString(){
        return String.format("Drone %d is at %.1f, %.1f", mId, mX, mY);
    }

    public void checkDrone(MyCanvas mc) {
        if (mX < mRad || mX > mc.getXCanvasSize() - mRad) mAngle = 180 - mAngle;
        // if drone hit (tried to go through) left or right walls, set mirror angle, being 180-angle
        if (mY < mRad || mY > mc.getYCanvasSize() - mRad) mAngle = -mAngle;
        // if drone hit (tried to go through) top or bottom walls, set mirror angle, being -angle
    }

    public void adjustDrone() {
        double radAngle = mAngle*Math.PI/180;	// put angle in radians
        mX += mSpeed * Math.cos(radAngle);		// new X position
        mY += mSpeed * Math.sin(radAngle);		// new Y position
    }


}
