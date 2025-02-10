package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

/**
 * SetExtensionCommand is a command that sets the extension to a specific setpoint
 * Setpoint is in centimeters and the extension is bound to extension limits
 */
public class SetExtensionCommand extends CommandBase {

    private final Extension extension;
    private final double setpoint;

    public SetExtensionCommand(Extension e, double setpointcm) {
        extension = e;
        this.setpoint = setpointcm;
        extension.setSetpoint(setpoint);
        addRequirements(extension);
    }

    @Override
    public void initialize() {
        extension.setSetpoint(setpoint);
    }

    @Override
    public boolean isFinished() {
        return (Math.abs(extension.getPositionCM()) - setpoint) <= Config.extension_tolerance;
    }
}
