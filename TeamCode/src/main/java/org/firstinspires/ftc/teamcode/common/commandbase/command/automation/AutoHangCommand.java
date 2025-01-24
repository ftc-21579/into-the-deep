package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.ascent.SetPTOCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.ascent.WinchArmsCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Ascent;

public class AutoHangCommand extends SequentialCommandGroup {

    public AutoHangCommand(Bot b) {
        addCommands(
                new InstantCommand(() -> b.getDrivetrain().toggleDriveMode(true)),
                new SetPTOCommand(b.getAscent(), Ascent.PTOState.ENGAGED),
                new WaitCommand(1000),
                new WinchArmsCommand(b.getDrivetrain()),
                new WaitCommand(3000),
                new SetPTOCommand(b.getAscent(), Ascent.PTOState.LOCKED)
        );
    }

}
