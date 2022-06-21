package tech.goksi.supportbot.interfaces;

import net.dv8tion.jda.api.entities.Emoji;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Configurable {

    @NotNull String getName();

    @Nullable Emoji getEmoji();

    @Nullable String[] getResponses();
}
