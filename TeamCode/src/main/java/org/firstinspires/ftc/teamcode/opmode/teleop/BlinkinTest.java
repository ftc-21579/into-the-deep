package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Bot;
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
