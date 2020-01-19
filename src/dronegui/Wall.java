package dronegui;

import javafx.scene.image.Image;

public class Wall extends Entity {

    public Wall (){ }

    public Wall(double x, double y) {
        GlobalEntityId++;
        mId = GlobalEntityId;
        mX = x;
        mY = y;
        mImage = new Image(getClass().getResourceAsStream("tree.png"));
        mRadius = 50;
    }

    public String toString(){
        return String.format("Entity %d (Tree) is at %.1f, %.1f", mId, mX, mY);
    }

    public void interact(Drone e) {
        if (e.getBounds().intersects(this.getBounds())) {
            e.setAngle(e.getAngle() - 180);
        }
    }
}
