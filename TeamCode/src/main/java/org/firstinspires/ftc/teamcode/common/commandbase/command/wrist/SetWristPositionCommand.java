package org.firstinspires.ftc.teamcode.common.commandbase.command.wrist;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;

public class SetWristPositionCommand extends CommandBase {

    private final Wrist wrist;
    private final Vector2d position;

    public SetWristPositionCommand(Wrist wrist, Vector2d position) {
        this.wrist = wrist;
        this.position = position;
        addRequirements(this.wrist);
    }

    @Override
    public void initialize() {
        double mappedAngle = wrist.normalizeAngle(position.getY());
        wrist.setTwist(position.getX());
        wrist.setAngle(mappedAngle);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
