package tech.goksi.supportbot.tickets;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.jetbrains.annotations.NotNull;
import tech.goksi.supportbot.Bot;
import tech.goksi.supportbot.exceptions.ConfigException;
import tech.goksi.supportbot.utils.CommonUtil;
import tech.goksi.supportbot.utils.Constants;

import java.util.*;

public final class TicketUtils {

    private static final Map<Long, Long> tickets; //<ChannelID, OwnerID>

    static {
        tickets = new HashMap<>();
    }

    public static @NotNull TextChannel createTicket(long issuerId, ButtonInteractionEvent e) throws ConfigException{
        if(Constants.TICKET_CATEGORY_ID == null) throw new ConfigException("TicketCategory is not set inside of config.yml");
        if(Constants.SUPPORT_ROLE_ID == null) throw new ConfigException("Support role is not set inside of config.yml");
        User issuer = Bot.getInstance().getJda().retrieveUserById(issuerId).complete();
        TextChannel ticketChannel = Objects.requireNonNull(Bot.getInstance().getJda().getCategoryById(Constants.TICKET_CATEGORY_ID)).createTextChannel(Bot.getInstance().getConfig().getString("Settings.TicketName")
                .replaceAll("%name", issuer.getName()).replaceAll("%randomID", String.valueOf(CommonUtil.randomInt()))).complete();
        tickets.put(ticketChannel.getIdLong(), issuerId);
        ticketChannel.upsertPermissionOverride(Objects.requireNonNull(Objects.requireNonNull(e.getGuild()).getMemberById(issuerId))).setAllowed(
                Permission.MESSAGE_SEND,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EXT_EMOJI,
                Permission.VIEW_CHANNEL
        ).reason("Adding ticket creator to the channel").queue();
        ticketChannel.upsertPermissionOverride(Objects.requireNonNull(e.getGuild()).getSelfMember()).setAllowed(
                Permission.MESSAGE_SEND,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EXT_EMOJI,
                Permission.VIEW_CHANNEL,
                Permission.MESSAGE_MANAGE
        ).reason("Adding bot to the channel").queue();
        ticketChannel.upsertPermissionOverride(Objects.requireNonNull(e.getMember())).setAllowed(
                Permission.MESSAGE_SEND,
                Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_ATTACH_FILES,
                Permission.MESSAGE_ADD_REACTION,
                Permission.MESSAGE_EXT_EMOJI,
                Permission.VIEW_CHANNEL,
                Permission.MESSAGE_MANAGE
        ).reason("Adding staff role to the channel").queue();
        ticketChannel.upsertPermissionOverride(e.getGuild().getPublicRole()).setDenied(
                Permission.VIEW_CHANNEL
        ).reason("Removing public role from the channel").queue();
        return ticketChannel;
    }

    public static boolean isTicketChannel(@NotNull TextChannel textChannel){
        return tickets.containsKey(textChannel.getIdLong());
    }

    public static boolean canInteract(@NotNull Member member, @NotNull TextChannel textChannel){
        return PermissionUtil.checkPermission(textChannel, member, Permission.VIEW_CHANNEL);
    }

    public static boolean hasMoreThenAllowed(@NotNull Member member){
        return tickets.values().stream()
                .filter(id -> id == member.getIdLong())
                .count() > Bot.getInstance().getConfig().getInt("Tickets.MaxTickets");
    }

    public static void closeTicket(@NotNull TextChannel textChannel){
        tickets.remove(textChannel.getIdLong());
        textChannel.delete().queue();
    }

    public static void addToChannel(Member member, TextChannel ticketChannel ){
        if(!isTicketChannel(ticketChannel)) return;
        ticketChannel.upsertPermissionOverride(Objects.requireNonNull(member)).setAllowed(
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
