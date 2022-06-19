package tech.goksi.supportbot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.simpleyaml.configuration.file.YamlFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.supportbot.config.Config;
import tech.goksi.supportbot.tickets.TicketChannel;
import tech.goksi.supportbot.utils.Constants;

import javax.security.auth.login.LoginException;


public class Bot {
    private final Config config;
    private static Bot instance;
    private final Logger logger;
    private JDA jda;
    public Bot(Config config){
        this.config = config;
        instance = this;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void init(){
        if(Constants.TOKEN == null || Constants.GUILD_ID == null || Constants.OWNER_ID == null) exit();
        JDABuilder JDABuilder = net.dv8tion.jda.api.JDABuilder.createDefault(Constants.TOKEN);
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.forceGuildOnly(Constants.GUILD_ID);
        builder.setOwnerId(Constants.OWNER_ID);
        switch (getConfig().getString("Settings.Status")){
            case "DND":
                builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
                break;
            case "IDLE":
                builder.setStatus(OnlineStatus.IDLE);
                break;
            case "INVISIBLE":
                builder.setStatus(OnlineStatus.INVISIBLE);
                break;
            default:
                builder.setStatus(OnlineStatus.ONLINE);
                break;
        }
        String activity = getConfig().getString("Settings.ActivityName");
        switch (getConfig().getString("Settings.Activity")){
            case "PLAYING":
                builder.setActivity(Activity.playing(activity));
                break;
            case "WATCHING":
                builder.setActivity(Activity.watching(activity));
                break;
            case "LISTENING":
                builder.setActivity(Activity.listening(activity));
                break;
        }


        CommandClient client = builder.build();
        try{
            jda = JDABuilder.build();
        }catch (LoginException e){
            logger.error("Bot token you provided is not valid, please check it again !", e);
            System.exit(1);
        }
        jda.addEventListener(client);
        try{
            jda.awaitReady();
        }catch (InterruptedException e){
            logger.error("Main JDA process interrupted !", e);
        }

        logger.info("I can start supporting people now :)");

    }


    public static Bot getInstance() {
        return instance;
    }
    public YamlFile getConfig(){
        return config.getConfig();
    }
    private void exit(){
        logger.error("Looks like you didn't setup your bot properly, please look back and edit config.yml file !");
        System.exit(1);
    }
    public Guild getGuild(){
        return jda.getGuilds().get(0);
    }

}
