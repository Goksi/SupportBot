package tech.goksi.supportbot.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goksi.supportbot.utils.Constants;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getMessage().getContentRaw().isEmpty()) return;
        if(event.getMessage().getContentRaw().startsWith(Constants.COMMAND_PREFIX)){

        }
    }
}
