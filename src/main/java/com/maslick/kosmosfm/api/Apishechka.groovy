package com.maslick.kosmosfm.api

import com.maslick.kosmosfm.shuffler.Shuffler
import groovy.util.logging.Slf4j
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletResponse

/**
 * Created by maslick on 17/06/16.
 */
@Slf4j
@RestController
@SpringBootApplication
public class Apishechka {
    public static final int DEFAULT_PADDING = 50
    private Shuffler shuffler

    Apishechka() {
        String resourceDir = System.getenv("KOSMOS_MUSIC")
        if(resourceDir == null || resourceDir.isEmpty()) {
            resourceDir = System.getProperty("user.dir") + "/resources/"
        }
        shuffler = new Shuffler(resourceDir: resourceDir);
    }

    public static void main(String[] args) {
        println 'Starting'.center(DEFAULT_PADDING, '=')
        SpringApplication.run Apishechka, args
        println 'Started'.center(DEFAULT_PADDING, '=')
    }

    @RequestMapping(value = '/getPlsAll', method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    def generateAll() {
        return shuffler.getPlaylistAll()
    }

    @RequestMapping(value = '/getPlsAll.pls', method = RequestMethod.GET)
    def generatAllpls(HttpServletResponse response) {
        response.setContentType("application/pls+xml")
        response.setHeader("Content-disposition", "inline; filename=playlist" + (int) (Math.random() * 1000) + ".pls")

        OutputStream outStream = response.getOutputStream()
        PrintWriter writer = new PrintWriter(outStream)
        writer.write(shuffler.getPlaylistAll())
        writer.close()
    }
}