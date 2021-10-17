package com.monkeman.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadWriteFile {
    public static int highscore = 0;
//    public static void ensureLeaderboardFile() throws IOException{
//        try {
//            File myObj = new File("leaderboard.txt");
//            if (myObj.createNewFile()) {
//                System.out.println("File created: " + myObj.getName());
//            }
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//    }

    public static void readLeaderboardFile() throws IOException{
        Placement.placementList.clear();
        String[] lineData;
        FileHandle myObj = Gdx.files.internal("ui/leaderboard.txt");
        Scanner scan = new Scanner(myObj.read());

        int i = 0;
         while (scan.hasNextLine()) {
             lineData = scan.nextLine().split(",");
             Placement placement = new Placement(Integer.parseInt(lineData[0]), lineData[1], Integer.parseInt(lineData[2]));
             if (highscore < Integer.parseInt(lineData[2])) {
                 highscore = Integer.parseInt(lineData[2]);
             }
             ArrayList<Placement> placementList = placement.getPlacementList();
             placementList.add(placement);
             i++;
         }
    }

    public static void savePlacement(Placement place){
        BufferedWriter bw = null;
        try {
            FileHandle myObj = Gdx.files.local("ui/leaderboard.txt");
            bw = new BufferedWriter(new OutputStreamWriter(myObj.write(true)));
            bw.write(place.toString());
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
