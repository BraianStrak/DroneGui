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
    private ArrayList<Drone> mDrones; //array of all the drones
    private ArrayList<Wall> mWalls; //array of all the walls
    private PlayerDrone mPlayer; //the player variable

    /**
     * This method initialises both arrayLists as well as adds three drones which are in the arena by default
     */
    public DroneArena(){
        //initialise entity lists
        mDrones = new ArrayList<>();
        mWalls = new ArrayList<>();

        //providing three default drones
        mDrones.add(new Drone(100, 100));
        mDrones.add(new Drone(250, 250));
        mDrones.add(new Drone(350, 200));
    }

    /**
     * This method is a getter for the player variable
     * @return the player currently in the arena
     */
    public PlayerDrone getPlayer(){
        return mPlayer;
    }

    /**
     * This method is responsible for adding entities into the member variables, once the user clicks the screen it adds
     * an entity to the screen depending on the value of entityflag. The exception is player, there can only be one player
     * on the screen at once, and the method simply changes the player position instead of adding another one if one already
     * exists
     * @param x     double  the x value which is somewhere in the canvas, on the same position where the mouse was clicked
     * @param y     double  the y value which is somewhere in the canvas, on the same position where the mouse was clicked
     * @param entityFlag    String  this value depends on which button was pressed in the arena, and decides which
     *                      drone is inserted
     */
    public void setArena(MyCanvas mc, double x, double y, String entityFlag) {
        switch (entityFlag){ //depending on entityFlag
            case "drone": //if the flag string states drone
                mDrones.add(new Drone(x, y)); break; //create a new drone
            case "hitpointsdrone":
                mDrones.add(new HitpointsDrone(x, y)); break;
            case "predator":
                mDrones.add(new PredatorDrone(x, y)); break;
            case "wall":
                mWalls.add(new Wall(x, y)); break;
            case "player":
                if(mPlayer == null) { //if there is no player
                    mPlayer = new PlayerDrone(x, y); //create a new player
                } else {
                    mPlayer.setXPosition(x); //otherwise set a new position for the player
                    mPlayer.setYPosition(y);
                }
            default:
                break;
        }
    }

    /**
     * This method iterates through all of the member variables (all of which are entities) and draws the images onto
     * the arena (the images are attributes of the entities)
     * @param mc    MyCanvas    This contains functionality to draw the images
     */
    public void drawArena(MyCanvas mc){
        mc.clearCanvas();  //clear the canvas of all previous drawings
        Image image = new Image(getClass().getResourceAsStream("background.png")); //set background image

        mc.drawImage(image, mc.getXCanvasSize()/2, mc.getYCanvasSize()/2, mc.getYCanvasSize()); //draw background

        if(mPlayer != null){ //draw player drone
            mc.drawImage(mPlayer.getImage(), mPlayer.getXPosition(), mPlayer.getYPosition(), mPlayer.getSize());
        }

        if(mDrones != null) { //draw drones
            for (Drone d : mDrones) {
                mc.drawImage(d.getImage(), d.getXPosition(), d.getYPosition(), d.getSize());
            }
        }

        if(mWalls != null) { //draw walls
            for (Wall w : mWalls) {
                mc.drawImage(w.getImage(), w.getXPosition(), w.getYPosition(), w.getSize());
            }
        }
    }

    /**
     * This method iterates through every single drone, then every single entity individually in a nested loop in order
     * to check if the entities collide and calculate trajectory of all entities. At the end of the method the drone
     * positions are adjusted.
     * @param mc    MyCanvas    This contains functionality to draw the images
     */
    public void updateArena(MyCanvas mc){
        for (Drone d : mDrones) {
            //if the drone is a predator
            if (d.getClass() == PredatorDrone.class && mPlayer != null) {
                ((PredatorDrone) d).setHasTarget(true); //set the target to the player
                ((PredatorDrone) d).setTarget(mPlayer);
                d.interact(mPlayer); //move towards/destroy player
                if(mPlayer.isAlive() == false){ //if player is not alive
                    mPlayer = null; //remove them from the arena
                }
            } else if (d.getClass() == PredatorDrone.class && mPlayer == null) { //else if a predator exists and there is no player
                for (Drone e : mDrones) { //interact as a normal drone
                    d.interact(e);
                }
            }

            for (Drone e : mDrones) { //collision detection for all drones
                if(e.getClass() != PredatorDrone.class && e != null) { //if it is not a predator behave like a normal drone
                    d.interact(e);
                }
            }

            for (Wall e : mWalls) { //if its a wall then interact with all normal drones
                e.interact(d);
            }

            d.checkDrone(mc); //update positions and draw all drones.
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

    /**
     * Iterates through every member variable and calls its toString() method
     * @return a string with information on every entity in the arena .
     */
    public String toString(){
        //returns positions of all drones
        String append = new String();
        append += "Entity Information: " + "\n"; //introductory dialogue
        for(int i = 0; i<46; i++){
            append += "_";
        }
        append += "\n";
        if(mDrones != null) { //print all drone info
            for (Drone d : mDrones) {
                append += d.toString() + "\n";
            }
        }
        if(mWalls != null) { //print wall info
            for (Wall w : mWalls) {
                append += w.toString() + "\n";
            }
        }
        if(mPlayer != null) { //print player info
            append += mPlayer.toString();
        }
        return append;
    }

    /**
     * Handles user input, takes in a keycode and moves the player a specified amount of steps if the player's next move
     * does not intersect a wall or arena bounds.
     * @param e     KeyEvent    Value passed in from the listener
     * @param mc    MyCanvas    Used to fetch arena size information
     */
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

    /**
     * Saves arena information to a .txt file which was specified
     * @param fileName      String      the filename passed in from a text dialogue
     */
    public void save(String fileName){
        //save method from last time
        //ask for file name which you wish to call it using a text dialogue
        File arenaFile = new File(fileName);

        try{
            FileWriter outFileWriter = new FileWriter(arenaFile); //open file writer for the specified file
            PrintWriter fw = new PrintWriter(outFileWriter); //allows to print to file

            for(Drone d : mDrones){
                if(d.getClass() == Drone.class) { //if it is a drone
                    //if its a normal drone then print x and y and add delimiter
                    fw.print("DRONE" + " " + d.getXPosition() + " " + d.getYPosition() + " ");
                } else if (d.getClass() == PredatorDrone.class){
                    fw.print("PREDATOR" + " " + d.getXPosition() + " " + d.getYPosition() + " ");
                } else if (d.getClass() == HitpointsDrone.class){
                    //if its a HPDrone print x, y, health and add delimiter
                    fw.print("HPDRONE" + " " + d.getXPosition() + " " + d.getYPosition() + " "
                            + ((HitpointsDrone) d).getHitPoints() + " ");
                }
            }
            for(Wall d : mWalls){ //if its a wall print x and y
                fw.print("WALL" + " " + d.getXPosition() + " " + d.getYPosition() + " ");
            }
            if(mPlayer != null){ //if its a player print x and y
                fw.print("PLAYER" + " " + mPlayer.getXPosition() + " " + mPlayer.getYPosition() + " ");
            }

            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearArena(){
        //clear all of the current arena.

        //set entity total to zero
        Drone entity = new Drone();
        entity.setGlobalEntityId(0);

        mDrones.clear();
        mWalls.clear();
        mPlayer = null;
    }

    /**
     * Loads arena information from a .txt file which was specified
     * @param fileName      String      the filename passed in from a text dialogue
     */
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
