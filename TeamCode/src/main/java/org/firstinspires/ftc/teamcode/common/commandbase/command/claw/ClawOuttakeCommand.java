package org.firstinspires.ftc.teamcode.common.commandbase.command.claw;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;

/**
 * Intended to be used like this:
 * <pre>
 * ClawOuttakeCommand command = new ClawOuttakeCommand(claw);
 * schedule(command.withTimeout(xxx));
 * </pre>
 */
public class ClawOuttakeCommand extends CommandBase {
    private final Claw claw;

    public ClawOuttakeCommand(Claw claw) {
        this.claw = claw;

        claw.outtake();

        addRequirements(this.claw);
    }

    @Override
    public boolean isFinished() {
        // need to track timings at some point
        return true;
    }
}
