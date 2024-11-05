package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

import java.util.function.DoubleSupplier;

public class ManualExtensionCommand extends CommandBase {

    private final Extension extension;
    private final DoubleSupplier inModifier;
    private final DoubleSupplier outModifier;

    public ManualExtensionCommand(Extension extension, DoubleSupplier inModifier, DoubleSupplier outModifier) {
        this.extension = extension;
        this.inModifier = inModifier;
        this.outModifier = outModifier;

        addRequirements(this.extension);
    }

    @Override
    public void execute() {
        if (inModifier.getAsDouble() > 0 && extension.getSetpointCM() > extension.getMinExtension()) {
            extension.setSetpointCM(extension.getSetpointCM() - (Config.ext_increment * inModifier.getAsDouble()));
        } else if (outModifier.getAsDouble() > 0 && extension.getSetpointCM() < extension.getMaxExtension()) {
            extension.setSetpointCM(extension.getSetpointCM() + (Config.ext_increment * outModifier.getAsDouble()));
        }
    }
}
