package com.monkeman.game.scenes;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.monkeman.game.CameraController;
import com.monkeman.game.Score;
import com.monkeman.game.UI;
import com.monkeman.game.entities.Mouse;
import com.monkeman.game.entities.Player;
import com.monkeman.game.entities.Projectiles;
import com.monkeman.game.entities.enemy.EnemyHandler;
import com.monkeman.game.entities.enemy.EnemyProjectiles;
import com.monkeman.game.utils.TileObject;


import java.io.IOException;

import static com.monkeman.game.utils.Constants.*;

public class GameScreen extends ApplicationAdapter implements Screen{
	SceneSettings game;

	private final float SCALE = 4;
	private final UI ui;

	private final Player player;

	private final OrthographicCamera camera;
	private final Box2DDebugRenderer b2dr;
	private ContactListener contact;
	private final World world;

	// texture render
	public SpriteBatch batch;

	private final Projectiles projectiles;
	private final EnemyProjectiles enemyProjectiles;
	private final EnemyHandler enemyHandler;

	//load map
	private final TiledMap mapLoader;
	private final OrthogonalTiledMapRenderer tileMap;

	//TEMP MOUSE TO WORLD POS HERE
	private final Mouse mouse;

	Music music;

	public GameScreen(SceneSettings game) {
		this.game = game;
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width / SCALE, height / SCALE);

		world = new World(new Vector2(0, 0), false);
		boolean DEBUG = false;
		b2dr = new Box2DDebugRenderer(DEBUG, DEBUG, DEBUG, DEBUG, DEBUG, DEBUG);

		player = new Player(world, 968, 954);

		enemyHandler = new EnemyHandler(1);

		batch = new SpriteBatch();

		mapLoader = new TmxMapLoader().load("map/NewMap.tmx");
		tileMap = new OrthogonalTiledMapRenderer(mapLoader);
		TileObject.parseTileObject(world, mapLoader.getLayers().get("collision-boxes").getObjects());

		projectiles = new Projectiles();
		enemyProjectiles = new EnemyProjectiles();

		mouse = new Mouse();

		this.ui = new UI(camera.position, player.getHp(), Score.getScore());

		//reset of game stuff
		Score.setScore(0);
		enemyHandler.setNumberRound(0);
		music = Gdx.audio.newMusic(Gdx.files.internal("music/gamemusic.mp3"));
		music.play();
		music.setLooping(true);
		music.setVolume(0.1f);
	}

	@Override
	public void render(float delta) {
		// Render
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		update(delta);

		tileMap.render();

		player.render(batch, delta);

		b2dr.render(world, camera.combined.scl(PPM));
		appWindow();

		projectiles.render(batch);
		enemyProjectiles.render(batch);
		enemyHandler.render(batch);
		//mouse.render(batch);
		ui.render(player.getHp(), enemyHandler.getNumberRound());
	}

	public void update(float delta) {
		mouse.update();
		world.step(1 / 60f, 6, 2);

		// Mouse Position
		player.update(delta);
		player.shoot(delta, world, camera);

		enemyHandler.update(world, player.getPosition().scl(PPM), delta);

		cameraUpdate(delta);
		tileMap.setView(camera);
		batch.setProjectionMatrix(camera.combined);

		//placeholder position maybe possibly until I get smarter
		projectiles.update(delta);
		projectiles.despawnFirst(world);

		enemyProjectiles.update(delta);
		enemyProjectiles.despawnFirst(world);

		ui.update(camera.position);

		//Collision dumb way dont do

		enemyHandler.collisionWith(projectiles.getCollisionRect(), world);
		for (int i = 0; i < EnemyProjectiles.projectilesList.size(); i++) {
			player.collisionWith(EnemyProjectiles.projectilesList.get(i).getCollisionRect());
		}
		enemyProjectiles.collisionWith(player.getCollisionRect(), world);
		for (int i = 0; i < Projectiles.projectilesList.size(); i++) {
			enemyHandler.collisionWith(Projectiles.projectilesList.get(i).getCollisionRect(), world);
		}

		for (int i = 0; i < EnemyHandler.enemyHandler.size(); i++) {
			projectiles.collisionWith(EnemyHandler.enemyHandler.get(i).getCollisionRect(), world);
		}

		if (player.isDead()) {
			enemyHandler.setNumberOfEnemy(0);
			enemyHandler.setNumberOfEnemyMax(1);
			enemyHandler.setDeadEnemies(0);
			EnemyProjectiles.projectilesList.clear();
			Projectiles.projectilesList.clear();
			EnemyHandler.enemyHandler.clear();
			Score.updateHighScore();
			try {
				music.dispose();
				game.setScreen(new GameOverScreen(game, Score.getScore()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void show() {
		mouse.render();
	}

	@Override
	public void resize(int width, int height) {
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		camera.setToOrtho(false, width / SCALE, height / SCALE);
	}

	@Override
	public void hide() {

	}

	public void cameraUpdate(float delta) {
		CameraController.draw(camera, player.getPosition().scl(PPM));
	}

	public void appWindow() {
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}

	@Override
	public void dispose() {
		world.dispose();
		b2dr.dispose();
		batch.dispose();
		mapLoader.dispose();
		tileMap.dispose();
	}
}