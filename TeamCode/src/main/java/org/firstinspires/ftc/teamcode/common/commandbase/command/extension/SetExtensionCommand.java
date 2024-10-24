package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class SetExtensionCommand extends CommandBase {

    private final Extension extension;
    private final double distance;

    public SetExtensionCommand(Extension extension, double distanceCM) {
        this.extension = extension;
        this.distance = distanceCM;

        extension.setSetpointCM(distance);

        addRequirements(this.extension);
    }

    @Override
    public void execute() {
        // nothing needed
    }

    @Override
    public boolean isFinished() {
        return Math.abs(extension.getPositionCM() - distance) < Config.extension_tolerance;
    }
}
