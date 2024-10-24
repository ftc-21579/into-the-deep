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
        addRequirements(this.claw);
    }

    @Override
    public void execute() {
        // outtake the game element
        claw.outtakePowers();
    }

    @Override
    public void end(boolean interrupted) {
        // stop the claw when the command ends
        claw.stop();
    }
}
