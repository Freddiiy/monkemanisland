package com.monkeman.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.monkeman.game.utils.CollisionRect;
import com.monkeman.game.utils.Constants;

public class Projectile {
    private Body body;
    private final Vector2 velocity;

    private final Texture texture;

    private final CollisionRect collisionRect;

    public Projectile(World world, Vector2 spawn, Vector2 target) {
        this.body = createBox(world, spawn.x, spawn.y, 4, 4, false);
        this.texture = new Texture("projectiles/projectile.png");
        this.velocity = new Vector2((target.x - spawn.x),(target.y - spawn.y));

        this.collisionRect = new CollisionRect(spawn.x, spawn.y, 8, 8);
    }

    public void render(Batch batch) {
        float width = 8;
        float height = 8;
        if (this.body != null) {
            batch.begin();
            batch.draw(texture, this.body.getPosition().x * Constants.PPM - (width / 2), this.body.getPosition().y * Constants.PPM - (height / 2));
            batch.end();
        }
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void update(float delta) {
        if (this.body != null) {
            float SPEED = 1000;
            body.setLinearVelocity(velocity.nor().x * (SPEED * delta), body.getLinearVelocity().y);
            body.setLinearVelocity(body.getLinearVelocity().x, velocity.nor().y * (SPEED * delta));

            updateCollisionRect();
        }
    }

    public float getSpread(float maxAmount) {
        float minusAmount = maxAmount -(maxAmount*2);
        return (float)((Math.random() * (maxAmount - minusAmount)) + minusAmount);
    }

    public void getTravelDirection(Vector2 spawn, Vector2 target) {
        Vector2 direction = new Vector2(0, 0);
        direction.x = (spawn.x - target.x);
        direction.y = (spawn.y - target.y);
    }

    public void input () {
    }

    public Body createBox(World world, float x, float y, int width, int height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / Constants.PPM, y / Constants.PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = Constants.BIT_PROJECTILE;
        fixtureDef.filter.maskBits = Constants.BIT_ENEMY;
        fixtureDef.filter.groupIndex = 1;
        pBody.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        return pBody;
    }

    public void fixFixtureFilter(Fixture fixture, short categoryBits, short maskBits) {
        Filter filter = fixture.getFilterData();
        filter.categoryBits = categoryBits;
        filter.maskBits = maskBits;
        fixture.setFilterData(filter);
    }

    public void despawn(World world) {
        if (body != null) {
            world.destroyBody(this.body);
            texture.dispose();
            this.body = null;
        }
    }

    //Collision rect
    public void updateCollisionRect () {
        this.collisionRect.updatePosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
        //System.out.println("PLAYER:" + collisionRect.getX() + ", " + collisionRect.getY() + ", " + collisionRect.getX() + collisionRect.getWidth() + ", " + collisionRect.getX() + collisionRect.getHeight());
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }
}
