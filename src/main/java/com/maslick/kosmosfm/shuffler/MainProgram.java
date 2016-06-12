package com.maslick.kosmosfm.shuffler;


/**
 * Created by maslick on 12/06/16.
 */
public class MainProgram {

    public static void main(String[] args) throws Exception {
        Shuffler sh = new Shuffler();
        sh.setResourceDir("/Users/maslick/pmaslov/HOME/sandbox/shuffler/resources/");
        sh.saveToDb();

        sh.setOutputDir(System.getProperty("user.dir") + "/hello");
        sh.createPlaylist();

        sh.setResourceDir("/Users/maslick/pmaslov/HOME/sandbox/shuffler/resources/");
        for(int i=1;i<=5;i++) {
            sh.setOutputDir(System.getProperty("user.dir") + "/hello/" + i);
            sh.createPlaylistWithRhythm(Integer.toString(i));
        }
    }
}
