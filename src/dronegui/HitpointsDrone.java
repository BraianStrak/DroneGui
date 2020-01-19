package dronegui;

import javafx.scene.image.Image;

import java.util.Random;

public class HitpointsDrone extends Drone {
    private int mHitPoints;

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

    public int getHitPoints(){
        return mHitPoints;
    }

    public String toString(){
        return String.format("Entity %d (HP Drone) is at %.1f, %.1f, HP %d", mId, mX, mY, mHitPoints);
    }

    public void interact(Drone e){
        Random random = new Random();
        if (!this.equals(e) && this.getBounds().intersects(e.getBounds())) {
            mHitPoints -= random.nextInt(20);
            this.setAngle(random.nextInt(360));
            e.setAngle(random.nextInt(360));
        }
    }
}
