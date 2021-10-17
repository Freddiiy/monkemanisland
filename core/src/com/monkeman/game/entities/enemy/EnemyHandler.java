package com.monkeman.game.entities.enemy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.monkeman.game.utils.CollisionRect;

import java.util.ArrayList;

//spawns the enemies and spawns new ones when the current ones are dead
public class EnemyHandler {
    private float numberOfEnemy;
    private float numberOfEnemyMax = 1;
    private int deadEnemies = 0;
    private int numberRound = 0;
    private int limit = 100;
    private int counter = 1;

    public EnemyHandler(float numberOfEnemyMax) {
        this.numberOfEnemyMax = numberOfEnemyMax;
    }

    public static ArrayList<Enemy> enemyHandler = new ArrayList<>();

    public void render(Batch batch) {
        for (Enemy enemy : enemyHandler) {
            enemy.render(batch);
        }
    }

    public void update(World world, Vector2 target, float delta) {
        if (allEnemiesDead()) {
            numberOfEnemyMax *= 1.2;
            numberOfEnemy = 0;
            numberRound += 1;
            deadEnemies = 0;
        }
        dead(world);
        despawnFirst(world);
        spawnEnemies(world, getRandomNumber(600, 1000), getRandomNumber(600, 1000), delta);
        updatePosition(world, target, delta);
    }

    public void updatePosition(World world, Vector2 target, float delta) {
        for (Enemy enemy : enemyHandler) {
            enemy.update(world, target, delta);
        }
    }

    public void spawnEnemies(World world, float x, float y, float delta) {
        if (numberOfEnemyMax >= numberOfEnemy) {
            enemyHandler.add(new Enemy(world, x, y));
            numberOfEnemy++;
            counter = 1;
        } else {
            counter++;
        }
    }

    public void despawnFirst(World world) {
        if (enemyHandler.size() > limit) {
            enemyHandler.get(0).despawn(world);
            enemyHandler.remove(0);
        }
    }

    public void despawn(World world) {
        if (enemyHandler.size() > limit) {
            enemyHandler.get(0).despawn(world);
            enemyHandler.remove(0);
        }
    }

    public boolean allEnemiesDead() {
        return numberOfEnemy == deadEnemies;
    }

    public void dead(World world) {
        for (int i = 0; i < enemyHandler.size(); i++) {
            if (enemyHandler.get(i).isDead()) {
                enemyHandler.get(i).despawn(world);
                enemyHandler.remove(i);
                deadEnemies++;
                System.out.println(deadEnemies);
            }
        }
    }

    public boolean collisionWith(CollisionRect contact, World world) {
        for (Enemy enemy : enemyHandler) {
            if (enemy.getCollisionRect().collisionWith(contact)) {
                enemy.collisionWith(contact);
                return true;
            }
        }
        return false;
    }

    public CollisionRect getCollisionRect() {
        CollisionRect collisionRect = new CollisionRect(1, 1, 1, 1);
        for (Enemy enemy : enemyHandler) {
            return enemy.getCollisionRect();
        }
        return collisionRect;
    }

    public float getRandomNumber(int x, int y) {
        return (float)(Math.random() * (x-y) + y);
    }

    public float getNumberOfEnemy() {
        return numberOfEnemy;
    }

    public void setNumberOfEnemy(float numberOfEnemy) {
        this.numberOfEnemy = numberOfEnemy;
    }

    public float getNumberOfEnemyMax() {
        return numberOfEnemyMax;
    }

    public void setNumberOfEnemyMax(float numberOfEnemyMax) {
        this.numberOfEnemyMax = numberOfEnemyMax;
    }

    public int getDeadEnemies() {
        return deadEnemies;
    }

    public void setDeadEnemies(int deadEnemies) {
        this.deadEnemies = deadEnemies;
    }

    public int getNumberRound() {
        return numberRound;
    }

    public void setNumberRound(int numberRound) {
        this.numberRound = numberRound;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
