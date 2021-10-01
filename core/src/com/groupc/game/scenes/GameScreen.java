package com.groupc.game.scenes;

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
import com.groupc.game.CameraController;
import com.groupc.game.Score;
import com.groupc.game.UI;
import com.groupc.game.entities.*;
import com.groupc.game.entities.enemy.EnemyHandler;
import com.groupc.game.entities.enemy.EnemyProjectiles;
import com.groupc.game.utils.TileObject;


import java.io.IOException;

import static com.groupc.game.utils.Constants.*;

public class GameScreen extends ApplicationAdapter implements Screen{
	SceneSettings game;

	private boolean DEBUG = false;
	private final float SCALE = 4;
	private UI ui;

	private Player player;

	private OrthographicCamera camera;
	private Box2DDebugRenderer b2dr;
	private ContactListener contact;
	private World world;
	//private Body player;

	// texture render
	public SpriteBatch batch;
	private Sprite playerSprite;
	private Texture playerTex;

	private Projectiles projectiles;
	private EnemyProjectiles enemyProjectiles;
	private EnemyHandler enemyHandler;

	//load map
	private TiledMap mapLoader;
	private OrthogonalTiledMapRenderer tileMap;

	//TEMP MOUSE TO WORLD POS HERE
	private Vector3 mouseToWorld;
	private Vector2 mouseToWorld2D;
	private Mouse mouse;

	Music music;

	public GameScreen(SceneSettings game) {
		this.game = game;
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width / SCALE, height / SCALE);

		world = new World(new Vector2(0, 0), false);
		b2dr = new Box2DDebugRenderer(DEBUG, DEBUG, DEBUG, DEBUG, DEBUG, DEBUG);

		player = new Player(world, 968, 954);

		//placeholder enemy
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

		update(Gdx.graphics.getDeltaTime());

		tileMap.render();

		player.render(batch);

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
		//tileMap.setView(camera.combined, -300, -300, 2000, 2000);
		batch.setProjectionMatrix(camera.combined);

		//placeholder position maybe possibly until I get smarter
		projectiles.update(delta);
		projectiles.despawnFirst(world);

		enemyProjectiles.update(delta);
		enemyProjectiles.despawnFirst(world);

		ui.update(camera.position);

		//Collision dumb way dont do

		enemyHandler.collisionWith(projectiles.getCollisionRect(), world);
		for (int i = 0; i < enemyProjectiles.projectilesList.size(); i++) {
			player.collisionWith(enemyProjectiles.projectilesList.get(i).getCollisionRect());
		}
		enemyProjectiles.collisionWith(player.getCollisionRect(), world);
		for (int i = 0; i < projectiles.projectilesList.size(); i++) {
			enemyHandler.collisionWith(Projectiles.projectilesList.get(i).getCollisionRect(), world);
		}

		for (int i = 0; i < enemyHandler.enemyHandler.size(); i++) {
			projectiles.collisionWith(EnemyHandler.enemyHandler.get(i).getCollisionRect(), world);
		}
		//Collision end+

		if (player.isDead()) {
			enemyHandler.setNumberOfEnemy(0);
			enemyHandler.setNumberOfEnemyMax(1);
			enemyHandler.setDeadEnemies(0);
			enemyProjectiles.projectilesList.clear();
			projectiles.projectilesList.clear();
			enemyHandler.enemyHandler.clear();
			Score.updateHighScore();
			try {
				music.dispose();
				game.setScreen(new GameOverScreen(game, Score.getScore()));
			} catch (IOException e) {
				e.printStackTrace();
			}

			return;
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
		boolean isFullscreen = false;
		if(Gdx.input.isKeyJustPressed(Input.Keys.O) && !isFullscreen) {
			//isFullscreen = true;
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		}
//		if (Gdx.input.isKeyJustPressed(Input.Keys.O) && isFullscreen) {
//			Gdx.graphics.setWindowedMode(1280, 720);
//			isFullscreen = false;
//		}
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