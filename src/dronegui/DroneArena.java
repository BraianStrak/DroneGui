package dronegui;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Random;

public class DroneArena {
    private ArrayList<Drone> mDrones;
    private ArrayList<Wall> mWalls;
    private PlayerDrone mPlayer;

    public DroneArena(){
        mDrones = new ArrayList<>();
        mWalls = new ArrayList<>();
    }

    public PlayerDrone getPlayer(){
        return mPlayer;
    }

    public void setArena(MyCanvas mc, double x, double y, String entityFlag) {
        switch (entityFlag){
            case "drone":
                mDrones.add(new Drone(x, y)); break;
            case "predator":
                mDrones.add(new PredatorDrone(x, y)); break;
            case "wall":
                mWalls.add(new Wall(x, y)); break;
            case "player":
                if(mPlayer == null) {
                    mPlayer = new PlayerDrone(x, y);
                } else {
                    mPlayer.setXPosition(x);
                    mPlayer.setYPosition(y);
                }
            default:
                break;
        }
    }

    public void drawArena(MyCanvas mc){
        mc.clearCanvas();

        if(mPlayer != null){
            mc.drawImage(mPlayer.getImage(), mPlayer.getXPosition(), mPlayer.getYPosition(), mPlayer.getSize());
        }

        if(mDrones != null) {
            for (Drone d : mDrones) {
                mc.drawImage(d.getImage(), d.getXPosition(), d.getYPosition(), d.getSize());
            }
        }

        if(mWalls != null) {
            for (Wall w : mWalls) {
                mc.drawImage(w.getImage(), w.getXPosition(), w.getYPosition(), w.getSize());
            }
        }
    }

    public void updateArena(MyCanvas mc){

        Random random = new Random();
        if(mDrones != null) {
            for (Drone d : mDrones) {
                //if the drone is a predator
                if (d.getClass() == PredatorDrone.class && mPlayer != null) {
                    ((PredatorDrone) d).setHasTarget(true);
                    ((PredatorDrone) d).setTarget(mPlayer);
                    d.interact(mPlayer);
                    if(mPlayer.isAlive() == false){
                        mPlayer = null;
                    }
                } else if (d.getClass() == PredatorDrone.class && mPlayer == null) {
                    for (Drone e : mDrones) {
                        d.interact(e);
                    }
                }

                if (mDrones != null) {
                    for (Drone e : mDrones) { //collision detection
                        if(e.getClass() != PredatorDrone.class) {
                            d.interact(e);
                        }
                    }
                }

                if (mWalls != null && d != null) {
                    for (Wall e : mWalls) {
                        e.interact(d);
                    }
                }

                //put move method on player here

                d.checkDrone(mc);
                d.adjustDrone();
            }
        }
    }

    public String toString(){
        //returns positions of all drones
        String append = new String();
        if(mDrones != null) {
            for (Drone d : mDrones) {
                append += d.toString() + "\n";
            }
        }
        if(mWalls != null) {
            for (Wall w : mWalls) {
                append += w.toString() + "\n";
            }
        }
        if(mPlayer != null) {
            append += mPlayer.toString();
        }
        return append;
    }

    public void pressedKey(KeyEvent e, MyCanvas mc) {
        KeyCode code = e.getCode();
        if(code == KeyCode.UP) { //if key pressed was up
            if(mPlayer.getYPosition() > 0 + mPlayer.getSize()){ //if it doesn't move out of arena bounds
                if(mPlayer.tryToMove(mWalls, "up")) { //if it doesn't intersect with a wall
                    mPlayer.setYPosition(mPlayer.getYPosition() - 5); //let it move
                }
            }
        } else if (code == KeyCode.DOWN){
            if(mPlayer.getYPosition() < mc.getYCanvasSize() - mPlayer.getSize()) {
                if(mPlayer.tryToMove(mWalls, "down")) {
                    mPlayer.setYPosition(mPlayer.getYPosition() + 5);
                }
            }
        } else if (code == KeyCode.LEFT) {
            if(mPlayer.getXPosition() > 0 + mPlayer.getSize()) {
                if(mPlayer.tryToMove(mWalls, "left")) {
                    mPlayer.setXPosition(mPlayer.getXPosition() - 5);
                }
            }
        } else if (code == KeyCode.RIGHT) {
            if(mPlayer.getXPosition() < mc.getXCanvasSize() - mPlayer.getSize()) {
                if(mPlayer.tryToMove(mWalls, "right")) {
                    mPlayer.setXPosition(mPlayer.getXPosition() + 5);
                }
            }
        }
    }

    public void save(){
        //ask what to call the filename to save.
        //for walls and player, just save, put nothing underneath heading if one doesn't exist.
        //for drones, you need to specify the type of drone when saving, so they need an additional item.
        //save positions, ID, type and rest can be loaded.

        //could have a save method in each drone and call it here maybe.
    }

    public void load(){
        //ask what filename to load, create error if doesn't exist
        //if exists, go through mDrones, mWalls and mPlayer, only need to save positions AND TYPE (for drones)
        //with a delimiter
        //When you load, DELETE ALL ENTITIES FIRST.
        //when you load add to each separate array.
    }
}
