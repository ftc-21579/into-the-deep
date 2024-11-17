package org.firstinspires.ftc.teamcode.common.commandbase.command.pivot;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;

public class ManualPivotUpCommand extends CommandBase {
    private final Pivot pivot;
    private final Bot bot;

    public ManualPivotUpCommand(Bot bot, Pivot pivot) {
        this.pivot = pivot;
        this.bot = bot;
        addRequirements(this.pivot);
    }

    @Override
    public void execute() {
        if (bot.getTargetElement() == GameElement.SAMPLE) {
            pivot.setSetpointDEG(pivot.getSetpointDEG() + 15);
        } else {
            pivot.setSetpointDEG(pivot.getSetpointDEG() + 10);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
