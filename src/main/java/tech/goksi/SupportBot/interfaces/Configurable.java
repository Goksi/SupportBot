package tech.goksi.supportbot.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Configurable {

    @NotNull String getName();

    String getEmoji();

    @Nullable String[] getResponses();
}
