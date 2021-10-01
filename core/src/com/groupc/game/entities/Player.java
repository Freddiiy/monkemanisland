package com.groupc.game.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.groupc.game.utils.CollisionRect;

import static com.groupc.game.utils.Constants.*;

public class Player {
    private Vector2 position;

    private TextureAtlas atlas;
    private Texture texture;
    private Sprite sprite;
    private SpriteBatch batch;

    private boolean hasFlippedL = false;
    private boolean hasFLippedR = false;
    private Projectiles projectiles;

    private Sound shootSound;
    private Sound hitSound;

    private Body body;

    private int hp;
    private final float SPEED = 400;

    private CollisionRect collisionRect;

    //animations
    private Texture current;
    private boolean flipX;
    private float animationState;
    private Texture frame1, frame2, frame3, frame4, frame5, frame6;
    private Animation walk, idle, hit, shoot;

    public Player(World world, float x, float y) {
        this.body = createBox(world, x, y, (int)PPM, (int)PPM, false);
        this.texture = new Texture("player/monkey16p.png");
        this.sprite = new Sprite(texture);
        this.batch = new SpriteBatch();
        this.projectiles = new Projectiles();
        hp = 9;

        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shootsound.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));

        this.collisionRect = new CollisionRect(body.getPosition().x * PPM, body.getPosition().y * PPM, 16, 16);

        this.flipX = false;
        initAnimations();
    }

    public void update(float delta) {
        Vector2 force = new Vector2(0, 0);

        current = (Texture) idle.getKeyFrame(animationState, false);

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            force.x -= 1;
            if (!hasFlippedL) {
                this.sprite.flip(true, false);
                hasFlippedL = true;
                hasFLippedR = false;
            }
            this.flipX = true;
            current = (Texture) walk.getKeyFrame(animationState, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            force.x += 1;
            if (!hasFLippedR) {
                this.sprite.flip(true, false);
                hasFlippedL = false;
                hasFLippedR = true;
            }
            this.flipX = false;
            current = (Texture) walk.getKeyFrame(animationState, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            force.y += 1;

            current = (Texture) walk.getKeyFrame(animationState, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            force.y -= 1;

            current = (Texture) walk.getKeyFrame(animationState, true);
        }

        body.setLinearVelocity(force.nor().x * (SPEED * delta), body.getLinearVelocity().y);
        body.setLinearVelocity(body.getLinearVelocity().x, force.nor().y * (SPEED * delta));

        updateCollisionRect();
        animationState += delta;
    }

    public void render(Batch batch) {
        float width = PPM;
        float height = PPM;
        batch.begin();
        batch.draw(current, body.getPosition().x * PPM-(width/2), body.getPosition().y * PPM-(height/2), width, height, 0, 0, (int)width, (int)height, isFlipX(), false);
        batch.end();
    }

    public boolean isFlipX() {
        return flipX;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void dispose() {
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
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_ENEMY_PROJECTILE | BIT_WALL;
        fixtureDef.filter.groupIndex = 1;
        pBody.createFixture(fixtureDef);
        shape.dispose();
        return pBody;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isDead() {
        if (hp <= 0) {
            hp = 0;
            return true;
        } else {
            return false;
        }
    }

    //temp
    float counter = 1;
    boolean canShoot;
    public void shoot(float delta, World world, Camera camera) {
        if (counter * delta > 0.4) {
            if (Gdx.input.isTouched(Input.Buttons.LEFT)) {
                projectiles.projectilesList.add(new Projectile(world, body.getPosition().scl(PPM), getMouseToWorld2D(camera)));
                shootSound.play(1);
                counter = 1;
            }
        } else {
            counter++;
        }
    }

    //Collision rect
    public void updateCollisionRect () {
        this.collisionRect.updatePosition(body.getPosition().x * PPM, body.getPosition().y * PPM);
        //System.out.println("PLAYER:" + collisionRect.getX() + ", " + collisionRect.getY() + ", " + collisionRect.getX() + collisionRect.getWidth() + ", " + collisionRect.getX() + collisionRect.getHeight());
    }

    public void collisionWith (CollisionRect contact) {
        if (collisionRect.collisionWith(contact)) {
            setHp(hp -= 1);
            hitSound.play(0.5f);
            current = (Texture) hit.getKeyFrame(animationState, true);
            System.out.println(getHp());
        }
    }

    public CollisionRect getCollisionRect() {
        return collisionRect;
    }

    public Vector2 getMouseToWorld2D (Camera camera) {
        Vector2 mouse = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        Vector3 mouseToWorld = new Vector3(camera.unproject(new Vector3(mouse.x, mouse.y, 0)));
        mouse.x = mouseToWorld.x;
        mouse.y = mouseToWorld.y;
        return mouse;
    }

    private void initAnimations() {
        animationState = 0;

        frame1 = new Texture("player/player1.png");
        frame2 = new Texture("player/player2.png");
        frame3 = new Texture("player/player3.png");
        frame4 = new Texture("player/player4.png");
        frame5 = new Texture("player/player5.png");
        frame6 = new Texture("player/player6.png");

        walk = new Animation(.170f, frame5, frame6);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        idle = new Animation(0, frame1);

        shoot = new Animation(.170f, frame3, frame4);
        shoot.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        hit = new Animation(.170f, frame2, frame2, frame2);
        hit.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        current = (Texture)idle.getKeyFrame(animationState);
    }
}
