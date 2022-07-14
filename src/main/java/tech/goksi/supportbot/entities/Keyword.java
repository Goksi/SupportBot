package tech.goksi.supportbot.entities;


import java.util.List;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import org.simpleyaml.configuration.ConfigurationSection;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.interfaces.Configurable;

public class Keyword implements Configurable {
    private final String name;
    private final String emoji;
    private final String[] responses;
    private final boolean hasEmoji;
    private final Boolean replay;
    private final List<String> channels;

    private Keyword(final String name) {
        this.name = name;
        final ConfigurationSection section = Bot.getInstance().getConfig().getConfigurationSection("Keywords." + name);
        this.responses = section.getStringList("Response").toArray(new String[0]);
        this.emoji = section.getString("Emoji");
        this.replay = section.getBoolean("Replay");
        this.hasEmoji = (this.emoji != null);
        this.channels = section.getStringList("Channels");
    }

    @NotNull
    public String getName() {
        return name;
    }

    public String getEmoji() {
        return this.emoji;
    }

    @Nullable
    public String[] getResponses() {
        return this.responses;
    }

    public boolean hasEmoji() {
        return this.hasEmoji;
    }

    public boolean shouldReplay() {
        return this.replay != null && this.replay;
    }

    public List<String> getChannels() {
        return channels;
    }

    @Nullable
    public static Keyword find(final String message) {
        for (final String key : Bot.getInstance().getConfig().getConfigurationSection("Keywords").getKeys(false)) {
            final ConfigurationSection section = Bot.getInstance().getConfig().getConfigurationSection("Keywords." + key);
            if (Pattern.compile(section.getString("Regex"), Pattern.CASE_INSENSITIVE).matcher(message).find()) {
                return new Keyword(key);
            }
        }
        return null;
    }

}
