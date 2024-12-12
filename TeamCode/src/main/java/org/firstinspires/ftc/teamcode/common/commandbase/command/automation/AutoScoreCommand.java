package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ToggleDepositTarget;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;

public class AutoScoreCommand extends CommandBase {

    private Bot bot;

    public AutoScoreCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        if (bot.getTargetElement() == GameElement.SAMPLE) {
            new ToggleDepositTarget(bot.getExtension()).schedule();
        } else {
            new AutoSpecimenCommand(bot).schedule();
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
