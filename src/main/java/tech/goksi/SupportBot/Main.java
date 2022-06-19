package tech.goksi.supportbot;

import tech.goksi.supportbot.config.Config;



public class Main {

    public static void main(String[] args) {
        Config config = new Config();
        config.initConfig();
        Bot bot = new Bot(config);
        bot.init();
    }
}
