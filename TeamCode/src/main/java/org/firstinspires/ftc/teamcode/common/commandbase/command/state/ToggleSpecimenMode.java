package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.SpecimenMode;

public class ToggleSpecimenMode extends CommandBase {


    private Bot bot;
    public ToggleSpecimenMode(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        if (bot.getSpecimenMode() == SpecimenMode.INTAKE) {
            bot.setSpecimenMode(SpecimenMode.DEPOSIT);
        } else {
            bot.setSpecimenMode(SpecimenMode.INTAKE);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}