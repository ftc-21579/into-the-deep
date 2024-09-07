package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.mineinjava.quail.util.MiniPID;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

@Config
public class SetExtensionElevationCommand extends CommandBase {

    private final Extension extension;
    private final int targetTicks;
    private int currentPosition;
    private boolean isFinished = false;

    private static double kP = 0.0;
    private static double kI = 0.0;
    private static double kD = 0.0;
    private MiniPID pidController = new MiniPID(kP, kI, kD);

    public SetExtensionElevationCommand(Extension extension, double elevationRad) {
        this.extension = extension;

        targetTicks = (int) ((elevationRad * 128.3) / (2 * Math.PI));

        currentPosition = extension.getElevationMotorPosition();
        if (Math.abs(targetTicks - currentPosition) < 30) {
            isFinished = true;
        }
    }

    @Override
    public void execute() {

        currentPosition = extension.getElevationMotorPosition();
        if (Math.abs(targetTicks - currentPosition) < 30) {
            isFinished = true;
            return;
        }

        double power = pidController.getOutput(currentPosition, targetTicks);
        extension.setElevationMotorPower(power);

    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
