package pl.edu.agh.to2.kitkats.codinlearner.controller;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import pl.edu.agh.to2.kitkats.codinlearner.level.LevelManager;
import pl.edu.agh.to2.kitkats.codinlearner.model.Arena;
import pl.edu.agh.to2.kitkats.codinlearner.model.Command;
import pl.edu.agh.to2.kitkats.codinlearner.parser.CommandParser;
import java.util.List;
import java.util.HashMap;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CodinOverviewController {

    private LevelManager levelManager;
    private CommandParser commandParser;

    public Arena arena;

    private CodinAppController appController;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label prevCommands;

    @FXML
    private TextField commandLine;

    @FXML
    private Canvas turtleCanvas;

    private GraphicsContext turtleGc;

    @FXML
    private Canvas lineCanvas;

    private GraphicsContext lineGc;


    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private Button moveForwardButton;

    @FXML
    private Button checkButton;

    @FXML
    private void initialize() {
        levelManager = new LevelManager(0);
        //TODO commands map parsed from JSON or level - for every level some available commands
        HashMap<String, Command> movesMap = new HashMap<>();
        movesMap.put("go", Command.FORWARD);
        movesMap.put("left", Command.LEFT);
        movesMap.put("right", Command.RIGHT);
        commandParser = new CommandParser(movesMap);

        prevCommands.setMinHeight(170);
        turtleGc = turtleCanvas.getGraphicsContext2D();
        prevCommands.heightProperty().addListener(observer -> scrollPane.setVvalue(1.0));
        turtleGc.setFill(Color.BLACK);

        lineGc = lineCanvas.getGraphicsContext2D();
        lineGc.setStroke(Color.RED);

        commandLine.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke){
                if (ke.getCode().equals(KeyCode.ENTER)){

                    prevCommands.setMinHeight(max(170,Region.USE_PREF_SIZE));
                    prevCommands.setText(prevCommands.getText() + "\n>>> " + commandLine.getText());
                    List<Command> commands = commandParser.parseCommand(commandLine.getText());
                    levelManager.addCommands(commands);
                    move(commands);
                    commandLine.clear();
                }
            }
        });
    }

//    @FXML
//    private void handleLeftAction(ActionEvent event) {
//        this.arena.getTurtle().turnLeft();
//        clearArena();
//        turtleGc.fillPolygon(
//                this.arena.getTurtle().getShapePointsX(), this.arena.getTurtle().getShapePointsY(),3);
//
//    }
//
//
//    @FXML
//    private void handleRightAction(ActionEvent event) {
//        this.arena.getTurtle().turnRight();
//        clearArena();
//        turtleGc.fillPolygon(
//                this.arena.getTurtle().getShapePointsX(), this.arena.getTurtle().getShapePointsY(),3);
//    }
//
//    @FXML
//    private void handleAddAction(ActionEvent event) {
//        move();
//    }

    @FXML
    private void handleCheckAction(ActionEvent event) {
        boolean passed = levelManager.checkCurrentLevel(this.prevCommands.toString());


    }


    private void move(List<Command> commands){

        clearArena();
        DoubleProperty x  = new SimpleDoubleProperty();
        DoubleProperty y  = new SimpleDoubleProperty();

        float startX = arena.getTurtle().getX();
        float startY = arena.getTurtle().getY();

        this.arena.getTurtle().move(commands);

        float endX = arena.getTurtle().getX();
        float endY = arena.getTurtle().getY();

        lineGc.strokeLine( startX,  startY, endX, endY);

        turtleGc.fillPolygon(
                arena.getTurtle().getShapePointsX(), arena.getTurtle().getShapePointsY(),3);

    }


    public void setData(Arena arena) {
        this.arena = arena;
        turtleGc.fillPolygon(
                this.arena.getTurtle().getShapePointsX(), this.arena.getTurtle().getShapePointsY(),3);

    }

    public void setAppController(CodinAppController appController) {
        this.appController = appController;
    }

    private void clearArena(){
        turtleGc.clearRect(0, 0, this.arena.getWidth(), this.arena.getHeight());
    }
}

