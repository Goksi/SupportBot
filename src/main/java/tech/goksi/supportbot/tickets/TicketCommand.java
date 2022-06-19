package tech.goksi.supportbot.tickets;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class TicketCommand extends SlashCommand {
    public TicketCommand(){
        this.name = "ticket";
        this.help = "Opens gui for requesting staff support!";
    }
    @Override
    protected void execute(SlashCommandEvent event) {
        TextInput title = TextInput.create("title", "Issue", TextInputStyle.SHORT)
                .setPlaceholder("Please tell us your issue").setMinLength(3).setMaxLength(35).build();
        TextInput desc = TextInput.create("desc", "Issue description", TextInputStyle.PARAGRAPH).setMaxLength(1500).setPlaceholder("Please describe your issue here").build();
        Modal modal  = Modal.create("support", "Issue report")
                .addActionRows(ActionRow.of(title), ActionRow.of(desc)).build();
        event.replyModal(modal).queue();
    }
}
