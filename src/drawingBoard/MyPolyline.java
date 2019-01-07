package drawingBoard;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polyline;

public class MyPolyline implements Tool
{
    private Polyline currentPolyline =new Polyline();
    @Override
    public void press(MouseEvent e, Board pane)
    {
        if(e.getButton().equals(MouseButton.SECONDARY))
        {
            if(currentPolyline.getPoints().isEmpty()) return;

            currentPolyline.getPoints().addAll(currentPolyline.getPoints().get(0), currentPolyline.getPoints().get(1));
            currentPolyline.setFill(pane.fa.getMyLeft().getColor());
            currentPolyline =new Polyline();
            return;
        }
        if(currentPolyline.getPoints().isEmpty())
        {
            pane.add(currentPolyline);
            currentPolyline.getPoints().addAll(e.getX(), e.getY());
        }
        currentPolyline.getPoints().addAll(e.getX(),e.getY());
    }

    @Override
    public void drag(MouseEvent e, Board pane)
    {
        if(e.getButton().equals(MouseButton.SECONDARY)) return;
        currentPolyline.getPoints().remove(currentPolyline.getPoints().size()-1);
        currentPolyline.getPoints().remove(currentPolyline.getPoints().size()-1);
        currentPolyline.getPoints().addAll(e.getX(),e.getY());
    }

    @Override
    public void release(MouseEvent e, Board pane)
    {
        if(e.getButton().equals(MouseButton.SECONDARY)) return;
        currentPolyline.getPoints().addAll(e.getX(),e.getY());
    }
}