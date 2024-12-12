package org.firstinspires.ftc.teamcode.common.commandbase.command.ascent;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Ascent;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

public class WinchArmsCommand extends CommandBase {
    private final MecanumDrivetrain drivetrain;

    public WinchArmsCommand(MecanumDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(this.drivetrain);
    }

    @Override
    public void initialize() {
        drivetrain.winchArms();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}