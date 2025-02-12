package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

import org.firstinspires.ftc.teamcode.common.Bot;

public class FollowerCommand extends CommandBase {
    private final Bot bot;
    private final Follower follower;
    private final GamepadEx gamepad;

    public FollowerCommand(Bot bot, Follower follower, GamepadEx gamepad) {
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
        follower.setTeleOpMovementVectors(gamepad.getLeftY(), -gamepad.getLeftX(), -gamepad.getRightX(), bot.getRobotCentric());
        follower.update();
    }
}