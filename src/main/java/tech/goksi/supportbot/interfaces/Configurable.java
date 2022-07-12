package tech.goksi.supportbot.interfaces;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import tech.goksi.supportbot.utils.CommonUtil;

public interface Configurable
{
    @NotNull
    String getName();

    String getEmoji();

    @Nullable
    String[] getResponses();

    default String getRandomResponse(final String... replacements) {
        String[] responses = getResponses();
        return CommonUtil.formatString(responses[(int) (Math.random() * responses.length)], replacements);
    }
}