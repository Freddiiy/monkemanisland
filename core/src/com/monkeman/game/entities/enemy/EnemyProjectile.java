package com.monkeman.game.entities.enemy;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.monkeman.game.utils.CollisionRect;

import static com.monkeman.game.utils.Constants.*;
import static com.monkeman.game.utils.Constants.PPM;

public class EnemyProjectile {
    final boolean DEBUG = false;

    private final float SPEED = 500;
    private float spread = 1000;
    private Body body;
    private Vector2 spawn;
    private Vector2 target;
    private Vector2 velocity;

    float rotate;

    private final CollisionRect collisionRect;

    private final Texture texture;
    private final Sprite sprite;

    public EnemyProjectile(World world, Vector2 spawn, Vector2 target) {
        this.body = createBox(world, spawn.x, spawn.y, 4, 4, false);
        this.spawn = new Vector2(spawn);
        this.target = new Vector2(target);
        this.texture = new Texture("projectiles/bana.png");
        this.sprite = new Sprite(texture);
        this.velocity = new Vector2((target.x - spawn.x),(target.y - spawn.y));

        this.rotate = 0;

        this.collisionRect = new CollisionRect(body.getPosition().x * PPM, body.getPosition().y * PPM, 8, 8);
    }

    public void render(Batch batch) {
        float width = 8;
        float height = 8;
        if (this.body != null) {
            batch.begin();
            sprite.setRotation(rotate/360f);
            batch.draw(sprite, this.body.getPosition().x * PPM - (width / 2), this.body.getPosition().y * PPM - (height / 2));
            batch.end();
        }
    }

    public void update(float delta) {
        if (this.body != null) {

            body.setLinearVelocity(velocity.nor().x * (SPEED * delta), body.getLinearVelocity().y);
            body.setLinearVelocity(body.getLinearVelocity().x, velocity.nor().y * (SPEED * delta));
            rotate += rotate + 1 * delta;
            updateCollisionRect();
        }
    }

    public float getSpread(float maxAmount) {
        float minusAmount = maxAmount -(maxAmount*2);
        return (float)((Math.random() * (maxAmount - minusAmount)) + minusAmount);
    }

    public Body createBox(World world, float x, float y, int width, int height, boolean isStatic) {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = BIT_ENEMY_PROJECTILE;
        fixtureDef.filter.maskBits = BIT_WALL | BIT_PLAYER;
        fixtureDef.filter.groupIndex = 1;
        pBody.createFixture(fixtureDef);
        shape.dispose();
        return pBody;
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
        this.collisionRect.updatePosition(body.getPosition().x * PPM, body.getPosition().y * PPM);
        //System.out.println("PLAYER:" + collisionRect.getX() + ", " + collisionRect.getY() + ", " + collisionRect.getX() + collisionRect.getWidth() + ", " + collisionRect.getX() + collisionRect.getHeight());
    }

    public void collisionWith (CollisionRect contact) {
        if (collisionRect.collisionWith(contact)) {
        }
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }
}


