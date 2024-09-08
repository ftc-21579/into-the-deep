package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class SetExtensionElevatorPowerCommand extends InstantCommand {

    public SetExtensionElevatorPowerCommand(Extension e, double power) {
        super(() -> e.setElevationMotorPower(power));
    }
}
