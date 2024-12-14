package org.firstinspires.ftc.teamcode.opmode.auto.command;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToIntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;

public class DelayedSetExtensionCommand extends SequentialCommandGroup {

    public DelayedSetExtensionCommand (Bot bot, double ext, long delay) {
        addCommands(
                new WaitCommand(delay),
                new SetExtensionCommand(bot.getExtension(), ext)
        );
    }
}
