package com.amosyo.serv.user.web.runner;

import org.eclipse.microprofile.config.Config;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ConfigRunner {

    private static final Logger logger = Logger.getLogger(ConfigRunner.class.getName());

    @Resource(name = "bean/config")
    private Config config;

    @PostConstruct
    public void init() {
        logger.log(Level.INFO, "Current Application Name is " + config.getValue("application.name", String.class));
    }
}
