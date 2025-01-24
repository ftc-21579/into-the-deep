package org.firstinspires.ftc.teamcode.common.commandbase.command;

import com.arcrobotics.ftclib.command.CommandBase;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

public class BlinkinCommand extends CommandBase {
    private final RevBlinkinLedDriver blinkin;
    private final RevBlinkinLedDriver.BlinkinPattern pattern;

    public BlinkinCommand(RevBlinkinLedDriver blinkin, RevBlinkinLedDriver.BlinkinPattern pattern) {
        this.blinkin = blinkin;
        this.pattern = pattern;
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
