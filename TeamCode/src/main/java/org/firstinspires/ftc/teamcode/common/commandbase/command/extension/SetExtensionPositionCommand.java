package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.CommandBase;
import com.mineinjava.quail.util.MiniPID;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

@Config
public class SetExtensionPositionCommand extends CommandBase {

    private final Extension extension;
    private final int targetTicks;
    private int currentPosition;
    private boolean isFinished = false;

    public static double ticksPerCm = 0.2495;

    public static double kP = 0.1;
    public static double kI = 0.0;
    public static double kD = 0.0;
    private MiniPID pidController = new MiniPID(kP, kI, kD);

    public SetExtensionPositionCommand(Extension extension, double targetLengthCm) {
        this.extension = extension;

        targetTicks = (int) (targetLengthCm * ticksPerCm);

        currentPosition = extension.getExtensionMotorPosition();
        if (Math.abs(targetTicks - currentPosition) < 30) {
            isFinished = true;
        }
    }

    @Override
    public void execute() {

        currentPosition = extension.getExtensionMotorPosition();
        if (Math.abs(targetTicks - currentPosition) < 30) {
            isFinished = true;
            return;
        }

        double power = pidController.getOutput(currentPosition, targetTicks);
        extension.setExtensionMotorPower(power);

    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
