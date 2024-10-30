package org.firstinspires.ftc.teamcode.common.commandbase.command.wrist;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;

public class ManualWristTwistCommand extends CommandBase {
    private final Wrist wrist;
    private final Direction direction;

    public ManualWristTwistCommand(Wrist wrist, Direction direction) {
        this.wrist = wrist;
        this.direction = direction;
        addRequirements(this.wrist);
    }

    @Override
    public void execute() {
        switch (direction) {
            case LEFT:
                wrist.incrementTwist();
                break;
            case RIGHT:
                wrist.decrementTwist();
                break;
        }
    }
}
