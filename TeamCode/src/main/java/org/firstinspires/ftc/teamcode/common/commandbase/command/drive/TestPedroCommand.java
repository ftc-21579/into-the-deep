package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.specIntake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.localization.Pose;

import org.firstinspires.ftc.teamcode.common.Bot;

import java.util.concurrent.atomic.AtomicInteger;

public class TestPedroCommand extends CommandBase {
    private final Bot bot;

    public TestPedroCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        bot.setEnableTeleOpDrive(false);
        bot.getFollower().breakFollowing();
        bot.getFollower().setPose(specIntake);
        new SpecCycleCommand(bot).schedule();
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
