package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;

public class SetBotStateCommand extends CommandBase {

    private final Bot bot;
    private final BotState state;

    public SetBotStateCommand(Bot bot, BotState state) {
        this.bot = bot;
        this.state = state;
    }

    public SetBotStateCommand(Bot bot) {
        this(bot, bot.getState() == BotState.INTAKE ? BotState.DEPOSIT : BotState.INTAKE);
    }

    @Override
    public void initialize() {
        bot.setState(state);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
