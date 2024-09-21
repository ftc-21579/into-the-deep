package org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Manipulator;

public class IdleCommand extends InstantCommand {

    public IdleCommand(Manipulator m) {
        super(m::idle);
    }
}