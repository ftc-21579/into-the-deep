package org.firstinspires.ftc.teamcode.common.commandbase.command.extension;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;

public class ToggleDepositTarget extends CommandBase {

    private final Extension extension;

    public ToggleDepositTarget(Extension extension) {
        this.extension = extension;

        addRequirements(extension);
    }

    @Override
    public void initialize() {
        extension.toggleDepositTarget();
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}