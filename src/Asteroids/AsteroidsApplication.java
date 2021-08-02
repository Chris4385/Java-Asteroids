package Asteroids;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicInteger;


public class AsteroidsApplication extends Application {
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    @Override
    public void start(Stage window){

        Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
        Pane layout = new Pane();
        layout.setPrefSize(WIDTH,HEIGHT);
        AtomicInteger score = new AtomicInteger();
        Text text = new Text(10, 10, "Points: 0");
        Button restart = new Button("Restart");

        //style restart button
        restart.setMinSize(40,10);
        restart.setStyle("-fx-font-size:15");
        restart.setTranslateX(WIDTH/2 - 20);
        restart.setTranslateY(HEIGHT/2 -20);
        restart.setVisible(false);

        Ship ship = new Ship(WIDTH/2,HEIGHT/2);
        List<Asteroid> asteroids = new ArrayList<>();
        List<Projectile> projectiles = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            Random rand = new Random();
            asteroids.add(new Asteroid(rand.nextInt(WIDTH/3), rand.nextInt(HEIGHT)));
        }

        layout.getChildren().add(ship.getCharacter());
        layout.getChildren().add(restart);
        for(Asteroid asteroid: asteroids){
            layout.getChildren().add(asteroid.getCharacter());
        }
        layout.getChildren().add(text);

        Scene scene = new Scene(layout);


        scene.setOnKeyPressed(event -> {
            pressedKeys.put(event.getCode(), Boolean.TRUE);
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.put(event.getCode(), Boolean.FALSE);
        });

        restart.setOnAction((event) -> {
            restart(window);
        });

        //Animation timer details
        charactersMovement(pressedKeys, ship, asteroids, projectiles, layout, text, score, restart);

        window.setScene(scene);
        window.setTitle("Asteroids!");
        window.show();

    }

    private void restart (Stage window){
        window.close();
        start(window);
    }

    public static void main(String[] args) {
        launch(AsteroidsApplication.class);
    }



    private void charactersMovement(Map<KeyCode, Boolean> pressedKeys, Ship ship, List<Asteroid> asteroids, List<Projectile> projectiles, Pane layout, Text text, AtomicInteger points, Button restart){
        new AnimationTimer(){
            @Override
            public void handle(long now){
                if(pressedKeys.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if(pressedKeys.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if(pressedKeys.getOrDefault(KeyCode.UP, Boolean.FALSE)){
                    ship.accelerate();
                }

                if(pressedKeys.getOrDefault(KeyCode.SPACE, Boolean.FALSE) && projectiles.size() < 3){
                    Projectile proj = new Projectile((int) ship.getCharacter().getTranslateX(), (int) ship.getCharacter().getTranslateY());
                    proj.getCharacter().setRotate(ship.getCharacter().getRotate());
                    projectiles.add(proj);


                    proj.accelerate();
                    proj.setMovement(proj.getMovement().normalize().multiply(3));


                    layout.getChildren().add(proj.getCharacter());


                }

                if(Math.random() < 0.005) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if(!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        layout.getChildren().add(asteroid.getCharacter());
                    }
                }

                for(Projectile projectile: projectiles){
                    for(Asteroid asteroid: asteroids){
                        if(projectile.collide(asteroid)){
                            projectile.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    }
                    if(!projectile.isAlive()) {
                        text.setText("Points: " + points.addAndGet(1000));
                    }

                }
                projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .forEach(projectile -> layout.getChildren().remove(projectile.getCharacter()));

                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> layout.getChildren().remove(asteroid.getCharacter()));

                projectiles.removeAll(projectiles.stream()
                        .filter(projectile -> !projectile.isAlive())
                        .collect(Collectors.toList()));


                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .collect(Collectors.toList()));


                for(Asteroid asteroid: asteroids){
                    asteroid.move();
                    if(ship.collide(asteroid)){
                        stop();
                        restart.setVisible(true);
                    }
                }

                for(Projectile projectile: projectiles){
                    projectile.move();
                }

                ship.move();
            }

        }.start();
    }


}
