package com.groupc.game.scenes;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class SceneSettings extends Game {
public static final int WIDTH = 400;
public static final int HEIGHT = 600;

	 public SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//this.setScreen((Screen) new GameScreen(this));
		this.setScreen(new MainMenu(this));
	}

	@Override
	public void render () {
	super.render();
	}
	
	@Override
	public void dispose () {
	}
}
