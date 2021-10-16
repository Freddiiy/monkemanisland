package com.groupc.game.entities.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.groupc.game.Score;
import com.groupc.game.utils.CollisionRect;

import static com.groupc.game.utils.Constants.*;

public class Enemy {
    final boolean bodyNullChecker = false;

    private TextureAtlas atlas;
    private final Texture texture;
    private TextureRegion walk1, walk2, walk3, walk4;
    private boolean flipped;
    private boolean shotFinished;
    private boolean isDead = false;

    private final Sound deadSound;
    private final Sound hitSound;

    private final boolean hasFlippedL = false;
    private final boolean hasFLippedR = false;

    float counter = 1;
    float behaviorCounter = 1;

    private int hp;

    private Body body;

    private final CollisionRect collisionRect;

    //animation
    private Texture current;
    private boolean flipX;
    private float animationState;
    private Animation<Texture> walk;
    private Animation<Texture> idle;

    public Enemy(World world, float x, float y) {
        this.body = createBox(world, x, y, (int)PPM, (int)PPM, false);
        this.texture = new Texture("enemy/16b_Banana.png");
        Sprite sprite = new Sprite(texture);
        SpriteBatch batch = new SpriteBatch();

        this.deadSound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemyDead.wav"));
        this.hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/enemyHit.wav"));

        EnemyProjectiles projectiles = new EnemyProjectiles();
        this.hp = 9;

        this.collisionRect = new CollisionRect((this.body.getPosition().x)*PPM, (this.body.getPosition().y)*PPM, 10, 10);

        initAnimations();
    }

    public void render(Batch batch) {
        float width = PPM;
        float height = PPM;
        if (this.body != null) {
            batch.begin();
            batch.draw(current, body.getPosition().x * PPM - (width / 2), body.getPosition().y * PPM - (height / 2), width, height, 0, 0, (int)width, (int)height, isFlipX(), false);
            batch.end();
        }
    }

    public void update(World world, Vector2 target, float delta) {
        if (this.body != null) {
            behavior(world, target, delta);
            updateCollisionRect();
            shoot(world, target, delta);

            animationState += delta;
        }
    }

    double rnd = 0;

    public boolean isFlipX() {
        return flipX;
    }

    public void behavior(World world, Vector2 target, float delta) {
        if (behaviorCounter * delta > 2) {
            rnd = (int)((Math.random() * (4)) + 0);
            behaviorCounter =1;
        } else {
            behaviorCounter++;
        }
        float SPEED = 200;
        if (rnd == 0 || rnd == 1) {
            Vector2 direction = new Vector2(target.x - (body.getPosition().x * PPM), target.y - (body.getPosition().y) * PPM);

            body.setLinearVelocity(direction.nor().x * (SPEED * delta), body.getLinearVelocity().y);
            body.setLinearVelocity(body.getLinearVelocity().x, direction.nor().y * (SPEED * delta));

            current = walk.getKeyFrame(animationState, true);
        } else if (rnd == 2) {
            body.setLinearVelocity(0, 0);

            current = idle.getKeyFrame(animationState, false);
        }else if (rnd == 3) {
            Vector2 direction = new Vector2(target.x - (body.getPosition().x * PPM), target.y - (body.getPosition().y) * PPM);

            body.setLinearVelocity(-direction.nor().x * (SPEED * delta), body.getLinearVelocity().y);
            body.setLinearVelocity(body.getLinearVelocity().x, -direction.nor().y * (SPEED * delta));

            current = walk.getKeyFrame(animationState, true);
        }

        //flip the banana
        if (this.body.getPosition().x * PPM< target.x) {
            flipX = true;
        } else if (this.body.getPosition().x * PPM >= target.x) {
            flipX = false;
        }
    }

    public void shoot(World world, Vector2 target, float delta) {
        if (counter * delta > (float)((Math.random() * (6.0f-1.0f)) + 1.0f)) {
            EnemyProjectiles.projectilesList.add(new EnemyProjectile(world, body.getPosition().scl(PPM), target));
            counter = 1;
        } else {
            counter++;
        }
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public void setPosition(Vector2 position) {
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
        fixtureDef.filter.categoryBits = BIT_ENEMY;
        fixtureDef.filter.maskBits = BIT_PROJECTILE | BIT_WALL;
        fixtureDef.filter.groupIndex = 1;
        pBody.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        return pBody;
    }

    public void updateCollisionRect () {
        this.collisionRect.updatePosition((this.body.getPosition().x)*PPM, (this.body.getPosition().y)*PPM);
    }

    public void collisionWith (CollisionRect contact) {
        if (this.collisionRect.collisionWith(contact)) {
            this.hp--;
            hitSound.play(0.5f);
        }
    }

    public CollisionRect getCollisionRect() {
        return this.collisionRect;
    }

    public void despawn(World world) {
        if (isDead) {
            if (body != null) {
                world.destroyBody(this.body);
                texture.dispose();
                this.body = null;
            }
        }
    }

    public boolean isDead() {
        if (this.hp <= 0) {
            Score.setScore(Score.getScore() + 50);
            deadSound.play(0.5f);
            return true;
        } else {
            return false;
        }
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    private void initAnimations() {
        animationState = 0;

        Texture frame1 = new Texture("enemy/enemy1.png");
        Texture frame2 = new Texture("enemy/enemy2.png");
        Texture frame3 = new Texture("enemy/enemy3.png");
        Texture frame4 = new Texture("enemy/enemy4.png");
        Texture frame5 = new Texture("enemy/enemy5.png");
        Texture frame6 = new Texture("enemy/enemy6.png");

        walk = new Animation<>(.170f, frame5, frame6);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        idle = new Animation<>(0, frame1);
        idle.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Animation<Texture> shoot = new Animation<>(.170f, frame3, frame4);
        shoot.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        Animation<Texture> hit = new Animation<>(.170f, frame2, frame2, frame2);
        hit.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        current = idle.getKeyFrame(animationState);
    }
}
