package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

public class ToggleDriveModeCommand extends InstantCommand {
    private final MecanumDrivetrain drivetrain;
    private boolean mode;

    public ToggleDriveModeCommand(MecanumDrivetrain drivetrain, boolean mode) {
        this.drivetrain = drivetrain;
        this.mode = mode;
        addRequirements(drivetrain); // Ensures the command uses the drivetrain subsystem
    }

    @Override
    public void initialize() {
        drivetrain.toggleDriveMode(mode);
    }
}
