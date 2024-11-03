package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class ManualExtensionInCommand extends CommandBase {
    private final Extension extension;

    public ManualExtensionInCommand(Extension extension) {
        this.extension = extension;
        addRequirements(this.extension);
    }

    @Override
    public void execute() {
        if (extension.getSetpointCM() > extension.getMinExtension()) {
            extension.setSetpointCM(extension.getSetpointCM() - 5);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
