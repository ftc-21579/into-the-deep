package org.firstinspires.ftc.teamcode.common.commandbase.subsystem.vision;

import android.util.Size;

import com.arcrobotics.ftclib.command.SubsystemBase;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.vision.apriltags.AprilTagHandler;
import org.firstinspires.ftc.teamcode.common.intothedeep.Processors;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionProcessor;

import java.util.ArrayList;

public class Vision extends SubsystemBase {

    private final Bot bot;
    private VisionPortal visionPortal;

    private AprilTagHandler aprilTagHandler;

    //region Vision Processors
    private final ArrayList<VisionProcessor> processors;


    //endregion

    public Vision(Bot bot) {
        this.bot = bot;

        processors = new ArrayList<>();

        // Initialize vision processors and add them to the list
        /*
            Processor Indices:
            0: AprilTagProcessor/Handler
         */
        aprilTagHandler = new AprilTagHandler();
        processors.add(aprilTagHandler.getAprilTagProcessor());

        // Initialize vision portal
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(bot.hMap.get(WebcamName.class, "Webcam 1")); // TODO verify name
        builder.setCameraResolution(new Size(640, 480));

        for (VisionProcessor processor : processors) {
            builder.addProcessor(processor);
        }

        visionPortal = builder.build();
    }

    public VisionPortal getVisionPortal() {
        return visionPortal;
    }

    public VisionPortal setVisionPipeline(Processors processor) {
        // Disable all processors
        for (VisionProcessor p : processors) {
            visionPortal.setProcessorEnabled(p, false);
        }

        // Enable the processor specified
        switch (processor) {
            case APRIL_TAG_PROCESSOR:
                visionPortal.setProcessorEnabled(processors.get(0), true);
                break;
            default:
                break;
        }
        return visionPortal;
    }

    public AprilTagHandler getAprilTagHandler() {
        return aprilTagHandler;
    }

}
