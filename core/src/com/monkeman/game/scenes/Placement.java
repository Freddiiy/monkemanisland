package com.monkeman.game.scenes;

import java.util.ArrayList;

class Placement {
    public static ArrayList<Placement> placementList = new ArrayList<Placement>();


    public void create(){

    }

    public int rank = 0;
    public String name = "asd";
    public int score = 10000;

    public static void setPlacementList(ArrayList<Placement> placementList) {
    }


    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Placement(int rank, String name, int score) {
        this.rank = rank;
        this.name = name;
        this.score = score;

    }


    public ArrayList<Placement> getPlacementList() {
        return placementList;
    }


//    @Override
//    public void show() {
//
//    }
//
//    @Override
//    public void render(float delta) {
//
//
//            Gdx.gl.glClearColor(0,0,0,1);
//            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//
//        game.batch.begin();
//
//        scoreFont.draw(game.batch,"RANK",Gdx.graphics.getWidth()/100*27,Gdx.graphics.getHeight()/100*99);
   //   font.draw(game.batch,"RANK",Gdx.graphics.getWidth()/100*27,Gdx.graphics.getHeight()/100*99);
   //   font.draw(game.batch,"RANK",Gdx.graphics.getWidth()/100*27,Gdx.graphics.getHeight()/100*99);
   //   font.getData().setScale(4);
   //   font.draw(batch,"NAME",Gdx.graphics.getWidth()/100*47,Gdx.graphics.getHeight()/100*99);
   //   font.draw(batch,"SCORE",Gdx.graphics.getWidth()/100*67,Gdx.graphics.getHeight()/100*99);
  /*     font.getData().setScale(10);
        font.draw(batch,"placement",Gdx.graphics.getWidth()/100*7,Gdx.graphics.getHeight()/100*30);

        font.getData().setScale(1);
        font.draw(batch,"L to toggle placement",Gdx.graphics.getWidth()/100*94,Gdx.graphics.getHeight()/100*3);



        font.getData().setScale(2);

        int spaceBetweenPlacement = Gdx.graphics.getHeight()/100;
        int standardHeight = Gdx.graphics.getHeight()/100*89;


        for (int i = 0; i < placementList.size(); i++) {

            font.draw(batch,placementList.get(i).rank,Gdx.graphics.getWidth()/100*30,standardHeight);
            font.draw(batch,placementList.get(i).name,Gdx.graphics.getWidth()/100*50,standardHeight);
            font.draw(batch,String.valueOf(placementList.get(i).score),Gdx.graphics.getWidth()/100*70,standardHeight);
            standardHeight = standardHeight - spaceBetweenPlacement*8;

            if (i == 4)
                break;
        }*/
//        game.batch.end();
//
//
//
//        }

    @Override
    public String toString() {
        return  rank +
                "," + name +
                "," + score
                ;
    }

    public Placement() {
        this.rank = 0;
        this.name = "name";
        this.score = 0;
    }

    public Placement(int score) {
        rank = 0;
        name = "Abenym";
        this.score = score;

    }
}