package com.monkeman.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.Align;
import com.monkeman.game.Score;

import java.io.IOException;

public class GameOverScreen implements Screen {

    private static int BANNER_WIDTH = 1280;
    private static int BANNER_HEIGHT = 720;

    SceneSettings game;
    Placement placement;

    int score, highScore;

    Texture gameOverBanner;
    BitmapFont scoreFont;
    public Music music;
    public Sound win;

    public GameOverScreen(SceneSettings game, int score) throws IOException {
        this.game = game;
        this.score = Score.getScore();
        this.highScore = Score.getHighScore();


        Preferences prefs = Gdx.app.getPreferences(("SceneSetting"));

        if (score > highScore) {
            prefs.putInteger("highScore", score);
            prefs.flush();
        }
        gameOverBanner = new Texture("ui/GameOver.png");
        scoreFont = new BitmapFont(Gdx.files.internal("ui/Fonts/score.fnt"));
        music = Gdx.audio.newMusic(Gdx.files.internal("music/gameover.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
        Placement newPlacement = new Placement(score);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();

        game.batch.draw(gameOverBanner, Gdx.graphics.getWidth() / 2 - BANNER_WIDTH / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT, BANNER_WIDTH, BANNER_HEIGHT);

        GlyphLayout scoreLayout = new GlyphLayout(scoreFont, " Your Score:\n " + score, Color.YELLOW, 0, Align.left, false);
        GlyphLayout highscoreLayout = new GlyphLayout(scoreFont, "Highscore:\n" + highScore, Color.YELLOW, 0, Align.left, false);
        scoreFont.draw(game.batch, scoreLayout, Gdx.graphics.getWidth() / 2 - scoreLayout.width / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT + 460);
        scoreFont.draw(game.batch, highscoreLayout, Gdx.graphics.getWidth() / 2 - highscoreLayout.width / 2, Gdx.graphics.getHeight() - BANNER_HEIGHT + scoreLayout.height + 475);

        GlyphLayout tryAgainLayout = new GlyphLayout(scoreFont, "Try Again");
        GlyphLayout mainMenuLayout = new GlyphLayout(scoreFont, "MainMenu");

        float tryAgainX = Gdx.graphics.getWidth() / 2 - tryAgainLayout.width / 2;
        float tryAgainY = Gdx.graphics.getHeight() / 2 - tryAgainLayout.height / 2 - 212;

        float mainMenuX = Gdx.graphics.getWidth() / 2 - mainMenuLayout.width / 2;
        float mainMenuY = Gdx.graphics.getHeight() / 2 - mainMenuLayout.height / 2 - 255;

        float touchX = Gdx.input.getX(), touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if (Gdx.input.isTouched()) {
            if (touchX > tryAgainX && touchX < tryAgainX + tryAgainLayout.width && touchY > tryAgainY - tryAgainLayout.height && touchY < tryAgainY) {
                music.dispose();
                this.dispose();
                game.batch.end();
                game.setScreen(new GameScreen(game));
                return;
            }
            if (touchX > mainMenuX && touchX < mainMenuX + mainMenuLayout.width && touchY > mainMenuY - mainMenuLayout.height && touchY < mainMenuY) {
                music.dispose();
                this.dispose();
                game.batch.end();
                game.setScreen(new MainMenu(game));
                return;
            }
        }
        scoreFont.draw(game.batch, tryAgainLayout, tryAgainX, tryAgainY);
        scoreFont.draw(game.batch, mainMenuLayout, mainMenuX, mainMenuY);
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {


    }
}
