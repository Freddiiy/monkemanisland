package com.monkeman.game.entities.enemy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.monkeman.game.utils.CollisionRect;

import java.util.ArrayList;

public class EnemyProjectiles {
    private int limit = 80;
    public static ArrayList<EnemyProjectile> projectilesList = new ArrayList<EnemyProjectile>();

    public void render(Batch batch) {
        for (int i = 0; i < projectilesList.size(); i++) {
            projectilesList.get(i).render(batch);
        }
    }

    public void update(float delta) {
        for (int i = 0; i < projectilesList.size(); i++) {
            projectilesList.get(i).update(delta);
        }
    }

    public boolean collisionWith(CollisionRect contact, World world) {
        for (int i = 0; i < projectilesList.size(); i++) {
            if (projectilesList.get(i).getCollisionRect().collisionWith(contact)) {
                projectilesList.get(i).despawn(world);
                projectilesList.remove(i);
                return true;
            }
        }
        return false;
    }

    public CollisionRect getCollisionRect() {
        CollisionRect collisionRect = new CollisionRect(1, 1, 1, 1);
        for (int i = 0; i < projectilesList.size(); i++) {
            collisionRect = projectilesList.get(i).getCollisionRect();
        }
        return collisionRect;
    }

    public void despawnFirst(World world) {
        if (projectilesList.size() > limit) {
            projectilesList.remove(0);
            projectilesList.get(0).despawn(world);
        }
    }

    public void despawn(World world) {
        if (projectilesList.size() > limit) {
            projectilesList.remove(0);
            projectilesList.get(0).despawn(world);
        }
    }
}
