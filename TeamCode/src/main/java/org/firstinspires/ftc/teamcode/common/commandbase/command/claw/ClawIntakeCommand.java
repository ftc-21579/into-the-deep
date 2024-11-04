package org.firstinspires.ftc.teamcode.common.commandbase.command.claw;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;

public class ClawIntakeCommand extends CommandBase {
    private final Claw claw;

    public ClawIntakeCommand(Claw claw) {
        this.claw = claw;

        claw.intake();

        addRequirements(this.claw);
    }

    @Override
    public boolean isFinished() {
        // need to track timings at some point
        return true;
    }
}
