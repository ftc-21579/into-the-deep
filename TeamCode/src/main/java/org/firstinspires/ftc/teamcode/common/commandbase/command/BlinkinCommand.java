package org.firstinspires.ftc.teamcode.common.commandbase.command;

import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.common.intothedeep.Color;

public class BlinkinCommand extends CommandBase {
    private RevBlinkinLedDriver blinkin;
    private final RevBlinkinLedDriver.BlinkinPattern pattern;

    public BlinkinCommand(RevBlinkinLedDriver blinkin, RevBlinkinLedDriver.BlinkinPattern pattern) {
        this.blinkin = blinkin;
        this.pattern = pattern;
    }

    public BlinkinCommand(RevBlinkinLedDriver blinkin, Color color) {
        if (color == Color.RED) {
            pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
        } else if (color == Color.BLUE) {
            pattern = RevBlinkinLedDriver.BlinkinPattern.BLUE;
        } else if (color == Color.YELLOW) {
            pattern = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
        } else {
            pattern = RevBlinkinLedDriver.BlinkinPattern.WHITE;
        }
    }

    @Override
    public void initialize() {
        blinkin.setPattern(pattern);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
