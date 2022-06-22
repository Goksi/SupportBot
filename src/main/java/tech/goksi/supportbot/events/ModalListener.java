package tech.goksi.supportbot.events;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.utils.Constants;
import tech.goksi.supportbot.utils.JsonUtils;
import tech.goksi.supportbot.utils.Web;

import java.awt.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class ModalListener extends ListenerAdapter {
    private final Logger logger;
    public ModalListener(){
        this.logger = LoggerFactory.getLogger(getClass());
    }
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if(event.getModalId().equals("support")){
            EmbedBuilder eb = new EmbedBuilder();
            TextChannel staffChannel;
            if((staffChannel = Bot.getInstance().getJda().getTextChannelById(Constants.STAFF_CHANNEL)) == null ){
                eb.setColor(Color.red);
                eb.setDescription("Error, please check your console !");
                event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                logger.error("Error while trying to send ticket embed, please check StaffChannel inside of your config.yml");
            }
            String title = Objects.requireNonNull(event.getValue("title")).getAsString();
            String description = Objects.requireNonNull(event.getValue("desc")).getAsString();
            JsonObject embedJson;
            try {
                 embedJson = (JsonObject) JsonParser.parseReader(new FileReader("embed.json"));
            } catch (IOException e) {
                logger.error("Error while reading embed.json file !", e);
                return;
            }

            assert staffChannel != null;
            if(description.length() > 1024){
                try {
                    description = Web.uploadToHaste(description);
                } catch (IOException e) {
                    logger.error("Error while uploading to hastebin !", e);
                    eb.setColor(Color.red);
                    eb.setDescription("Error, please check your console !");
                    event.replyEmbeds(eb.build()).setEphemeral(true).queue();
                }
            }
            staffChannel.sendMessageEmbeds(JsonUtils.jsonToEmbed(embedJson, event.getUser(), title, description))
                    .setActionRow(Button.of(ButtonStyle.SUCCESS, String.format("openTicket:%d", event.getUser().getIdLong()), Bot.getInstance().getConfig().getString("Tickets.ButtonText")))
                    .queue();
            event.reply(Bot.getInstance().getConfig().getString("Tickets.SentIssue")).setEphemeral(true).queue();


        }
    }
}
