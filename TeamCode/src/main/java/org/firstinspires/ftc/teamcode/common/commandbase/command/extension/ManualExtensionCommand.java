package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

import java.util.function.DoubleSupplier;


/**
 * ManualExtensionCommand is a command that allows the driver to manually control the extension
 * Positive speed extends the extension, negative speed retracts the extension
 */
public class ManualExtensionCommand extends CommandBase {

    private final Extension extension;
    private final DoubleSupplier in, out;

    /**
     * Constructor for ManualExtensionCommand
     * @param e Extension subsystem
     * @param inModifier Supplier for extension retraction speed (in)
     * @param outModifier Supplier for extension extension speed (out)
     */
    public ManualExtensionCommand(Extension e, DoubleSupplier inModifier, DoubleSupplier outModifier) {
        extension = e;
        in = inModifier;
        out = outModifier;
        addRequirements(extension);
    }

    @Override
    public void initialize() {
        if (in.getAsDouble() > 0 && extension.getSetpointCM() > extension.getMinExtension()) {
            extension.setSetpointCM(extension.getSetpointCM() - (Config.ext_increment * in.getAsDouble()));
        } else if (out.getAsDouble() > 0 && extension.getSetpointCM() < extension.getMaxExtension()) {
            extension.setSetpointCM(extension.getSetpointCM() + (Config.ext_increment * out.getAsDouble()));
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
