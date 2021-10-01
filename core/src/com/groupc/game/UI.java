package com.groupc.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.groupc.game.entities.Player;

import static com.groupc.game.utils.Constants.PPM;

public class UI {
    private Vector3 position;
    private int hp;
    private int score;
    private BitmapFont font;
    private GlyphLayout scoreLayout;
    private GlyphLayout hpLayout;
    private GlyphLayout roundNumberLayout;
    private SpriteBatch batch;

    public UI(Vector3 position, int hp, int score) {
        this.position = position;
        this.hp = hp;
        this.score = score;
        this.font = new BitmapFont(Gdx.files.internal("ui/Fonts/score.fnt"));
        font.getData().setScale(16/PPM);
        this.batch = new SpriteBatch();
    }

    public void render(int hp, int round) {
        this.roundNumberLayout = new GlyphLayout(font, "Round: " + round, Color.WHITE, PPM, Align.right, false);
        this.hpLayout = new GlyphLayout(font, "HP: " + hp, Color.WHITE, PPM, Align.right, false);
        this.scoreLayout = new GlyphLayout(font, "Score: " + Score.getScore(), Color.WHITE, PPM, Align.left, false);
        batch.begin();
        font.draw(batch, scoreLayout, Gdx.graphics.getWidth() - 400, Gdx.graphics.getHeight() - 20);
        font.draw(batch, hpLayout, Gdx.graphics.getWidth()-Gdx.graphics.getHeight() - 390, 55);
        font.draw(batch, roundNumberLayout, Gdx.graphics.getWidth()-Gdx.graphics.getHeight() - 10, 55);
        batch.end();
    }

    public void update(Vector3 position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
