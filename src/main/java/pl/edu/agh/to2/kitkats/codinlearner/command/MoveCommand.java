package pl.edu.agh.to2.kitkats.codinlearner.command;

import javafx.scene.canvas.GraphicsContext;
import pl.edu.agh.to2.kitkats.codinlearner.model.Arena;
import pl.edu.agh.to2.kitkats.codinlearner.model.ParameterizedInstruction;


public class MoveCommand implements pl.edu.agh.to2.kitkats.codinlearner.command.Command{

    private GraphicsContext lineGc;
    private ParameterizedInstruction command;
    private Arena arena;

    public MoveCommand(GraphicsContext graphicsContext, ParameterizedInstruction command, Arena arena) {
        this.command = command;
        this.arena = arena;
        this.lineGc = graphicsContext;
    }

    @Override
    public void execute() {
        double startX = arena.getCursor().getX();
        double startY = arena.getCursor().getY();

        this.arena.getCursor().move(1, command);

        double endX = arena.getCursor().getX();
        double endY = arena.getCursor().getY();

        lineGc.strokeLine( startX,  startY, endX, endY);
    }

    @Override
    public void undo() {

        this.arena.getCursor().moveBack(command);

        lineGc.clearRect(0, 0, this.arena.getWidth(), this.arena.getHeight());

    }

    @Override
    public void redraw(){
        double startX = arena.getCursor().getX();
        double startY = arena.getCursor().getY();

        this.arena.getCursor().move(0, this.command);

        double endX = arena.getCursor().getX();
        double endY = arena.getCursor().getY();

        lineGc.strokeLine( startX,  startY, endX, endY);
    }


    @Override
    public void redo() {
        this.execute();
    }

    @Override
    public String getName() {
        return "New line: ";
    }
}
