package org.firstinspires.ftc.teamcode.common.commandbase.command.manipulator;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Manipulator;

public class SetGripperPowerCommand extends InstantCommand {
    public SetGripperPowerCommand(Manipulator m, double power) {
        super(() -> {m.setGripperPower(power);} );
    }
}
