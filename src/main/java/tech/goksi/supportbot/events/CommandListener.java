package tech.goksi.supportbot.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goksi.supportbot.entities.Command;
import tech.goksi.supportbot.utils.Constants;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(event.getMessage().getContentRaw().isEmpty()) return;
        if(event.getMessage().getContentRaw().startsWith(Constants.COMMAND_PREFIX)){
            String message = event.getMessage().getContentRaw();
            Command cmd = Command.find(message);

            if(cmd != null){
                if(cmd.getChannels() != null && !cmd.getChannels().isEmpty() && !cmd.getChannels().contains(event.getChannel().getId())) return;
                String response = cmd.getRandomResponse("%mention", event.getAuthor().getAsMention(),
                        "%tag", event.getAuthor().getAsTag());
                if(cmd.shouldDelete()) {
                    event.getMessage().delete().queue();
                }else if(cmd.hasEmoji()) {
                    event.getMessage().addReaction(cmd.getEmoji()).queue();
                }
                if(event.getMessage().getReferencedMessage() != null){
                    event.getMessage().getReferencedMessage().reply(response).mentionRepliedUser(false).queue();
                }else {
                    event.getChannel().sendMessage(response).queue();
                }

            }
        }
    }
}
