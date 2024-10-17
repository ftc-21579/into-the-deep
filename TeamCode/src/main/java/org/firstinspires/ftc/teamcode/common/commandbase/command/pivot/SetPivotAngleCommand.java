package org.firstinspires.ftc.teamcode.common.commandbase.command.pivot;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class SetPivotAngleCommand extends CommandBase {

    private final Pivot pivot;
    private final double angle;

    public SetPivotAngleCommand(Pivot pivot, double angle) {
        this.pivot = pivot;
        this.angle = angle;
    }

    @Override
    public void execute() {
        // set the pivot angle
    }

    @Override
    public boolean isFinished() {
        // return true if the pivot is at the desired angle
        return true;
    }
}
