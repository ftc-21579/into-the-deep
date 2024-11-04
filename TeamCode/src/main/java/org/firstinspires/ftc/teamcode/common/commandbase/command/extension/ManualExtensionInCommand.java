package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class ManualExtensionInCommand extends CommandBase {
    private final Extension extension;
    private final double modifier;

    public ManualExtensionInCommand(Extension extension, double modifier) {
        this.extension = extension;
        this.modifier = modifier;
        addRequirements(this.extension);
    }

    @Override
    public void execute() {
        if (extension.getSetpointCM() > extension.getMinExtension()) {
            extension.setSetpointCM(extension.getSetpointCM() - (Config.ext_increment * modifier));
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
