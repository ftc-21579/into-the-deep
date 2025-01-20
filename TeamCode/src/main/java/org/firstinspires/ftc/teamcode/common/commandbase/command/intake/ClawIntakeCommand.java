package org.firstinspires.ftc.teamcode.common.commandbase.command.intake;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;

public class ClawIntakeCommand extends CommandBase {
    private final Intake claw;

    public ClawIntakeCommand(Intake claw) {
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

