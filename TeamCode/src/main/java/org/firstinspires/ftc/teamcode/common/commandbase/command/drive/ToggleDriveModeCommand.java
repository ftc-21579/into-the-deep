package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

public class ToggleDriveModeCommand extends InstantCommand {
    private final MecanumDrivetrain drivetrain;

    public ToggleDriveModeCommand(MecanumDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain); // Ensures the command uses the drivetrain subsystem
    }

    @Override
    public void initialize() {
        drivetrain.toggleDriveMode();
    }
}
