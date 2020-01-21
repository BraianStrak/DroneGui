package dronegui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.Optional;

public class DroneWindow extends Application {
    private int canvasSize = 512;
    private VBox rtPane;						// pane in which info on drones listed
    private MyCanvas mc;
    private DroneArena mArena;
    private boolean mAnimationOn;
    private String mEntityFlag = "drone";

    /**
     * Creates the pane which displays drone information
     */
    public void drawStatus() {
        rtPane.getChildren().clear();
        Label label = new Label(mArena.toString()); //creating a label with drone information
        rtPane.getChildren().add(label); //adding the label to the right pane
    }

    /**
     * Creates a mouse event listener which creates a drone on the position clicked with the specified delimiter
     * @param canvas    Canvas  used for adding the event handler
     */
    private void setMouseEvents (Canvas canvas) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) { //on mouse click
                        mArena.setArena(mc, e.getX(), e.getY(), mEntityFlag); //pass the current co ordinates and flag
                        mArena.drawArena(mc); //update the arena to reflect the changes
                        drawStatus();
                    }
                });
    }

    /**
     * Creates the menu for the top of the screen and all the behaviour associated with the buttons, both save and load
     * create a textdialogue and call the respective methods based on which button was pressed. The cleararena button simply
     * calls that method in DroneArena to clear the arena.
     * @return the VBox containing the menu
     */
    private VBox setMenu(){
        MenuBar menuBar = new MenuBar(); //create new menue
        VBox menuBox = new VBox(menuBar);
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);

        MenuItem save = new MenuItem("save"); //set menu titles
        MenuItem load = new MenuItem("load");
        MenuItem clear = new MenuItem("clear all");

        fileMenu.getItems().add(save); //add menu options
        fileMenu.getItems().add(load);
        fileMenu.getItems().add(clear);

        save.setOnAction((e -> { //set behaviour for save button
            TextInputDialog saveDialog = new TextInputDialog("filename.txt"); //make a new textdialogue
            saveDialog.setTitle("Save Arena");
            saveDialog.setHeaderText("Enter File Name");
            saveDialog.setHeaderText("Enter File Name");

            Optional<String> result = saveDialog.showAndWait(); //show text dialogue

            result.ifPresent((name -> { //call save with the returned value
                mArena.save(name);
            }));
        }));

        load.setOnAction((e -> { //set behaviour for load button
            TextInputDialog loadDialog = new TextInputDialog("filename.txt"); //make a new textdialogue
            loadDialog.setTitle("Load Arena");
            loadDialog.setHeaderText("Enter File Name");
            loadDialog.setHeaderText("Enter File Name");

            Optional<String> result = loadDialog.showAndWait(); //show text dialogue

            result.ifPresent((name -> {
                mArena.load(name); //call load with the returned value
                mArena.drawArena(mc); //update arena to reflect changes
                drawStatus();
            }));
        }));


        clear.setOnAction((e -> { //set clear button behaviour
            mArena.clearArena(); //call clear in arena
            drawStatus(); //update arena to reflect changes
            mArena.drawArena(mc);
        }));


        return menuBox;
    }

    /**
     * Sets up and returns all buttons which stop and start the animation as well as set the flag to the drone respective
     * of the button.
     * @return the HBox containing the buttons
     */
    private HBox setButtons(){
        Button btnAnimOn = new Button("Start"); //create a button
        btnAnimOn.setOnAction(new EventHandler<ActionEvent>() { //add a handler for the button
            @Override
            public void handle(ActionEvent event) {
                mAnimationOn = true; //start animation
            }
        });

        Button btnAnimOff = new Button("Stop");
        btnAnimOff.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mAnimationOn = false;
            }
        });

        Button btnInsertDrone = new Button("Drone");
        btnInsertDrone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //set flag to make drone
                mEntityFlag = "drone";
            }
        });

        Button btnInsertWall = new Button("Tree");
        btnInsertWall.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mEntityFlag = "wall";
            }
        });

        Button btnInsertPredator = new Button("Predator");
        btnInsertPredator.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mEntityFlag = "predator";
            }
        });

        Button btnInsertPlayer = new Button("Player");
        btnInsertPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mEntityFlag = "player";
            }
        });

        Button btnInsertHitpointsDrone = new Button("HP Drone");
        btnInsertHitpointsDrone.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mEntityFlag = "hitpointsdrone";
            }
        });


        return new HBox(new Label(" Animation: "), btnAnimOn, btnAnimOff,
                btnInsertDrone, btnInsertWall, btnInsertPredator, btnInsertPlayer, btnInsertHitpointsDrone);
    }

    /**
     * calls all of the methods which return the menus, toolbars and right pane, as well as starts the animation
     * @param stagePrimary      Stage      used to open the window
     */
    @Override
     public void start(Stage stagePrimary) throws Exception{
        stagePrimary.setTitle("Braian Strak, 27001448, Drone Animation");

        BorderPane bp = new BorderPane();			// create border pane

        Group root = new Group();					// create group

        Canvas canvas = new Canvas( canvasSize, canvasSize );
        // and canvas to draw in

        root.getChildren().add( canvas );			// and add canvas to group

        mc = new MyCanvas(canvas.getGraphicsContext2D(), canvasSize, canvasSize);
        // create MyCanvas passing context on canvas onto which images put

        setMouseEvents(canvas);                     //sets mouse listeners

        mArena = new DroneArena();

        bp.setCenter(root);                         //put group in center pane

        rtPane = new VBox();						// set vBox for listing data
        bp.setRight(rtPane);						// put in right pane

        bp.setBottom(setButtons());					// add buttons to bottom

        bp.setTop(setMenu());                       // add menu top top

        mArena.drawArena(mc);                       //draw so background is viewable
        drawStatus();

        // for animation, note start time
        new AnimationTimer()			// create timer
        {
            public void handle(long currentNanoTime) {
                // define handle for what do at this time
                if (mAnimationOn) {
                    mArena.updateArena(mc);			// finds new positions
                    mArena.drawArena(mc);				// draw entities in new position
                    drawStatus();
                }
            }
        }.start();					// start it

        Scene scene = new Scene(bp, canvasSize*1.9, canvasSize*1.3);
        // create scene bigger than canvas,

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() { //start animation
            @Override
            public void handle(KeyEvent event) {
                if(mArena.getPlayer() != null) {
                    mArena.pressedKey(event, mc);
                }
            }
        });

        stagePrimary.setScene(scene);
        stagePrimary.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
