package drawingBoard;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.io.Serializable;

public class MyLine extends Line implements Tool
{
    private Line currentLine = new Line();
    @Override
    public void press(MouseEvent e,Board pane)
    {
        currentLine=new Line(e.getX()-pane.getWidth()/2,e.getY()-pane.getHeight()/2,e.getX()-pane.getWidth()/2,e.getY()-pane.getHeight()/2);
        currentLine.setStrokeWidth(pane.fa.getMyLeft().getNowStrokeWidth());
        currentLine.setStroke(pane.fa.getMyLeft().getColor());
        pane.add(currentLine);
    }
    @Override
    public void drag(MouseEvent e,Board pane)
    {
        currentLine.setEndX(e.getX()-pane.getWidth()/2);
        currentLine.setEndY(e.getY()-pane.getHeight()/2);
    }
    @Override
    public void release(MouseEvent e, Board pane)
    {
        double lengthSquare = (currentLine.getStartX()-currentLine.getEndX())*(currentLine.getStartX()-currentLine.getEndX())+(currentLine.getStartY()-currentLine.getEndY())*(currentLine.getStartY()-currentLine.getEndY());
        if(lengthSquare<4)
        {
            pane.delete(currentLine);
        }
        else if(lengthSquare<100)
        {
            AlertBox alertBox = new AlertBox("The object you draw is small, Do you still want to add it?", "Too Small", "yes", "no");
            if (alertBox.getMode() != 1)pane.delete(currentLine);
        }
    }
}
