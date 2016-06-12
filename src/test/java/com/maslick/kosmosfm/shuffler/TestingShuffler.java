package com.maslick.kosmosfm.shuffler;

import com.maslick.kosmosfm.shuffler.model.Music;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.Mp3File;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by maslick on 11/06/16.
 */
public class TestingShuffler {
    private static final String PERSISTENCE_UNIT_NAME = "example";
    private EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    private EntityManager em = factory.createEntityManager();

    @Test
    public void loopOverDir() throws Exception {
        String resourceDir = "/Users/maslick/pmaslov/HOME/sandbox/shuffler/resources/";
        File dir = new File(resourceDir);
        File[] directoryListing = dir.listFiles();

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

    @Test
    public void createPlaylist() throws Exception {
        TypedQuery<Music> query = em.createNamedQuery("Music.findAll", Music.class);
        List<Music> list = query.getResultList();

        String outPlsPath = "playlist.pls";
        PrintWriter writer = new PrintWriter(outPlsPath, "UTF-8");
        writer.println("[playlist]");
        int i;
        for (i = 0; i < list.size(); i++) {
            writer.println("File" + i + "=" + list.get(i).getFullpath());
            writer.println("Title" + i + "=" + list.get(i).getSong());
            writer.println("Length" + i + "=" + list.get(i).getLength());
        }
        writer.println("NumberOfEntries=" + i);
        writer.println("Version=2");
        writer.close();
    }
}
