package com.monkeman.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.World;
import com.monkeman.game.utils.CollisionRect;

import java.util.ArrayList;

public class Projectiles {
    private CollisionRect collisionRect;
    private int limit = 20;
    public static ArrayList<Projectile> projectilesList = new ArrayList<Projectile>();

    public void render(Batch batch) {
        for (Projectile projectile : projectilesList) {
            projectile.render(batch);
        }
    }

    public void update(float delta) {
        for (Projectile projectile : projectilesList) {
            projectile.update(delta);
        }
    }

    public void collisionWith(CollisionRect contact, World world) {
        for (int i = 0; i < projectilesList.size(); i++) {
            if (projectilesList.get(i).getCollisionRect().collisionWith(contact)) {
                projectilesList.get(i).despawn(world);
                projectilesList.remove(i);
                return;
            }
        }
    }

    public CollisionRect getCollisionRect() {
        CollisionRect collisionRect = new CollisionRect(1, 1, 1, 1);
        for (Projectile projectile : projectilesList) {
            collisionRect = projectile.getCollisionRect();
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
