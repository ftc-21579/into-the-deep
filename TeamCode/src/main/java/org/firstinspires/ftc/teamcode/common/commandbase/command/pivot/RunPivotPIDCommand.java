package org.firstinspires.ftc.teamcode.common.commandbase.command.pivot;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

public class RunPivotPIDCommand extends CommandBase {
    private final Pivot pivot;

    public RunPivotPIDCommand(Pivot pivot) {
        this.pivot = pivot;
        addRequirements(this.pivot);
    }

    @Override
    public void execute() {
        // run the PID loop
    }
}
