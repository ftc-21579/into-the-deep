package org.firstinspires.ftc.teamcode.opmode.auto.command;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;

public class SpecimenSampleIntakeCommand extends SequentialCommandGroup {

    public SpecimenSampleIntakeCommand(Bot bot, double ext, Vec2d wrist) {
        addCommands(
                new ToIntakeCommand(bot),
                new SetExtensionCommand(bot.getExtension(), ext),
                new SetWristPositionCommand(bot.getWrist(), wrist)
        );
    }
}
