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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DroneWindow extends Application {
    private int canvasSize = 512;
    private VBox rtPane;						// pane in which info on drones listed
    private MyCanvas mc;
    private DroneArena mArena;
    private boolean mAnimationOn;
    private String mEntityFlag = "drone";

    public void drawStatus() {
        rtPane.getChildren().clear();
        Label label = new Label(mArena.toString()); //put drone info here (tostring method)
        rtPane.getChildren().add(label);
    }

    private void setMouseEvents (Canvas canvas) {
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        mArena.setArena(mc, e.getX(), e.getY(), mEntityFlag);
                        mArena.drawArena(mc);
                        drawStatus();
                    }
                });
    }

    private VBox setMenu(){
        MenuBar menuBar = new MenuBar();
        VBox menuBox = new VBox(menuBar);
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);

        MenuItem save = new MenuItem("save");
        MenuItem load = new MenuItem("load");

        fileMenu.getItems().add(save);
        fileMenu.getItems().add(load);

        save.setOnAction((e -> {
            //call save function in mArena
        }));

        load.setOnAction((e -> {
            //call load function in mArena
        }));

        return menuBox;
    }

    private HBox setButtons(){
        Button btnAnimOn = new Button("Start");
        // now add handler
        btnAnimOn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mAnimationOn = true;
            }
        });

        Button btnAnimOff = new Button("Stop");
        // now add handler
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


        return new HBox(new Label(" Animation: "), btnAnimOn, btnAnimOff,
                btnInsertDrone, btnInsertWall, btnInsertPredator, btnInsertPlayer);
    }

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

        Scene scene = new Scene(bp, canvasSize*1.5, canvasSize*1.2);
        // create scene so bigger than canvas,

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
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
