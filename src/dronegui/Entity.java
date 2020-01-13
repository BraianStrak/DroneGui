package dronegui;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public abstract class Entity {
    protected Image mImage;
    protected double mX, mY;
    protected static int GlobalEntityId;
    protected int mId;
    protected double mRadius;

    public double getXPosition() {
        return mX;
    }

    public double getYPosition() {
        return mY;
    }

    public void setXPosition(double x){
        mX = x;
    }

    public void setYPosition(double y){
        mY = y;
    }

    public Rectangle2D getBounds() {
        return new Rectangle2D(mX, mY, mRadius, mRadius);
    }

    public abstract String toString();

    public Image getImage(){
        return mImage;
    }

    public double getSize(){
        return mRadius;
    }
}
