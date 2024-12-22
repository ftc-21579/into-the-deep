package org.firstinspires.ftc.teamcode.common.commandbase.command.pivot;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;

public class ManualPivotCommand extends CommandBase {

    private final Pivot pivot;
    private final Direction direction;

    public ManualPivotCommand(Pivot pivot, Direction direction) {
        this.pivot = pivot;
        this.direction = direction;
        addRequirements(pivot);
    }

    @Override
    public void initialize() {
        pivot.setSetpointDEG(pivot.getSetpointDEG() + (Config.pivot_increment * (direction == Direction.UP ? 1 : -1)));
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
