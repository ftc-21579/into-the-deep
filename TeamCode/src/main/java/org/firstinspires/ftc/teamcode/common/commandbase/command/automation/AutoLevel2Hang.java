package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.ascent.EngagePTOCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.ascent.LockArmsCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.ascent.LockLeftArmCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.ascent.LockRightArmCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.ascent.ReleaseArmsCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.ascent.WinchArmsCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.ToggleDriveModeCommand;

@Config
public class AutoLevel2Hang extends SequentialCommandGroup {
    public AutoLevel2Hang(Bot bot) {
        addCommands(
                new ToggleDriveModeCommand(bot.getDrivetrain(), true),
                new ReleaseArmsCommand(bot.getAscent()),
                new WaitCommand(1000),
                new EngagePTOCommand(bot.getAscent()),
                new WaitCommand(1000),
                new WinchArmsCommand(bot.getDrivetrain()),
                new WaitCommand(3000),
                new LockLeftArmCommand(bot.getAscent(), bot.getDrivetrain()),
                new WaitCommand(1000),
                new LockRightArmCommand(bot.getAscent(), bot.getDrivetrain())
                //new LockArmsCommand(bot.getAscent(), bot.getDrivetrain())
                //new WinchArmsCommand(bot.getDrivetrain())
                //new ToggleDriveModeCommand(bot.getDrivetrain())
        );
    }
}
