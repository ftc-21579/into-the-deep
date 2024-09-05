package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

public class TeleOpDriveCommand extends InstantCommand {
    public TeleOpDriveCommand(MecanumDrivetrain d, Vec2d leftStick, double rot, double multiplier) {
        super(
                () -> d.teleopDrive(leftStick, rot, multiplier)
        );
    }
}
