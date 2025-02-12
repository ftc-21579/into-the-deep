package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.BlinkinCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.ascent.SetPTOCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Ascent;

public class AutoHangCommand extends SequentialCommandGroup {

    public AutoHangCommand(Bot b) {
        addCommands(
                new ParallelCommandGroup(
                        //new InstantCommand(() -> b.getDrivetrain().toggleDriveMode(true)),
                        new SetPivotAngleCommand(b.getPivot(), 0),
                        new SetExtensionCommand(b.getExtension(), 0),
                        new SetWristPositionCommand(b.getWrist(), new Vector2d(0, 135)),
                        new SetPTOCommand(b.getAscent(), Ascent.PTOState.ENGAGED)
                ),
                new WaitCommand(1000),
                new BlinkinCommand(b.getBlinkin(), RevBlinkinLedDriver.BlinkinPattern.RAINBOW_WITH_GLITTER),
                //new WinchArmsCommand(b.getDrivetrain()),
                new WaitCommand(3000),
                new SetPTOCommand(b.getAscent(), Ascent.PTOState.LOCKED)
        );
    }

}
