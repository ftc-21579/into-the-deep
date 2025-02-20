package org.firstinspires.ftc.teamcode.common.commandbase.command.automation;

import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.specIntake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;

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
        new SequentialCommandGroup(
                new SpecCycleStart(bot),
                new SpecCycleLoop(bot).perpetually().interruptOn(bot::getPathFinished)
        ).schedule();
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
