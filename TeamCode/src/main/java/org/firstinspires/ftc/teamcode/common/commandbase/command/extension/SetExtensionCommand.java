package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class SetExtensionCommand extends CommandBase {
    private final Extension extension;
    private final double distance;

    public SetExtensionCommand(Extension extension, double distance) {
        this.extension = extension;
        this.distance = distance;
        addRequirements(this.extension);
    }

    @Override
    public void execute() {
        // set the extension to the distance
    }

    @Override
    public boolean isFinished() {
        // return true if the extension is at the desired distance
        return true;
    }
}
