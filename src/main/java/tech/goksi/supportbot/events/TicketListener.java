package tech.goksi.supportbot.events;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.tickets.TicketUtils;
import tech.goksi.supportbot.utils.CommonUtil;
import tech.goksi.supportbot.utils.JsonUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;

public class TicketListener extends ListenerAdapter {
    private final Logger logger;
    public TicketListener(){
        logger = LoggerFactory.getLogger(TicketListener.class);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(TicketUtils.isTicketChannel(event.getTextChannel())){
            if(event.getMessage().getMentions().getMembers().size() > 0){
                Member member = event.getMessage().getMentions().getMembers().get(0);
                if(!TicketUtils.canInteract(member, event.getTextChannel())){
                    JsonObject embedJson;
                    try{
                        embedJson = (JsonObject) JsonParser.parseReader(new FileReader("AddToTicketEmbed.json"));
                    }catch (FileNotFoundException e){
                        logger.error("Can't find AddToTicketEmbed.json", e);
                        return;
                    }
                    String embedString = embedJson.toString();
                    embedString = CommonUtil.formatString(embedString, "%botAvatarUrl", Bot.getInstance().getJda().getSelfUser().getAvatarUrl(),
                            "%mentioned", member.getAsMention());
                    embedJson = (JsonObject) JsonParser.parseString(embedString);
                    event.getMessage().replyEmbeds(JsonUtils.jsonToEmbed(embedJson)).mentionRepliedUser(false)
                            .setActionRow(Button.of(ButtonStyle.SUCCESS, String.format("addToTicketYES:%d_%d", member.getIdLong(), event.getAuthor().getIdLong()), Bot.getInstance().getConfig().getString("Tickets.AddToTicketYES")),
                                    Button.of(ButtonStyle.DANGER, String.format("addToTicketNO:%d_%d", member.getIdLong(), event.getAuthor().getIdLong()), Bot.getInstance().getConfig().getString("Tickets.AddToTicketNO")))
                            .queue();
                }
            }
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if(Objects.equals(event.getButton().getId(), "closeTicket")){
            event.deferEdit().queue();
            TicketUtils.closeTicket(event.getTextChannel());
        }else if (Objects.requireNonNull(event.getButton().getId()).startsWith("addToTicket")){
            event.deferEdit().queue();
            String[] ids = event.getButton().getId().split(":")[1].split("_");
            Member toAdd = Objects.requireNonNull(event.getGuild()).retrieveMember(UserSnowflake.fromId(Long.parseLong(ids[0]))).complete();
            Member author = event.getGuild().getMember(UserSnowflake.fromId(Long.parseLong(ids[1])));
            assert author != null;
            if(author.equals(event.getMember())){
                if(event.getButton().getId().contains("YES")){
                    TicketUtils.addToChannel(toAdd, event.getTextChannel());
                }
                event.getHook().retrieveOriginal().queue(msg -> msg.delete().queue());
            }else {
                event.reply(Bot.getInstance().getConfig().getString("Tickets.CantAdd")).setEphemeral(true).queue();
            }
        }
    }
}
