package dronegui;


import java.util.ArrayList;

public class DroneArena {
    private ArrayList<Drone> mDrones;

    public DroneArena(){
        mDrones = new ArrayList<>();
    }

    public void setArena(MyCanvas mc, double x, double y){
        mDrones.add(new Drone(x, y));
    }

    public void drawArena(MyCanvas mc){
        mc.clearCanvas();

        for(Drone d: mDrones){
            mc.drawImage(d.getImage(), d.getXPosition(), d.getYPosition(), d.getSize());
        }
    }


    public void updateArena(MyCanvas mc){
        //call check and adjust on all drones
        for(Drone d: mDrones){
            d.checkDrone(mc);
            d.adjustDrone();
        }
    }

    public String toString(){
        //returns positions of all drones
        String append = new String();
        for(Drone d: mDrones){
            append += d.toString() + "\n";
        }
        return append;
    }

}