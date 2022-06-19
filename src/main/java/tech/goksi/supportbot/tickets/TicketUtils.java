package tech.goksi.supportbot.tickets;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.IPermissionHolder;
import net.dv8tion.jda.api.entities.TextChannel;

import net.dv8tion.jda.api.entities.User;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.exceptions.ConfigException;
import tech.goksi.supportbot.utils.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TicketUtils {

    private static final Map<Long, Long> tickets = new HashMap<>(); //<ChannelID, OwnerID>


    public static TextChannel createTicket(long issuerId) throws ConfigException{
        if(Constants.TICKET_CATEGORY_ID == null) throw new ConfigException("TicketCategory is not set inside of config.yml");
        if(Constants.SUPPORT_ROLE_ID == null) throw new ConfigException("Support role is not set inside of config.yml");
        User issuer = Bot.getInstance().getJda().retrieveUserById(issuerId).complete();
        TextChannel ticketChannel = Objects.requireNonNull(Bot.getInstance().getJda().getCategoryById(Constants.TICKET_CATEGORY_ID)).createTextChannel(Bot.getInstance().getConfig().getString("Settings.TicketName")
                .replaceAll("%name", issuer.getName())).complete();
        tickets.put(ticketChannel.getIdLong(), issuerId);
        ticketChannel.upsertPermissionOverride((IPermissionHolder) issuer).setAllowed(
                Permission.MESSAGE_SEND,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EXT_EMOJI,
                Permission.VIEW_CHANNEL
        ).reason("Adding ticket creator to the channel").queue();
        ticketChannel.upsertPermissionOverride((IPermissionHolder) Bot.getInstance().getJda().getSelfUser()).setAllowed(
                Permission.MESSAGE_SEND,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EXT_EMOJI,
                Permission.VIEW_CHANNEL,
                Permission.MESSAGE_MANAGE
        ).reason("Adding bot to the channel").queue();
        ticketChannel.upsertPermissionOverride(Objects.requireNonNull(Bot.getInstance().getJda().getRoleById(Constants.SUPPORT_ROLE_ID))).setAllowed(
                Permission.MESSAGE_SEND,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EXT_EMOJI,
                Permission.VIEW_CHANNEL,
                Permission.MESSAGE_MANAGE
        ).reason("Adding staff role to the channel").queue();
        return ticketChannel;
    }

    public static boolean isTicketChannel(TextChannel textChannel){
        return tickets.containsKey(textChannel.getIdLong());
    }

    public static void addToChannel(User user, TextChannel ticketChannel){
        if(!isTicketChannel(ticketChannel)) return;
        ticketChannel.upsertPermissionOverride((IPermissionHolder) user).setAllowed(
                Permission.MESSAGE_SEND,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EXT_EMOJI,
                Permission.VIEW_CHANNEL,
                Permission.MESSAGE_MANAGE
        ).reason("Adding user to ticket channel").queue();
    }
}
