package tech.goksi.supportbot.events;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import tech.goksi.supportbot.tickets.TicketUtils;

public class TicketListener extends ListenerAdapter {
    /*TODO: implementirati da ako neko taguje lika i on nije u tiket kanalu, baciti prompt da li da ga doda*/
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(TicketUtils.isTicketChannel(event.getTextChannel())){
            if(event.getMessage().getMentionedMembers().size() > 0){
                Member member = event.getMessage().getMentionedMembers().get(0);
                if(!TicketUtils.canInteract(member, event.getTextChannel())){
                    /*TODO:ovde poslati*/
                }
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

    }
}
