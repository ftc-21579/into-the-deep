package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.specIntake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.localization.Pose;

import org.firstinspires.ftc.teamcode.common.Bot;

public class TestPedroCommand extends CommandBase {
    private final Bot bot;
    private final int loopCount;

    public TestPedroCommand(Bot bot, int loopCount) {
        this.bot = bot;
        this.loopCount = loopCount;
    }

    @Override
    public void initialize() {
        bot.setEnableTeleOpDrive(false);
        bot.getFollower().breakFollowing();
        bot.getFollower().setPose(specIntake);
        new SpecCycleCommand(bot, loopCount).schedule();
    }

    @Override
    public void end(boolean interrupted) {
        bot.getFollower().breakFollowing();
        bot.setPathFinished(false);
        bot.getFollower().startTeleopDrive();
        bot.setEnableTeleOpDrive(true);
    }

    @Override
    public boolean isFinished() {
        return bot.getPathFinished();
    }
}
