package org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Manipulator;

public class GripperReleaseCommand extends InstantCommand {

    public GripperReleaseCommand(Manipulator m) {
        super(m::release);
    }
}