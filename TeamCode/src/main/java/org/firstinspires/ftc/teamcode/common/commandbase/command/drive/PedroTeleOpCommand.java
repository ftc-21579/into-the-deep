package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.common.Bot;

public class PedroTeleOpCommand extends CommandBase {
    private final Bot bot;
    private final Follower follower;
    private final GamepadEx gamepad;

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
        bot.telem.addData("Spec Cycles", bot.getSpecCycles());
    }

    @Override
    public boolean isFinished() {
        return !bot.getEnableDrive();
    }
}