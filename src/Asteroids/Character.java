package Asteroids;

import javafx.geometry.Point2D;
import javafx.scene.shape.Shape;
import javafx.scene.shape.Polygon;
import javafx.geometry.Bounds;

/**
 *
 * @author Gan
 */
public abstract class Character {
    private boolean alive;
    private Polygon layout;
    private Point2D movement;


    public Character(Polygon layout, int x, int y){
        this.alive = true;
        this.layout = layout;
        this.layout.setTranslateX(x);
        this.layout.setTranslateY(y);
        this.movement = new Point2D(0,0);

    }


    public boolean isAlive(){
        return this.alive;
    }

    public void setAlive(boolean value){
        this.alive = value;
    }
    public Point2D getMovement(){
        return this.movement;
    }

    public void setMovement(Point2D newValue){
        this.movement = newValue;
    }
    public Polygon getCharacter(){
        return this.layout;
    }

    public void turnLeft(){
        this.layout.setRotate(this.layout.getRotate() - 5);
    }

    public void turnRight(){
        this.layout.setRotate(this.layout.getRotate() + 5);
    }

    public void move(){
        Bounds layoutBounds = this.layout.getBoundsInParent();
        this.layout.setTranslateX(this.layout.getTranslateX() + this.movement.getX());
        this.layout.setTranslateY(this.layout.getTranslateY() + this.movement.getY());

        if (this.layout.getTranslateX() < 0 && layoutBounds.getMaxX() < 0) {
            this.layout.setTranslateX(AsteroidsApplication.WIDTH);
        }

        if (this.layout.getTranslateX() > AsteroidsApplication.WIDTH && layoutBounds.getMinX() > AsteroidsApplication.WIDTH) {
            this.layout.setTranslateX(0);
        }

        if (this.layout.getTranslateY() < 0 && layoutBounds.getMaxY() < 0) {
            this.layout.setTranslateY(AsteroidsApplication.HEIGHT);
        }

        if (this.layout.getTranslateY() > AsteroidsApplication.HEIGHT && layoutBounds.getMinY() > AsteroidsApplication.HEIGHT) {
            this.layout.setTranslateY(0);
        }
    }



    public void accelerate(){
        Double accelerateX = 0.05*(Math.cos(Math.toRadians(this.layout.getRotate())));
        Double accelerateY = 0.05 *(Math.sin(Math.toRadians(this.layout.getRotate())));

        this.movement = this.movement.add(accelerateX,accelerateY);
    }


    public boolean collide(Character other){
        Shape intersection = Shape.intersect(this.layout, other.getCharacter());
        return intersection.getBoundsInLocal().getWidth() != -1;
    }


}
