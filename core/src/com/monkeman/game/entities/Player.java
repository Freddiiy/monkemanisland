package com.monkeman.game.entities;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.monkeman.game.utils.CollisionRect;
import com.monkeman.game.utils.Constants;

public class Player {
    private final Sprite sprite;

    private boolean hasFlippedL = false;
    private boolean hasFLippedR = false;

    private final Sound shootSound;
    private final Sound hitSound;

    private final Body body;

    private int hp;

    private final CollisionRect collisionRect;

    //animations
    private Texture current;
    private boolean flipX;
    private float animationState;
    private Animation<Texture> walk;
    private Animation<Texture> idle;
    private Animation<Texture> hit;

    public Player(World world, float x, float y) {
        this.body = createBox(world, x, y, (int) Constants.PPM, (int) Constants.PPM, false);
        Texture texture = new Texture("player/monkey16p.png");
        this.sprite = new Sprite(texture);
        hp = 9;

        shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shootsound.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));

        this.collisionRect = new CollisionRect(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM, 16, 16);

        this.flipX = false;
        initAnimations();
    }

    public void update(float delta) {
        Vector2 force = new Vector2(0, 0);

        current = idle.getKeyFrame(animationState, false);

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            force.x -= 1;
            if (!hasFlippedL) {
                this.sprite.flip(true, false);
                hasFlippedL = true;
                hasFLippedR = false;
            }
            this.flipX = true;
            current = walk.getKeyFrame(animationState, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            force.x += 1;
            if (!hasFLippedR) {
                this.sprite.flip(true, false);
                hasFlippedL = false;
                hasFLippedR = true;
            }
            this.flipX = false;
            current = walk.getKeyFrame(animationState, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            force.y += 1;

            current = walk.getKeyFrame(animationState, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            force.y -= 1;

            current = walk.getKeyFrame(animationState, true);
        }

        float SPEED = 400;
        body.setLinearVelocity(force.nor().x * (SPEED * delta), body.getLinearVelocity().y);
        body.setLinearVelocity(body.getLinearVelocity().x, force.nor().y * (SPEED * delta));

        updateCollisionRect();
        animationState += delta;
    }

    public void render(Batch batch, float delta) {
        update(delta);
        float width = Constants.PPM;
        float height = Constants.PPM;
        batch.begin();
        batch.draw(current, body.getPosition().x * Constants.PPM-(width/2), body.getPosition().y * Constants.PPM-(height/2), width, height, 0, 0, (int)width, (int)height, isFlipX(), false);
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

        def.position.set(x / Constants.PPM, y / Constants.PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = Constants.BIT_PLAYER;
        fixtureDef.filter.maskBits = Constants.BIT_ENEMY_PROJECTILE | Constants.BIT_WALL;
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
                Projectiles.projectilesList.add(new Projectile(world, body.getPosition().scl(Constants.PPM), getMouseToWorld2D(camera)));
                shootSound.play(1);
                counter = 1;
            }
        } else {
            counter++;
        }
    }

    public void updateCollisionRect () {
        this.collisionRect.updatePosition(body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
    }

    public void collisionWith (CollisionRect contact) {
        if (collisionRect.collisionWith(contact)) {
            setHp(hp -= 1);
            hitSound.play(0.5f);
            current = hit.getKeyFrame(animationState, true);
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

        Texture frame1 = new Texture("player/player1.png");
        Texture frame2 = new Texture("player/player2.png");
        Texture frame3 = new Texture("player/player3.png");
        Texture frame4 = new Texture("player/player4.png");
        Texture frame5 = new Texture("player/player5.png");
        Texture frame6 = new Texture("player/player6.png");

        walk = new Animation<>(.170f, frame5, frame6);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        idle = new Animation<>(0, frame1);

        Animation<Texture> shoot = new Animation<>(.170f, frame3, frame4);
        shoot.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        hit = new Animation<>(.170f, frame2, frame2, frame2);
        hit.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        current = idle.getKeyFrame(animationState);
    }
}
