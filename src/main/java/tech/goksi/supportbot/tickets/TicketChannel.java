package tech.goksi.supportbot.tickets;

import net.dv8tion.jda.internal.entities.GuildImpl;
import net.dv8tion.jda.internal.entities.TextChannelImpl;
import tech.goksi.supportbot.Bot;

public class TicketChannel extends TextChannelImpl {
    public TicketChannel(long id) {
        super(id, (GuildImpl) Bot.getInstance().getGuild());

    }
}
