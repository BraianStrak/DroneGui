package dronegui;

import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;


public class DroneArena {
    private ArrayList<Drone> mDrones;
    private ArrayList<Wall> mWalls;
    private PlayerDrone mPlayer;

    public DroneArena(){
        mDrones = new ArrayList<>();
        mWalls = new ArrayList<>();

        //providing three default drones
        mDrones.add(new Drone(100, 100));
        mDrones.add(new Drone(250, 250));
        mDrones.add(new Drone(350, 200));
    }

    public PlayerDrone getPlayer(){
        return mPlayer;
    }

    public void setArena(MyCanvas mc, double x, double y, String entityFlag) {
        switch (entityFlag){
            case "drone":
                mDrones.add(new Drone(x, y)); break;
            case "hitpointsdrone":
                mDrones.add(new HitpointsDrone(x, y)); break;
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
        Image image = new Image(getClass().getResourceAsStream("background.png"));

        mc.drawImage(image, mc.getXCanvasSize()/2, mc.getYCanvasSize()/2, mc.getYCanvasSize());

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

            for (Drone e : mDrones) { //collision detection
                if(e.getClass() != PredatorDrone.class && e != null) {
                    d.interact(e);
                }
            }

            for (Wall e : mWalls) { //if its a wall
                e.interact(d);
            }

            d.checkDrone(mc);
            d.adjustDrone();
        }


        //need to go over to look for drones with <0 HP with an iterator because you cannot remove drones
        //from an arraylist without a concurrentModificationException
        Iterator<Drone> iter = mDrones.iterator();
        while(iter.hasNext()){
            Drone d = iter.next();
            if(d.getClass() == HitpointsDrone.class && ((HitpointsDrone) d).getHitPoints() <= 0){
                iter.remove();
            }
        }
    }

    public String toString(){
        //returns positions of all drones
        String append = new String();
        append += "Entity Information: " + "\n";
        for(int i = 0; i<46; i++){
            append += "_";
        }
        append += "\n";
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

    public void save(String fileName){
        //save method from last time
        //ask for file name which you wish to call it using a text dialogue
        File arenaFile = new File(fileName);

        try{
            FileWriter outFileWriter = new FileWriter(arenaFile);
            PrintWriter fw = new PrintWriter(outFileWriter); //allows to print to file

            for(Drone d : mDrones){
                if(d.getClass() == Drone.class) {
                    fw.print("DRONE" + " " + d.getXPosition() + " " + d.getYPosition() + " ");
                } else if (d.getClass() == PredatorDrone.class){
                    fw.print("PREDATOR" + " " + d.getXPosition() + " " + d.getYPosition() + " ");
                } else if (d.getClass() == HitpointsDrone.class){
                    fw.print("HPDRONE" + " " + d.getXPosition() + " " + d.getYPosition() + " "
                            + ((HitpointsDrone) d).getHitPoints() + " ");
                }
            }
            for(Wall d : mWalls){
                fw.print("WALL" + " " + d.getXPosition() + " " + d.getYPosition() + " ");
            }
            if(mPlayer != null){
                fw.print("PLAYER" + " " + mPlayer.getXPosition() + " " + mPlayer.getYPosition() + " ");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearArena(){
        //clear all of the current arena.
        mDrones.clear();
        mWalls.clear();
        mPlayer = null;
    }

    public void load(String fileName){
        String[] strArray;
        double tempX, tempY;
        int tempHP;

        clearArena();

        //create reader for the file.
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(fileName)); //makes file reader
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String str=scanner.nextLine(); //read the file
        strArray=str.split(" "); //split it into an array

        for(int i = 0; i < strArray.length; i++){
            switch(strArray[i]){
                case "DRONE":
                    //go through next two locations, use them to create a drone then leave it as it goes on to the next one
                    i++; tempX = parseDouble(strArray[i]);//convert to double
                    i++; tempY = parseDouble(strArray[i]);
                    mDrones.add(new Drone(tempX, tempY));
                    break;
                case "PREDATOR":
                    //go through next two locations, use them to create a predatordrone
                    i++; tempX = parseDouble(strArray[i]);
                    i++; tempY = parseDouble(strArray[i]);
                    mDrones.add(new PredatorDrone(tempX, tempY));
                    break;
                case "HPDRONE":
                    //go through next THREE locations, use to create hpdrone with other constructor
                    i++; tempX = parseDouble(strArray[i]);
                    i++; tempY = parseDouble(strArray[i]);
                    i++; tempHP = parseInt(strArray[i]);
                    mDrones.add(new HitpointsDrone(tempX, tempY, tempHP));
                    break;
                case "WALL":
                    //go through next two locations, use them to create a wall
                    i++; tempX = parseDouble(strArray[i]);
                    i++; tempY = parseDouble(strArray[i]);
                    mWalls.add(new Wall(tempX, tempY));
                    break;
                case "PLAYER":
                    i++; tempX = parseDouble(strArray[i]);
                    i++; tempY = parseDouble(strArray[i]);
                    mPlayer = new PlayerDrone(tempX, tempY);
                default:
                    break;
            }
        } //end of for loop
    }
}
