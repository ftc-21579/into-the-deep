package org.firstinspires.ftc.teamcode.common.commandbase.command.pivot;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class SetPivotAngleCommand extends CommandBase {

    private final Pivot pivot;
    private final double angleDeg;
    private final boolean ignoreSafety;

    public SetPivotAngleCommand(Pivot pivot, double angleDeg) {
        this(pivot, angleDeg, false);
    }

    public SetPivotAngleCommand(Pivot pivot, double angleDeg, boolean ignoreSafety) {
        this.pivot = pivot;
        this.angleDeg = angleDeg;
        this.ignoreSafety = ignoreSafety;
        addRequirements(pivot);
    }

    @Override
    public void initialize() {
        if (ignoreSafety) {
            pivot.setSetpointIGNORE(angleDeg);
            return;
        }
        pivot.setSetpointDEG(angleDeg);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
