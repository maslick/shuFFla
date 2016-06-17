package com.maslick.kosmosfm.api


import groovy.util.logging.Slf4j
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by maslick on 17/06/16.
 */
@Slf4j
@RestController
@SpringBootApplication
public class Apishechka {
    public static final int DEFAULT_PADDING = 50

    public static void main(String[] args) {
        println 'Starting'.center(DEFAULT_PADDING, '=')
        SpringApplication.run Apishechka, args
        println 'Started'.center(DEFAULT_PADDING, '=')
    }

    private AtomicInteger pid = new AtomicInteger(Integer.MAX_VALUE)

    @RequestMapping(value = '/test', method = RequestMethod.GET)
    def rent(@RequestParam(value = "count")Optional<Integer> count) {
        return [ "count" : pid.addAndGet(-1 * count.orElse(1)) ]
    }
}