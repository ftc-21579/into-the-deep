package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.specIntake;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.pedropathing.localization.Pose;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.DepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.SpecCycle;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;

import java.util.concurrent.atomic.AtomicInteger;

public class SpecCycleCommand extends CommandBase {
    private final Bot bot;

    public SpecCycleCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        bot.setPathFinished(false);
        bot.setEnableTeleOpDrive(false);
        bot.getFollower().breakFollowing();
        bot.getFollower().setPose(specIntake);
        bot.getFollower().setMaxPower(1.0);
        new SpecCycle(bot).schedule();
    }

    @Override
    public void end(boolean interrupted) {
        bot.currentSpecCycles.set(0);
        bot.getFollower().breakFollowing();
        bot.setPathFinished(true);
        bot.getFollower().startTeleopDrive();
        bot.setEnableTeleOpDrive(true);
        bot.setTargetMode(TargetMode.SPEC_INTAKE);
        new DepositCommand(bot).schedule();
    }

    @Override
    public boolean isFinished() {
        return bot.getPathFinished();
    }

}