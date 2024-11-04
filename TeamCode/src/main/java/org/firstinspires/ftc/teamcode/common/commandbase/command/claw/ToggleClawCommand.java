package org.firstinspires.ftc.teamcode.common.commandbase.command.claw;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;

public class ToggleClawCommand extends CommandBase {

    private final Claw claw;

    public ToggleClawCommand(Claw claw) {
        this.claw = claw;

        addRequirements(claw);
    }

    @Override
    public void initialize() {
        claw.toggle();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
