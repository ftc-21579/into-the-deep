package org.firstinspires.ftc.teamcode.common.commandbase.command.claw;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;

public class StopClawCommand extends CommandBase {

    private final Claw claw;

    public StopClawCommand(Claw claw) {
        this.claw = claw;
        addRequirements(this.claw);
    }

    @Override
    public void execute() {
        claw.stop();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
