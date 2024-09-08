package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class SetExtensionPowerCommand extends InstantCommand {

    public SetExtensionPowerCommand(Extension e, double power) {
        super(() -> e.setExtensionMotorPower(power));
    }
}
