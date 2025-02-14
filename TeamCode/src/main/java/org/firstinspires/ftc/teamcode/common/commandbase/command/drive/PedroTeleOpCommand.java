package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.specIntake;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.common.Bot;

public class PedroTeleOpCommand extends CommandBase {
    private final Bot bot;
    private final Follower follower;
    private final GamepadEx gamepad;
    double xOffset = 1;

    public PedroTeleOpCommand(Bot bot, Follower follower, GamepadEx gamepad) {
        this.bot = bot;
        this.follower = follower;
        this.gamepad = gamepad;
    }

    @Override
    public void initialize() {
        follower.startTeleopDrive();
    }

    @Override
    public void execute() {
        if (bot.getEnableTeleOpDrive()) {
            follower.setTeleOpMovementVectors(gamepad.getLeftY(), -gamepad.getLeftX(), -gamepad.getRightX(), bot.getRobotCentric());
        }
        follower.update();
        bot.telem.addData("isBusy", follower.isBusy());
        bot.telem.addData("Pose", follower.getPose());
        bot.telem.addData("Spec Intake X", specIntake.getX() - (xOffset * bot.currentSpecCycles.get()) + xOffset);
        bot.telem.addData("Current Spec Cycles", bot.currentSpecCycles.get());
        bot.telem.addData("Target Spec Cycles", bot.targetSpecCycles.get());
        bot.telem.addData("Target Game Element", bot.getTargetElement());
        bot.telem.addData("Target Scoring Mode", bot.getTargetMode());
    }

    @Override
    public boolean isFinished() {
        return !bot.getEnableDrive();
    }
}