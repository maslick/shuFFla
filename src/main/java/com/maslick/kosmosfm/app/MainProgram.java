package com.maslick.kosmosfm.app;


import com.maslick.kosmosfm.shuffler.Shuffler;

/**
 * Created by maslick on 12/06/16.
 */
public class MainProgram {

    public static void main(String[] args) throws Exception {
        String resourceDir = System.getenv("KOSMOS_MUSIC");
        if(resourceDir == null || resourceDir.isEmpty()) {
            resourceDir = System.getProperty("user.dir") + "/resources/";
        }

        // Save files to database
        Shuffler sh = new Shuffler();
        sh.setResourceDir(resourceDir);
        sh.saveToDb();

        // Create all playlists
        sh.setOutputDir(System.getProperty("user.dir") + "/lists");
        sh.createPlaylist();

        // Create 5 playlists based off rhythm
        for(int i=1;i<=5;i++) {
            sh.setOutputDir(System.getProperty("user.dir") + "/lists/" + i);
            sh.createPlaylistWithRhythm(Integer.toString(i));
        }
    }
}
