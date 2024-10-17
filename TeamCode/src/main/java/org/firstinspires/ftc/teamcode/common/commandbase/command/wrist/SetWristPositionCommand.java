package org.firstinspires.ftc.teamcode.common.commandbase.command.wrist;

import com.arcrobotics.ftclib.command.CommandBase;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;

public class SetWristPositionCommand extends CommandBase {

    private final Wrist wrist;
    private final Vec2d position;

    public SetWristPositionCommand(Wrist wrist, Vec2d position) {
        this.wrist = wrist;
        this.position = position;
        addRequirements(this.wrist);
    }

    @Override
    public void execute() {
        // set the wrist position
    }
}
