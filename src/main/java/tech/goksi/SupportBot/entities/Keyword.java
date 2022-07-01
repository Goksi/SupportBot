package tech.goksi.supportbot.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.interfaces.Configurable;

public class Keyword implements Configurable {
    private final String name;
    private final String emoji;
    private final String[] responses;

    public Keyword(String name){
        this.name = name;
        ConfigurationSection section = Bot.getInstance().getConfig().getConfigurationSection("Keywords." + name);
        this.responses = section.getStringList("Response").toArray(new String[0]);
        this.emoji = section.getString("Emoji");
    }
    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public String getEmoji() {
        return emoji;
    }

    @Override
    public @Nullable String[] getResponses() {
        return responses;
    }

    public String getRandomResponse(){
        return responses[(int) (Math.random() * responses.length)];
    }

    @Nullable
    public static String findKeyword(String message){
        return Bot.getInstance().getConfig().getConfigurationSection("Keywords").getKeys(false).stream()
                .filter(key -> message.toLowerCase().contains(key.toLowerCase()))
                .findFirst().orElse(null); //responds just to the first keyword
    }
}
