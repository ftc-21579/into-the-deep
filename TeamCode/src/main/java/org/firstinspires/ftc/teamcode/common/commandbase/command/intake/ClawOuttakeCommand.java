package org.firstinspires.ftc.teamcode.common.commandbase.command.intake;

import com.arcrobotics.ftclib.command.CommandBase;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;

public class ClawOuttakeCommand extends CommandBase {
    private final Intake claw;

    public ClawOuttakeCommand(Intake claw) {
        this.claw = claw;
        addRequirements(this.claw);
    }

    @Override
    public void initialize() {
        claw.outtake();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}

