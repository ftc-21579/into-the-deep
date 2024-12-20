package org.firstinspires.ftc.teamcode.common.commandbase.command.claw;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;

public class ClawIntakeCommand extends CommandBase {
    private final Claw claw;

    public ClawIntakeCommand(Claw claw) {
        this.claw = claw;
        addRequirements(this.claw);
    }

    @Override
    public void initialize() {
        claw.intake();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}

