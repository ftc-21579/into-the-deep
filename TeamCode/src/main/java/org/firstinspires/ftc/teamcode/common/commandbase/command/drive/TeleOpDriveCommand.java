package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.geometry.Vector2d;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

import java.util.function.DoubleSupplier;

public class TeleOpDriveCommand extends CommandBase {

    private final MecanumDrivetrain drivetrain;
    private final DoubleSupplier leftStickX, leftStickY, rot, multiplier;

    public TeleOpDriveCommand(MecanumDrivetrain d, DoubleSupplier leftStickX,
                              DoubleSupplier leftStickY,
                              DoubleSupplier rot,
                              DoubleSupplier multiplier) {

        this.drivetrain = d;
        this.leftStickX = leftStickX;
        this.leftStickY = leftStickY;
        this.rot = rot;
        this.multiplier = multiplier;

        addRequirements(this.drivetrain);
    }

    @Override
    public void execute() {
        drivetrain.teleopDrive(
                new Vector2d(rot.getAsDouble(), leftStickY.getAsDouble()),
                leftStickX.getAsDouble(),
                multiplier.getAsDouble()
        );
    }
}
