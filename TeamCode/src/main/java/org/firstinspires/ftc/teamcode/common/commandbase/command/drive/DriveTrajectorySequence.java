package org.firstinspires.ftc.teamcode.common.commandbase.command.drive;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.roadrunner.PinpointDrive;
import org.firstinspires.ftc.teamcode.common.util.PathSupplier;

public class DriveTrajectorySequence extends CommandBase {

    private final PinpointDrive p_drive;
    private final PathSupplier path;
    private boolean finished = false;
    private Action action;

    public DriveTrajectorySequence(PinpointDrive drive, PathSupplier trajectory){
        p_drive = drive;
        path = trajectory;
    }

    @Override
    public void initialize() {
        action = path.getPath(p_drive.actionBuilder(p_drive.pose));
    }

    @Override
    public void execute() {
        TelemetryPacket packet = new TelemetryPacket();
        action.preview(packet.fieldOverlay());
        finished = !action.run(packet);
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }

    @Override
    public boolean isFinished() {
        return finished;
    }
}