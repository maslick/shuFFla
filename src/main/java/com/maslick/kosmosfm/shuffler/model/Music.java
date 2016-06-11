package com.maslick.kosmosfm.shuffler.model;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by maslick on 11/06/16.
 */


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries({
    @NamedQuery(name="Music.findAll", query = "SELECT a FROM Music a"),
    @NamedQuery(name="Music.findByName", query = "SELECT OBJECT(a) FROM Music a WHERE a.filename = :filename"),
    @NamedQuery(name="Music.findByFullPath", query = "SELECT OBJECT(a) FROM Music a WHERE a.fullpath = :fullpath")
})
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String filename;
    private String fullpath;
    private String artist;
    private String song;
    private String detail;
    private long length;
}
