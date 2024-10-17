package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class RunExtensionPIDCommand extends CommandBase {
    private final Extension extension;

    public RunExtensionPIDCommand(Extension extension) {
        this.extension = extension;
        addRequirements(this.extension);
    }

    @Override
    public void execute() {
        // run the PID loop
    }
}
