package tech.goksi.supportbot.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.exceptions.ConfigException;
import tech.goksi.supportbot.tickets.TicketUtils;
import tech.goksi.supportbot.utils.CommonUtil;
import tech.goksi.supportbot.utils.JsonUtils;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.interactions.components.buttons.Button;


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
            JsonObject embedJson;
            try{
                embedJson = (JsonObject) JsonParser.parseReader(new FileReader("CloseTicketEmbed.json"));
            }catch (FileNotFoundException e){
                logger.error("Error while reading CloseTicketEmbed.json file !", e);
                return;
            }
            String embedString = embedJson.toString();
            embedString = CommonUtil.formatString(embedString, "%botAvatarUrl", Bot.getInstance().getJda().getSelfUser().getAvatarUrl(),
                    "%issuer", issuer.getAsMention(), "%ticketOpener", event.getUser().getAsMention());
            embedJson = JsonParser.parseString(embedString).getAsJsonObject();
            ticketChannel.sendMessageEmbeds(JsonUtils.jsonToEmbed(embedJson)).setActionRow(Button.of(ButtonStyle.DANGER, "closeTicket",
                    Bot.getInstance().getConfig().getString("Tickets.CloseButtonText"))).queue();
            ticketChannel.sendMessage(issuer.getAsMention() + " " + event.getUser().getAsMention()).queue(msg -> msg.delete().queueAfter(2, TimeUnit.SECONDS));
        }
    }
}
