package org.firstinspires.ftc.teamcode.common.commandbase.command.ascent;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Ascent;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

public class LockArmsCommand extends CommandBase {
    private final Ascent ascent;
    private final MecanumDrivetrain drivetrain;

    public LockArmsCommand(Ascent ascent, MecanumDrivetrain drivetrain) {
        this.ascent = ascent;
        this.drivetrain = drivetrain;
        addRequirements(this.ascent);
        addRequirements(this.drivetrain);
    }

    @Override
    public void initialize() {
        ascent.lockArms();
        drivetrain.winchArms();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}