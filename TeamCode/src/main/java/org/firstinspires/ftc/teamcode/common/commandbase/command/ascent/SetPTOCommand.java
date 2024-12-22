package org.firstinspires.ftc.teamcode.common.commandbase.command.ascent;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Ascent;

public class SetPTOCommand extends CommandBase {

    private final Ascent ascent;
    private final Ascent.PTOState state;

    public SetPTOCommand(Ascent ascent, Ascent.PTOState state) {
        this.ascent = ascent;
        this.state = state;
        addRequirements(ascent);
    }

    @Override
    public void initialize() {
        switch (state) {
            case ENGAGED:
                ascent.engagePTO();
                break;
            case LOCKED:
                ascent.lockArms();
                break;
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
