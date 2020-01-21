package dronegui;

import javafx.scene.image.Image;

public class Wall extends Entity {

    /**
     * default constructor for the wall object
     */
    public Wall (){ }

    /**
     * the positional constructor of the wall object
     * @param x the specified x position
     * @param y the specified y position
     */
    public Wall(double x, double y) {
        GlobalEntityId++;
        mId = GlobalEntityId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("tree.png"));
        mRadius = 50;
    }

    /**
     * returns a formatted string with information about the entity
     * @return a string with the ID, x and y positions of the entity
     */
    public String toString(){
        return String.format("Entity %d (Tree) is at %.1f, %.1f", mId, mX, mY);
    }

    /**
     * this interact method calls interact on the drone which collides with the wall
     * @param e the drone which we are checking if it collides with the wall
     */
    public void interact(Drone e) {
        if (e.getBounds().intersects(this.getBounds())) {
            e.setAngle(e.getAngle() - 180);
        }
    }
}
