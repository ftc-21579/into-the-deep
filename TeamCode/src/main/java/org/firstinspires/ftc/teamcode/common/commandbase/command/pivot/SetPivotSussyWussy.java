package org.firstinspires.ftc.teamcode.common.commandbase.command.pivot;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class SetPivotSussyWussy extends CommandBase {

    private final Pivot pivot;
    private final double angle;

    public SetPivotSussyWussy(Pivot pivot, double angleDEG) {
        this.pivot = pivot;
        this.angle = angleDEG;

        addRequirements(pivot);
    }

    @Override
    public void initialize() {
        pivot.setSetpointIGNORE(angle);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(pivot.getPosition() - angle) < Config.pivot_tolerance;
    }
}
