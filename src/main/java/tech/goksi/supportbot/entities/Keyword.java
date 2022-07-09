package tech.goksi.supportbot.entities;


import java.util.regex.Pattern;
import tech.goksi.supportbot.utils.CommonUtil;
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

    private Keyword(final String name) {
        this.name = name;
        final ConfigurationSection section = Bot.getInstance().getConfig().getConfigurationSection("Keywords." + name);
        this.responses = section.getStringList("Response").toArray(new String[0]);
        this.emoji = section.getString("Emoji");
        this.replay = section.getBoolean("Replay");
        this.hasEmoji = (this.emoji != null);
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

    public String getRandomResponse(final String... replacements) {
        return CommonUtil.formatString(this.responses[(int) (Math.random() * this.responses.length)], replacements);
    }

    public boolean hasEmoji() {
        return this.hasEmoji;
    }

    public boolean shouldReplay() {
        return this.replay != null && this.replay;
    }

    @Nullable
    public static Keyword findKeyword(final String message) {
        for (final String key : Bot.getInstance().getConfig().getConfigurationSection("Keywords").getKeys(false)) {
            final ConfigurationSection section = Bot.getInstance().getConfig().getConfigurationSection("Keywords." + key);
            if (Pattern.compile(section.getString("Regex"), Pattern.CASE_INSENSITIVE).matcher(message).find()) {
                return new Keyword(key);
            }
        }
        return null;
    }

}
