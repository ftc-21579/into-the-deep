package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

public class ToggleFieldCentricCommand extends InstantCommand {
    public ToggleFieldCentricCommand(MecanumDrivetrain d) {
        super(
                d::toggleFieldCentric
        );
        addRequirements(d);
    }
}
