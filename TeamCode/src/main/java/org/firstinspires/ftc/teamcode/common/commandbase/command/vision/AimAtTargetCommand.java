package org.firstinspires.ftc.teamcode.common.commandbase.command.vision;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.mineinjava.quail.util.MiniPID;
import com.mineinjava.quail.util.geometry.Vec2d;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.vision.Vision;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.vision.apriltags.RelativeAprilTag;
import org.firstinspires.ftc.teamcode.common.intothedeep.Processors;

import java.util.ArrayList;

@Config
public class AimAtTargetCommand extends CommandBase {

    private final Bot bot;
    private final Vision vision;
    private boolean isFinished = false;

    private double targetTagId;
    private RelativeAprilTag target;

    public static double kP = 0.0;
    public static double kI = 0.0;
    public static double kD = 0.0;
    private final MiniPID pidController = new MiniPID(kP, kI, kD);

    public AimAtTargetCommand(Bot bot) {
        this.bot = bot;
        vision = bot.getVision();
        addRequirements(vision);
    }

    @Override
    public void initialize() {
        vision.setVisionPipeline(Processors.APRIL_TAG_PROCESSOR);
        ArrayList<RelativeAprilTag> detections = vision.getAprilTagHandler().getDetectionsRelative();

        if (detections.isEmpty()) {
            // No targets found
            return;
        }

        // Find the correct tag
        target = detections.get(0); // TODO: Filter for the correct tag
        targetTagId = target.getTagId();

    }

    @Override
    public void execute() {
        // Verify that the target isn't within the deadzone
        updateTarget();
        if (target.getBearing() < 0.1) {
            isFinished = true;
            return;
        }

        // Aim at the target
        double error = target.getBearing();
        double output = pidController.getOutput(error);

        new TeleOpDriveCommand(bot.getDrivetrain(), new Vec2d(0.0, 0.0), output, 1.0).execute();
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    private void updateTarget() {
        ArrayList<RelativeAprilTag> detections = vision.getAprilTagHandler().getDetectionsRelative();

        if (detections.isEmpty()) {
            // No targets found
            isFinished = true;
            return;
        }

        // Find the correct tag
        for (RelativeAprilTag tag : detections) {
            if (tag.getTagId() == targetTagId) {
                target = tag;
                return;
            }
        }
    }

}
