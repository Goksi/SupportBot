package tech.goksi.supportbot.interfaces;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

public interface Configurable
{
    @NotNull
    String getName();

    String getEmoji();

    @Nullable
    String[] getResponses();
}