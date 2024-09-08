package org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Manipulator;

public class WristDownCommand extends InstantCommand {

    public WristDownCommand(Manipulator m) {
        super(m::wristDown);
    }
}
