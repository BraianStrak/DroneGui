package dronegui;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

public abstract class Entity {
    protected Image mImage; //image of the entity
    protected double mX, mY; //positions of the entity
    protected static int GlobalEntityId; //global entity identifier
    protected int mId; //current entity identifier
    protected double mRadius; //entity size

    /**
     * getter which returns the x position
     */
    public double getXPosition() {
        return mX;
    }

    /**
     * getter which returns the y position
     */
    public double getYPosition() {
        return mY;
    }

    /**
     * setter which changes the x position
     * @param x     double  the x position to be changed to
     */
    public void setXPosition(double x){
        mX = x;
    }

    /**
     * setter which changes the y position
     * @param y     double  the y position to be changed to
     */
    public void setYPosition(double y){
        mY = y;
    }

    /**
     * setter which changes the entity ID
     * @param id     int  the ID to be set to
     */
    public void setGlobalEntityId(int id){
        GlobalEntityId = id;
    }

    /**
     * returns a rectangle the exact size of the entity, used in collision detection
     */
    public Rectangle2D getBounds() {
        return new Rectangle2D(mX, mY, mRadius, mRadius);
    }

    /**
     * @param x the future x position
     * @param y the future y position
     * @return returns the rectangle bounds
     */
    public Rectangle2D getFutureBounds(double x, double y){
        return new Rectangle2D(x, y, mRadius, mRadius);
    }

    /**
     * this method is used by all subclasses
     * @return returns the string with information regarding the entity
     */
    public abstract String toString();

    /**
     * A getter for the image of the entity
     * @return the image of the entity
     */
    public Image getImage(){
        return mImage;
    }

    /**
     * a getter for the size of the entity
     * @return the size of the entity
     */
    public double getSize(){
        return mRadius;
    }
}
