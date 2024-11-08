package org.firstinspires.ftc.teamcode.common.commandbase.command.pivot;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class ManualPivotDownCommand extends CommandBase {
    private final Pivot pivot;

    public ManualPivotDownCommand(Pivot pivot) {
        this.pivot = pivot;
        addRequirements(this.pivot);
    }

    @Override
    public void execute() {
        pivot.setSetpointDEG(pivot.getSetpointDEG() - 5);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
