package tech.goksi.supportbot.entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.simpleyaml.configuration.ConfigurationSection;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.interfaces.Configurable;
import tech.goksi.supportbot.utils.CommonUtil;

import java.util.regex.Pattern;

public class Keyword implements Configurable {
    private final String name;
    private final String emoji;
    private final String[] responses;
    private final boolean hasEmoji;
    private final Boolean replay;

    private Keyword(String name){
        this.name = name;
        ConfigurationSection section = Bot.getInstance().getConfig().getConfigurationSection("Keywords." + name);
        this.responses = section.getStringList("Response").toArray(new String[0]);
        this.emoji = section.getString("Emoji");
        this.replay = section.getBoolean("Replay");
        this.hasEmoji = this.emoji != null;

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

    public String getRandomResponse(String... replacements){
        return CommonUtil.formatString(responses[(int) (Math.random() * responses.length)], replacements);
    }

    public boolean hasEmoji() {
        return hasEmoji;
    }

    public boolean shouldReplay(){
        return replay != null && replay;
    }

    @Nullable
    public static Keyword findKeyword(String message){
        ConfigurationSection section;
        for(String key : Bot.getInstance().getConfig().getConfigurationSection("Keywords").getKeys(false)){
            section = Bot.getInstance().getConfig().getConfigurationSection("Keywords." + key);
            if(Pattern.compile(section.getString("Regex"), Pattern.CASE_INSENSITIVE).matcher(message).find()){
                return new Keyword(key);
            }
        }

        return null;
    }
}
