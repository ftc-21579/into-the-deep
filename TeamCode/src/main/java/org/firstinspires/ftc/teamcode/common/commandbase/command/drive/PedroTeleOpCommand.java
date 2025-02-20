package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import static org.firstinspires.ftc.teamcode.opmode.auto.SpecimenAuto.specIntake;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.pedropathing.follower.Follower;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.pedropathing.localization.PoseUpdater;
import com.pedropathing.util.DashboardPoseTracker;
import com.pedropathing.util.Drawing;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;

@Config
public class PedroTeleOpCommand extends CommandBase {
    private final Bot bot;
    private final Follower follower;
    private PoseUpdater poseUpdater;
    private DashboardPoseTracker dashboardPoseTracker;
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
        poseUpdater = new PoseUpdater(bot.hMap);
        dashboardPoseTracker = new DashboardPoseTracker(poseUpdater);

        Drawing.drawRobot(poseUpdater.getPose(), "#4CAF50");
        Drawing.sendPacket();
    }

    @Override
    public void execute() {
        poseUpdater.update();
        dashboardPoseTracker.update();

        bot.updateXOffset();
        bot.updateYOffset();

        double forward = gamepad.getLeftY();
        double lateral = gamepad.getLeftX();
        double heading = gamepad.getRightX();
        double multiplier = 0.5;
        if (bot.getEnableTeleOpDrive()) {
            if (bot.getState() == BotState.INTAKE && bot.getExtension().getPositionCM() > 20) {
                forward *= multiplier;
                lateral *= multiplier;
                heading *= multiplier;
            }
            follower.setTeleOpMovementVectors(forward, -lateral, -heading, bot.getRobotCentric());
        }
        follower.update();
        bot.telem.addData("isBusy", follower.isBusy());
        bot.telem.addData("Pose", follower.getPose());
        bot.telem.addData("Spec Intake X", specIntake.getX() - (xOffset * bot.currentSpecCycles.get()) + xOffset);
        bot.telem.addData("Current Spec Cycles", bot.currentSpecCycles.get());
        bot.telem.addData("Target Spec Cycles", bot.targetSpecCycles.get());
        bot.telem.addData("Target Game Element", bot.getTargetElement());
        bot.telem.addData("Target Scoring Mode", bot.getTargetMode());

        Drawing.drawPoseHistory(dashboardPoseTracker, "#4CAF50");
        Drawing.drawRobot(poseUpdater.getPose(), "#4CAF50");
        Drawing.sendPacket();
    }

    @Override
    public boolean isFinished() {
        return !bot.getEnableDrive();
    }
}