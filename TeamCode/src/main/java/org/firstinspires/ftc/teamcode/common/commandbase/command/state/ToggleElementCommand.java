package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.arcrobotics.ftclib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;

public class ToggleElementCommand extends InstantCommand {

    public ToggleElementCommand(Bot bot) {
        super( () -> {
            switch (bot.getTargetElement()) {
                case SAMPLE:
                    bot.setTargetElement(GameElement.SPECIMEN);
                    break;
                case SPECIMEN:
                    bot.setTargetElement(GameElement.SAMPLE);
                    break;
            }
        });
    }
}
