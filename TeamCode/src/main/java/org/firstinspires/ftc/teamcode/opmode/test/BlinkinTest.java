package org.firstinspires.ftc.teamcode.opmode.test;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.commandbase.command.BlinkinCommand;

@TeleOp(name = "BlinkinTest", group = "TeleOp")
public class BlinkinTest extends CommandOpMode {
    private RevBlinkinLedDriver blinkin;

    @Override
    public void initialize() {
        CommandScheduler.getInstance().reset();
        blinkin = hardwareMap.get(RevBlinkinLedDriver.class, "blinkin");
        schedule(
                new BlinkinCommand(blinkin, RevBlinkinLedDriver.BlinkinPattern.RAINBOW_WITH_GLITTER)
        );
    }
}
