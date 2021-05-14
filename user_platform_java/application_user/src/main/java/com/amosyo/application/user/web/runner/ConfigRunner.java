package com.amosyo.application.user.web.runner;

import com.amosyo.application.user.config.UserConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * @author : amosyo (amosyo1994@gmail.com)
 * @since : 1.0.0
 **/
public class ConfigRunner {

    private static final Logger logger = Logger.getLogger(ConfigRunner.class.getName());

    @Resource(name = "config/UserConfig")
    private UserConfig userConfig;

    @PostConstruct
    public void init() {
//        logger.log(Level.INFO, "Current Application Name is " + config.getValue("application.name", String.class));
//        logger.log(Level.INFO, "Author is " + config.getValue("author", String.class));
//        this.initUserConfig();
    }

    private void initUserConfig() {
//        Optional.ofNullable(this.config.getValue("user.default.nick", String.class)).ifPresent(this.userConfig::setUserDefaultNick);
//        Optional.ofNullable(this.config.getValue("user.default.avatar", String.class)).ifPresent(this.userConfig::setUserDefaultAvatar);
//        Optional.ofNullable(this.config.getValue("user.default.gender", String.class)).ifPresent(this.userConfig::setUserDefaultGender);
//        Optional.ofNullable(this.config.getValue("user.default.id_step", Integer.class)).ifPresent(this.userConfig::setUserIdStep);
    }
}
