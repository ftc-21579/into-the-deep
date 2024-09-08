package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.mineinjava.quail.util.MiniPID;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

import java.util.Set;

@Config
public class SetExtensionPositionCommand extends InstantCommand {

    public SetExtensionPositionCommand(Extension e, double setpoint) {
        super(() -> {
            e.setExtensionSetpoint(setpoint);
        });
    }
}
