package dronegui;

import java.util.ArrayList;
import java.util.Random;

public class DroneArena {
    private ArrayList<Drone> mDrones;
    private ArrayList<Wall> mWalls;

    public DroneArena(){
        mDrones = new ArrayList<>();
        mWalls = new ArrayList<>();
    }

    public void setArena(MyCanvas mc, double x, double y, String entityFlag) {
        switch (entityFlag){
            case "drone":
                mDrones.add(new Drone(x, y)); break;
            case "predator":
                mDrones.add(new PredatorDrone(x, y)); break;
            case "wall":
                mWalls.add(new Wall(x, y)); break;
            default:
                break;
        }
    }

    public void drawArena(MyCanvas mc){ //this is bugged a bit
        mc.clearCanvas();

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
        //call check and adjust on all drones
        for(Drone d: mDrones){
            if(d.getAlive() == false){
                mDrones.remove(d);
            }

            if(mDrones != null && d != null) {
                for (Drone e : mDrones) { //collision detection
                    d.interact(e);
                }
            }

            if(mWalls != null && d != null) {
                for (Wall w : mWalls) {
                    if (d.getBounds().intersects(w.getBounds())) {
                        d.setAngle(d.getAngle() - 180);
                    }
                }
            }

            d.checkDrone(mc);
            d.adjustDrone();
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
        return append;
    }
}
