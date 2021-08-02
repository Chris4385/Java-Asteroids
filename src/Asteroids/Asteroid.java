package Asteroids;

import java.util.Random;

/**
 *
 * @author Gan
 */
public class Asteroid extends Character{
    private double rotationalMovement;

    public Asteroid(int x, int y){
        super(new AsteroidGenerator().createDifferentAsteroid(), x, y);
        Random rand = new Random();

        this.getCharacter().setRotate(rand.nextInt(360));
        int accelerationTimes = 1 + rand.nextInt(10);
        for(int i = 0; i < accelerationTimes; i++){
            this.accelerate();
        }

        this.rotationalMovement = 0.5 - rand.nextDouble();
    }

    @Override
    public void move(){
        super.move();
        super.getCharacter().setRotate(this.getCharacter().getRotate() + this.rotationalMovement);
    }
}
