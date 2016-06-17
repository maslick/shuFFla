package com.maslick.kosmosfm.shuffler;

import com.maslick.kosmosfm.model.Music;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import lombok.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by maslick on 11/06/16.
 */



@Data
@NoArgsConstructor
public class Shuffler {
    private static final String PERSISTENCE_UNIT_NAME = "example";

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private EntityManager em = factory.createEntityManager();

    private String resourceDir;
    private String outputDir;

    public void saveToDb() throws Exception {
        File[] directoryListing = new File(resourceDir).listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (!child.getName().contains(".mp3"))
                    continue;

                // Get mp3 metadata
                Mp3File mp3file = new Mp3File(child.getCanonicalFile());
                ID3v1 id3v1Tag = mp3file.getId3v1Tag();
                if (id3v1Tag == null) {
                    id3v1Tag = mp3file.getId3v2Tag();
                }
                if (id3v1Tag == null) {
                    continue;
                }

                // Check if already exists
                TypedQuery<Music> query = em.createNamedQuery("Music.findByName", Music.class)
                        .setParameter("filename", child.getName());
                List<Music> list = query.getResultList();
                if (list.size() > 0)
                    continue;


                // Persistence
                Music m = new Music();
                m.setId(null);
                m.setFilename(child.getName());
                m.setFullpath(child.getAbsolutePath());
                m.setArtist(id3v1Tag.getArtist());
                m.setSong(id3v1Tag.getTitle());
                m.setDetail(id3v1Tag.getComment());
                m.setLength(mp3file.getLengthInSeconds());
                em.getTransaction().begin();
                em.persist(m);
                em.getTransaction().commit();
            }
        }
    }

    private String generatePlaylist(List<Music> list) {
        String res = "";

        if (list.size() == 0 ) return res;

        res += "[playlist]\n";

        int i;
        for (i = 0; i < list.size(); i++) {
            res += "File" + i + "=" + list.get(i).getFullpath() + "\n";
            res += "Title" + i + "=" + list.get(i).getSong() + "\n";
            res += "Length" + i + "=" + list.get(i).getLength() + "\n";
        }
        res += "NumberOfEntries=" + i + "\n";
        res += "Version=2";
        return res;
    }

    public String getPlaylistAll() throws Exception {
        TypedQuery<Music> query = em.createNamedQuery("Music.findAll", Music.class);
        List<Music> list = sortPlaylist(query.getResultList());

        return generatePlaylist(list);
    }

    public void createPlaylist() throws Exception {
        String outPlsPath = outputDir + "/playlist.pls";
        createDir(outputDir);
        PrintStream out = new PrintStream(new FileOutputStream(outPlsPath));
        out.print(getPlaylistAll());
        out.close();
    }

    public String getPlaylistWithRhythm(String rhythm) throws Exception {
        TypedQuery<Music> query = em.createNamedQuery("Music.findByRythm", Music.class)
                .setParameter("detail", rhythm);
        List<Music> list = sortPlaylist(query.getResultList());

        return generatePlaylist(list);
    }

    public void createPlaylistWithRhythm(String rhythm) throws Exception {
        String outPlsPath = outputDir + "/playlist" + rhythm + ".pls";
        createDir(outputDir);
        PrintStream out = new PrintStream(new FileOutputStream(outPlsPath));
        out.print(getPlaylistWithRhythm(rhythm));
        out.close();
    }

    public List<Music> sortPlaylist(List<Music> list) {
        // implementation TODO
        Collections.shuffle(list);
        return list;
    }

    private void createDir(String directoryName) {
        File theDir = new File(directoryName);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + directoryName);
            boolean result = false;

            try{
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se){
                //handle it
            }
            if(result) {
                System.out.println("DIR created");
            }
        }
    }
}
