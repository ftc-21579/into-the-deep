package org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Manipulator;

public class IntakeCommand extends InstantCommand {

    public IntakeCommand(Manipulator m) {
        super(m::intake);
    }
}
