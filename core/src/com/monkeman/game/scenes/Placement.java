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