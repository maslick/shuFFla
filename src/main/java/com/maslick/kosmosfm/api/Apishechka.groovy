package com.maslick.kosmosfm.api

import com.maslick.kosmosfm.shuffler.Shuffler
import groovy.util.logging.Slf4j
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
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

    @RequestMapping(value = '/getList', method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    def getListAll() {
        return shuffler.getListAll()
    }

    @RequestMapping(value = '/getList/{rhythm}', method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    def getListWithRhythm(@PathVariable String rhythm) {
        return shuffler.getListWithRhythm(rhythm)
    }

    @RequestMapping(value = '/getPls', method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    def generateAll() {
        return shuffler.getPlaylistAll()
    }

    @RequestMapping(value = '/getPls.pls', method = RequestMethod.GET)
    def generateAllpls(HttpServletResponse response) {
        response.setContentType("application/pls+xml")
        response.setHeader("Content-disposition", "inline; filename=playlist-" + (int) (Math.random() * 1000) + ".pls")

        OutputStream outStream = response.getOutputStream()
        PrintWriter writer = new PrintWriter(outStream)
        writer.write(shuffler.getPlaylistAll())
        writer.close()
    }

    @RequestMapping(value = '/getPls/{rhythm}', method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    def generatePlsWithRhythm(@PathVariable String rhythm) {
        return shuffler.getPlaylistWithRhythm(rhythm)
    }

    @RequestMapping(value = '/getPls/{rhythm}.pls', method = RequestMethod.GET)
    def generatePlsWithRhythm(HttpServletResponse response, @PathVariable String rhythm) {
        response.setContentType("application/pls+xml")
        response.setHeader("Content-disposition", "inline; filename=playlist" + rhythm + "-" + (int) (Math.random() * 1000) + ".pls")

        OutputStream outStream = response.getOutputStream()
        PrintWriter writer = new PrintWriter(outStream)
        writer.write(shuffler.getPlaylistWithRhythm(rhythm))
        writer.close()
    }
}