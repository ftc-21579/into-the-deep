package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class RunExtensionPidCommand extends InstantCommand {

    public RunExtensionPidCommand(Extension e) {
        super(e::runPID);
    }
}
