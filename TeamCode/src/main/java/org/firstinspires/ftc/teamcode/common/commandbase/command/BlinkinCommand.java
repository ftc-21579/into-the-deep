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
        this.blinkin = blinkin;
        switch (color) {
            case RED:
                pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
                break;
            case BLUE:
                pattern = RevBlinkinLedDriver.BlinkinPattern.BLUE;
                break;
            case YELLOW:
                pattern = RevBlinkinLedDriver.BlinkinPattern.YELLOW;
                break;
            default:
                pattern = RevBlinkinLedDriver.BlinkinPattern.WHITE;
                break;
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
