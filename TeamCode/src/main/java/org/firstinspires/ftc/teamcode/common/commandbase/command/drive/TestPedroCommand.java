package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Point;

import org.firstinspires.ftc.teamcode.common.Bot;

public class TestPedroCommand extends CommandBase {
    private final Bot bot;

    public TestPedroCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        bot.setEnableTeleOpDrive(false);
        bot.getFollower().breakFollowing();
        bot.getFollower().setPose(new Pose(0, 0, 0));
        new FollowPathCommand(bot.getFollower(), bot.getFollower().pathBuilder()
                .addPath(new BezierLine(new Point(0, 0), new Point(10, 0)))
                .build()
        ).schedule();
    }

    @Override
    public void end(boolean interrupted) {
        bot.getFollower().breakFollowing();
        bot.getFollower().startTeleopDrive();
        bot.setEnableTeleOpDrive(true);
    }

    @Override
    public boolean isFinished() {
        return !bot.getFollower().isBusy();
    }
}
