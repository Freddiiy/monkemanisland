package com.monkeman.game.scenes;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Collections;

public class MainMenu implements Screen {
    private static final int EXIT_BUTTON_WIDTH = 200;
    private static final int EXIT_BUTTON_HEIGHT = 100;
    private static final int PLAY_BUTTON_HEIGHT = 100;
    private static final int PLAY_BUTTON_WIDTH = 200;
    private static final int LEADERBOARD_BUTTON_HEIGHT = 100;
    private static final int LEADERBOARD_BUTTON_WIDTH = 200;
    private static final int PLAY_BUTTON_Y = 400;
    private static final int EXIT_BUTTON_Y = 100;
    private static final int LEADERBOARD_BUTTON_Y = 250;

    private static final int BACKGROUND_WIDTH = 1280;
    private static final int BACKGROUND_HEIGHT = 1280;

    SceneSettings game;
    Batch batch;
    BitmapFont font;
    Placement placement;


    public void create(){
        batch = new SpriteBatch();
        font = new BitmapFont();
        placement = new Placement();
        font.getData().setScale(3);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        //java.lang.Runnable     ArrayList<Placement> placementList = placement.getPlacementList();
    }

    public void renderLeaderboard() {

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.getData().setScale(4);
        font.draw(batch,"RANK",Gdx.graphics.getWidth()/100*27,Gdx.graphics.getHeight()/100*99);
        font.draw(batch,"NAME",Gdx.graphics.getWidth()/100*47,Gdx.graphics.getHeight()/100*99);
        font.draw(batch,"SCORE",Gdx.graphics.getWidth()/100*67,Gdx.graphics.getHeight()/100*99);

        font.getData().setScale(10);
        font.draw(batch,"LEADERBOARD",Gdx.graphics.getWidth()/100*7,Gdx.graphics.getHeight()/100*30);

        font.getData().setScale(1);
        font.draw(batch,"L to toggle placement",Gdx.graphics.getWidth()/100*94,Gdx.graphics.getHeight()/100*3);

        ArrayList<Placement> placementList = placement.getPlacementList();
        ArrayList<Placement> topList = new ArrayList<>();

        topList = placementList;

        for (int i = 0; i < placementList.size()-1; i++) {
            if (placementList.get(i).score < placementList.get(i+1).score){
                Collections.swap(topList,i+1,i);

            }
            placementList.get(i).setRank(i+1);
        }

   //     System.out.println(topList.size());

        font.getData().setScale(2);

        int spaceBetweenPlacement = Gdx.graphics.getHeight()/100;
        int standardHeight = Gdx.graphics.getHeight()/100*89;


        font.draw(batch,"ST",(float) (Gdx.graphics.getWidth()/100*31.5),standardHeight);
        font.draw(batch,"ND",(float) (Gdx.graphics.getWidth()/100*31.7),standardHeight-spaceBetweenPlacement*8);
        font.draw(batch,"RD",(float) (Gdx.graphics.getWidth()/100*31.7),standardHeight-spaceBetweenPlacement*16);
        font.draw(batch,"TH",(float) (Gdx.graphics.getWidth()/100*31.7),standardHeight-spaceBetweenPlacement*24);
        font.draw(batch,"TH",(float) (Gdx.graphics.getWidth()/100*31.7),standardHeight-spaceBetweenPlacement*32);


        for (int i = 0; i < placementList.size(); i++) {

            font.draw(batch,String.valueOf(topList.get(i).rank),Gdx.graphics.getWidth()/100*30,standardHeight);
            font.draw(batch,topList.get(i).name,Gdx.graphics.getWidth()/100*49,standardHeight);
            font.draw(batch,String.valueOf(topList.get(i).score),Gdx.graphics.getWidth()/100*73,standardHeight);
            standardHeight = standardHeight - spaceBetweenPlacement*8;

            if (i == 4)
                break;
        }
        batch.end();
    }

