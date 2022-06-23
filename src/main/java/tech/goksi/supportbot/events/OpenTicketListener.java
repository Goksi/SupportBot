package tech.goksi.supportbot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.exceptions.ConfigException;
import tech.goksi.supportbot.tickets.TicketUtils;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class OpenTicketListener extends ListenerAdapter {
    private final Logger logger;
    public OpenTicketListener(){
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (Objects.requireNonNull(event.getButton().getId()).contains("openTicket")) {
            event.deferEdit().queue();
            event.getHook().editOriginal(event.getMessage()).setActionRow(event.getButton().asDisabled()).queue(); //mozda neko drugo dugme
            User issuer = Bot.getInstance().getJda().retrieveUserById(event.getButton().getId().split(":")[1]).complete();
            TextChannel ticketChannel;
            try {
                ticketChannel = TicketUtils.createTicket(issuer.getIdLong(), event);
            } catch (ConfigException e) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setColor(Color.red);
                eb.setDescription("Error, please check your console !");
                event.replyEmbeds(eb.build()).queue();
                logger.error("Error while creating ticket !", e);
                return;
            }
            ticketChannel.sendMessage(issuer.getAsMention() + " " + event.getUser().getAsMention()).queue(msg -> msg.delete().queueAfter(2, TimeUnit.SECONDS));
        }
    }
}
