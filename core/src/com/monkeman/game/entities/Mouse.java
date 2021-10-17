package com.monkeman.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;

public class Mouse {
    private final Vector2 position;
    private Pixmap pixmap;
    private Cursor cursor;

    private int width = Gdx.graphics.getWidth();
    private int height = Gdx.graphics.getHeight();

    public Mouse() {
        this.position = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        this.pixmap = new Pixmap(Gdx.files.internal("ui/crosshair.png"));
        this.cursor = Gdx.graphics.newCursor(pixmap, pixmap.getWidth() / 2, pixmap.getHeight() / 2);

    }

    public void render() {
        this.pixmap = new Pixmap(Gdx.files.internal("ui/crosshair.png"));
        this.cursor = Gdx.graphics.newCursor(pixmap, pixmap.getWidth() / 2, pixmap.getHeight() / 2);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();
    }

    public void update() {
        //constains the mouse inside the current windows size
        if (position.x >= width) {
            Gdx.input.setCursorPosition(width-1, (int)position.y);
        }
        if (position.x <= width-width) {
            Gdx.input.setCursorPosition(width-width+1, (int)position.y);
        }
        if (position.y >= height) {
            Gdx.input.setCursorPosition((int)position.x, height-1);
        }
        if (position.y <= height-height) {
            Gdx.input.setCursorPosition((int)position.x, height-height+1);
        }

        position.x = Gdx.input.getX();
        position.y = Gdx.input.getY();

        //gets the resized window and updates the mouse position
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
    }

    public Vector2 getPosition() {
        return position;
    }
}
