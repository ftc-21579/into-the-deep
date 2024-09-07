package org.firstinspires.ftc.teamcode.common.commandbase.subsystem.vision.apriltags;


import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;

public class AprilTagHandler {

    private final AprilTagProcessor aprilTagProcessor;

    public AprilTagHandler() {
        aprilTagProcessor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .build();

    }

    public AprilTagProcessor getAprilTagProcessor() {
        return aprilTagProcessor;
    }

    /**
     * Get the detections relative to the camera
     * Units are metric CM
     * @return ArrayList of RelativeAprilTag
     */
    public ArrayList<RelativeAprilTag> getDetectionsRelative() {
        ArrayList<RelativeAprilTag> detections = new ArrayList<>();
        for (AprilTagDetection detection : aprilTagProcessor.getDetections()) {
            RelativeAprilTag relative = new RelativeAprilTag(
                    detection.id,
                    detection.ftcPose.x,
                    detection.ftcPose.y,
                    detection.ftcPose.z,
                    detection.ftcPose.roll,
                    detection.ftcPose.pitch,
                    detection.ftcPose.yaw,
                    detection.ftcPose.range,
                    detection.ftcPose.bearing,
                    detection.ftcPose.elevation
            );
            detections.add(relative);
        }

        return detections;
    }

}