    Texture playButtonActive;
    Texture playButtonInactive;
    Texture leaderBoardButtonActive;
    Texture leaderBoardButtonInactive;
    Texture exitButtonActive;
    Texture exitButtonInactive;
    Texture menuBackground;

    Music music;
    public MainMenu(SceneSettings game)
    {
        this.game = game;
        playButtonActive = new Texture("ui/PlayButton.png");
        playButtonInactive = new Texture("ui/PlayButtonDark.png");
        leaderBoardButtonActive = new Texture("ui/LeaderboardButton.png");
        leaderBoardButtonInactive= new Texture("ui/LeaderboardButtonDark.png");
        exitButtonActive= new Texture("ui/ExitButton.png");
        exitButtonInactive = new Texture("ui/ExitButtonDark.png");
        menuBackground = new Texture("ui/background.png");

        this.music = Gdx.audio.newMusic(Gdx.files.internal("music/mainmenu.mp3"));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        int x;

        music.play();
        music.setLooping(true);
        music.setVolume(0.5f);

        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        create();
        game.batch.begin();
        //game.batch.draw(menuBackground,Gdx.graphics.getWidth()-Gdx.graphics.getWidth(),Gdx.graphics.getHeight()-Gdx.graphics.getHeight());
        game.batch.draw(menuBackground,Gdx.graphics.getWidth()/2 - BACKGROUND_WIDTH/2, Gdx.graphics.getHeight() - BACKGROUND_HEIGHT, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

        x = (Gdx.graphics.getWidth() / 2) - (EXIT_BUTTON_WIDTH / 2);
        if (Gdx.input.getX() < x + EXIT_BUTTON_WIDTH && Gdx.input.getX() > x && Gdx.graphics.getHeight() - Gdx.input.getY() < EXIT_BUTTON_Y + EXIT_BUTTON_HEIGHT && Gdx.graphics.getHeight() - Gdx.input.getY() > EXIT_BUTTON_Y) {
            game.batch.draw(exitButtonInactive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);

            if(Gdx.input.isTouched()) {
                Gdx.app.exit();
            }
        } else {
            game.batch.draw(exitButtonActive, x, EXIT_BUTTON_Y, EXIT_BUTTON_WIDTH, EXIT_BUTTON_HEIGHT);
        }

        x = (Gdx.graphics.getWidth() / 2) - (PLAY_BUTTON_WIDTH / 2);
        if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && Gdx.graphics.getHeight() - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && Gdx.graphics.getHeight() - Gdx.input.getY() > PLAY_BUTTON_Y) {
            game.batch.draw(playButtonInactive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);

            if(Gdx.input.isTouched()) {
                music.dispose();
                this.dispose();
                game.setScreen(new GameScreen(game));
            }
        } else {
            game.batch.draw(playButtonActive, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        }

        x = (Gdx.graphics.getWidth() / 2) - (LEADERBOARD_BUTTON_WIDTH / 2);
        if (Gdx.input.getX() < x + LEADERBOARD_BUTTON_WIDTH && Gdx.input.getX() > x && Gdx.graphics.getHeight() - Gdx.input.getY() < LEADERBOARD_BUTTON_Y + LEADERBOARD_BUTTON_HEIGHT && Gdx.graphics.getHeight() - Gdx.input.getY() > LEADERBOARD_BUTTON_Y) {
            game.batch.draw(leaderBoardButtonInactive, x, LEADERBOARD_BUTTON_Y, LEADERBOARD_BUTTON_WIDTH, LEADERBOARD_BUTTON_HEIGHT);
            if(Gdx.input.isTouched()) {
                this.dispose();
                renderLeaderboard();
            }

        }


        else {
            game.batch.draw(leaderBoardButtonActive, x, LEADERBOARD_BUTTON_Y, LEADERBOARD_BUTTON_WIDTH, LEADERBOARD_BUTTON_HEIGHT);
        }
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
