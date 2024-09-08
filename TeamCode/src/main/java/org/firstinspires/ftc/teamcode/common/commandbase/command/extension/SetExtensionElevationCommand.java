package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.mineinjava.quail.util.MiniPID;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

@Config
public class SetExtensionElevationCommand extends InstantCommand {

    public SetExtensionElevationCommand(Extension e, double setpoint) {
        super(() -> {
            e.setElevationSetpoint(setpoint);
        });
    }
}
