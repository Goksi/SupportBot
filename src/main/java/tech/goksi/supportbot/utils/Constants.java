package tech.goksi.supportbot.utils;

import tech.goksi.supportbot.Bot;

public class Constants {

    public static final String TOKEN = Bot.getInstance().getConfig().getString("BotInfo.Token");
    public static final String OWNER_ID = Bot.getInstance().getConfig().getString("BotInfo.OwnerID");
    public static final String GUILD_ID = Bot.getInstance().getConfig().getString("BotInfo.ServerID");
}
