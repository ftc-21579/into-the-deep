package org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Manipulator;

public class WristUpCommand extends InstantCommand {

    public WristUpCommand(Manipulator m) {
        super(m::wristUp);
    }
}
