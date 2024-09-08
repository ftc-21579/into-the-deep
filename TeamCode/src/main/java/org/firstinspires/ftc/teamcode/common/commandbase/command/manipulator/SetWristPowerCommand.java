package org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Manipulator;

public class SetWristPowerCommand extends InstantCommand {
    public SetWristPowerCommand(Manipulator m, double power) {
        super(() -> {m.setWristPower(power);} );
    }
}
