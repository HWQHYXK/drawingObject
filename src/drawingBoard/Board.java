package drawingBoard;

import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;


public class Board extends Pane
{
    MainPane fa;
    private StringProperty x = new SimpleStringProperty("x: "),y = new SimpleStringProperty("y: ");
    private Group object = new Group();
    private GetPos getPos;
    private static Bloom bloom = new Bloom(0.3);
    private final static double RATIO = 0.8;

    public GetPos getGetPos()
    {
        return getPos;
    }

    public Board(MainPane fa)
    {
        this.fa=fa;
//        setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 50%,  cyan, SkyBlue 75%, LightGreen)");
        setStyle("-fx-background-color: Silver");
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(10.0);
        innerShadow.setOffsetY(10.0);
        setEffect(innerShadow);
//        InnerShadow innerShadow = new InnerShadow();
//        innerShadow.setOffsetX(1.0);
//        innerShadow.setOffsetY(1.0);
//        setEffect(innerShadow);
        getChildren().add(object);
        object.layoutXProperty().bind(widthProperty().divide(2));
        object.layoutYProperty().bind(heightProperty().divide(2));
        getPos = new GetPos();
        object.setOnMouseEntered(getPos);
        object.setOnMouseMoved(getPos);
        object.setOnMouseExited(getPos);
        fa.widthProperty().addListener((width, pre, now) ->
        {
            setPrefWidth(fa.getWidth()*RATIO);
        });
        fa.heightProperty().addListener((height, pre, now) ->
        {
            setPrefHeight(fa.getHeight()*RATIO);
        });
        setOnMousePressed(this::press);
        setOnMouseDragged(this::drag);
        setOnMouseReleased(this::release);
    }
    public void press(MouseEvent e)
    {
        fa.getMyLeft().getTool().press(e,this);
    }
    public void drag(MouseEvent e)
    {
        fa.getMyLeft().getTool().drag(e,this);
    }
    public void release(MouseEvent e)
    {
        fa.getMyLeft().getTool().release(e,this);
    }

    public Group getObject()
    {
        return object;
    }

    class GetPos implements EventHandler<MouseEvent>
    {
        public StringProperty x = new SimpleStringProperty("x: "),y = new SimpleStringProperty("y: ");
        private boolean flag = false;
        @Override
        public void handle(MouseEvent event)
        {
            if(event.getEventType().equals(MouseEvent.MOUSE_ENTERED))
            {
                flag = true;
                x.setValue("x: "+event.getX());
                y.setValue("y: "+event.getY());
            }
            else if(flag&&event.getEventType().equals(MouseEvent.MOUSE_MOVED))
            {
                x.setValue("x: "+event.getX());
                y.setValue("y: "+event.getY());
            }
            else
            {
                flag=false;
            }
        }
    }
    public void load(Shape node)
    {
        object.getChildren().add(node);
        fa.getMyRight().add(node);
        node.setOnMouseEntered(event ->
        {
//            node.setStyle("-fx-fill: INDIANRED; -fx-stroke:INDIANRED");
//            node.setEffect(bloom);
            fa.getMyBottom().setSerial(String.valueOf(object.getChildren().indexOf(node)));
        });
        node.setOnMouseExited(event ->
        {
//            node.setStyle(null);
//            node.setEffect(null);
            fa.getMyBottom().setSerial("");
        });
        MoveProcessor processor = new MoveProcessor();
        node.setOnMousePressed(processor);
        node.setOnMouseDragged(processor);
//        node.setOnMousePressed(event ->
//        {
//            if(fa.getMyLeft().getTool() instanceof MyEraser)delete(node);
//        });
//        node.setOnMouseDragEntered(event ->
//        {
//            if(fa.getMyLeft().getTool() instanceof MyEraser)delete(node);
//        });
//        node.setOnMouseDragOver(event ->
//        {
//            if(fa.getMyLeft().getTool() instanceof MyEraser)delete(node);
//        });
    }
    public void add(Shape node)
    {
        fa.getMyTop().setRecentSave(false);
        load(node);
    }
    public void delete(Shape node)
    {
        fa.getMyRight().delete(node);
        object.getChildren().remove(node);
    }
    public void clear()
    {
        fa.getMyRight().getSelected().clear();
        fa.getMyRight().clear();
        object.getChildren().clear();
    }
    private class MoveProcessor implements EventHandler<MouseEvent>
    {
        private double originalX = 0, originalY = 0;
        @Override
        public void handle(MouseEvent event)
        {
            if(fa.getMyLeft().getTool() instanceof MyChooser)
            {

                if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED))
                {
                    if(event.getButton().equals(MouseButton.SECONDARY))
                    {
                        originalX = event.getX();//+((Node)event.getSource()).getLayoutX();
                        originalY = event.getY();//+((Node)event.getSource()).getLayoutY();
                    }
                }
                if(event.getEventType().equals(MouseEvent.MOUSE_DRAGGED))
                {
                    if(event.getButton().equals(MouseButton.SECONDARY))
                    {
                        ((Node)event.getSource()).setLayoutX(((Node)event.getSource()).getLayoutX()+event.getX()-originalX);
                        ((Node)event.getSource()).setLayoutY(((Node)event.getSource()).getLayoutY()+event.getY()- originalY);
                    }
                }
            }
        }
    }
}