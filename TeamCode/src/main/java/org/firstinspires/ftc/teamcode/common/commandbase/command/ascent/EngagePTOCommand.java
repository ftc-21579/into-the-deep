package org.firstinspires.ftc.teamcode.common.commandbase.command.ascent;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Ascent;

public class EngagePTOCommand extends CommandBase {
    private final Ascent ascent;

    public EngagePTOCommand(Ascent ascent) {
        this.ascent = ascent;
        addRequirements(this.ascent);
    }

    @Override
    public void initialize() {
        ascent.engagePTO();
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
