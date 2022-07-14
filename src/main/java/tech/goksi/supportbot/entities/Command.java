package tech.goksi.supportbot.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.interfaces.Configurable;
import tech.goksi.supportbot.utils.Constants;

import java.util.List;
import java.util.regex.Pattern;

public class Command implements Configurable {
    private final String name;
    private final String[] responses;
    private final boolean shouldDelete;
    private final List<String> channels;
    private final boolean hasEmoji;
    private final String emoji;

    private Command(final String name){
        this.name = name;
        final ConfigurationSection section = Bot.getInstance().getConfig().getConfigurationSection("Commands." + name);
        this.responses = section.getStringList("Response").toArray(new String[0]);
        this.shouldDelete = section.getBoolean("Delete");
        this.channels = section.getStringList("Channels");
        this.hasEmoji = (section.getString("Emoji") != null);
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

    @Override
    public List<String> getChannels() {
        return channels;
    }

    @Override
    public boolean hasEmoji() {
        return hasEmoji;
    }

    public boolean shouldDelete(){
        return shouldDelete;
    }

    public static Command find(String message){
        for(String key : Bot.getInstance().getConfig().getConfigurationSection("Commands").getKeys(false)){
            message = message.replaceAll(Constants.COMMAND_PREFIX, "");
            if(Pattern.compile(message, Pattern.CASE_INSENSITIVE).matcher(key).find()) return new Command(key);
        }
        return null;
        }
    }


