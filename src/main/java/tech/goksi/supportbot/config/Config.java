package tech.goksi.supportbot.config;

import org.simpleyaml.configuration.file.YamlFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public final class Config {
    private YamlFile config = null;
    private File configFile;
    private final File ticketEmbedFile;
    private final File closeTicketEmbedFile;
    private final File addToTicketEmbedFile;
    private final Logger logger;
    public Config(){
        configFile = new File("config.yml");
        ticketEmbedFile = new File("TicketEmbed.json");
        closeTicketEmbedFile = new File("CloseTicketEmbed.json");
        addToTicketEmbedFile = new File("AddToTicketEmbed.json");
        logger = LoggerFactory.getLogger(getClass().getName());
    }
    public void reloadConfig() {
        configFile = new File("config.yml");
        config = new YamlFile(configFile);
        try{
            config.loadWithComments();
        }catch (IOException e){
            logger.error("Error while loading configuration file !", e);
        }
    }
    public void initConfig(){
        if(!configFile.exists()){
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.yml");
            assert is != null;
            writeFile(is, configFile);
        }
        config = new YamlFile(configFile);
        try{
            config.loadWithComments();
        } catch (IOException e){
            logger.error("Error while reading configuration file !", e);
        }
        if(!ticketEmbedFile.exists()){
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("TicketEmbed.json");
            assert is != null;
            writeFile(is, ticketEmbedFile);
        }
        if(!closeTicketEmbedFile.exists()){
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("CloseTicketEmbed.json");
            assert is != null;
            writeFile(is, closeTicketEmbedFile);
        }
        if(!addToTicketEmbedFile.exists()){
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("AddToTicketEmbed.json");
            assert is != null;
            writeFile(is, addToTicketEmbedFile);
        }

    }

    public YamlFile getConfig() {
        return config;
    }

    public void saveConfig(){
        if(config == null || configFile == null) return;
        try{
            config.save(configFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void writeFile(InputStream is, File file){
        try{
            try (OutputStream out = Files.newOutputStream(file.toPath())) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
            }
        }catch (IOException e){
            logger.error("Error while writing configuration file !", e);
        }

    }
}
