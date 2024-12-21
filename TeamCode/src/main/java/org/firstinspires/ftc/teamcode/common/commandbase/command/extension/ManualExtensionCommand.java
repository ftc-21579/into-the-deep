package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;


/**
 * ManualExtensionCommand is a command that allows the driver to manually control the extension
 * Positive speed extends the extension, negative speed retracts the extension
 */
public class ManualExtensionCommand extends CommandBase {

    private final Extension extension;
    private final double modifier;

    /**
     * Constructor for ManualExtensionCommand
     * @param e Extension subsystem
     * @param speed Speed of extension (positive extends, negative retracts)
     */
    public ManualExtensionCommand(Extension e, double speed) {
        extension = e;
        modifier = speed;
        addRequirements(extension);
    }

    @Override
    public void initialize() {
        if (extension.getSetpointCM() > extension.getMinExtension()
                && extension.getSetpointCM() < extension.getMaxExtension()) {
            extension.setSetpointCM(extension.getSetpointCM() + (Config.ext_increment * modifier));
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
