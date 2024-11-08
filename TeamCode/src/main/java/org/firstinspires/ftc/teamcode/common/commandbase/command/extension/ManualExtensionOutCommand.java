package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class ManualExtensionOutCommand extends CommandBase {
    private final Extension extension;

    public ManualExtensionOutCommand(Extension extension) {
        this.extension = extension;
        addRequirements(this.extension);
    }

    @Override
    public void execute() {
        if (extension.getSetpointCM() < extension.getMaxExtension()) {
            extension.setSetpointCM(extension.getSetpointCM() + 5);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
