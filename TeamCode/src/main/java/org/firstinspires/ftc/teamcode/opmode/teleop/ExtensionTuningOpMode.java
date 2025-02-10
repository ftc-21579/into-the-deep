package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;

@TeleOp(name = "Extension Tuning OpMode", group = "Tuning")
public class ExtensionTuningOpMode extends OpMode {

    private Bot bot;
    private boolean enableDrive = true;
    private MultipleTelemetry telem;
    private GamepadEx driverGamepad;
    private Extension extension;
    private Pivot pivot;

    @Override
    public void init() {
        driverGamepad = new GamepadEx(gamepad1);
        telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        bot = new Bot(telem, hardwareMap, gamepad1, enableDrive);
        extension = bot.getExtension();
        pivot = bot.getPivot();
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            extension.setSetpoint(0);
        } else if (gamepad1.dpad_down) {
            extension.setSetpoint(35);
        } else if (gamepad1.dpad_left) {
            pivot.setSetpoint(90);
        } else if (gamepad1.dpad_right) {
            pivot.setSetpoint(0);
        }

        extension.periodic();
        pivot.periodic();

        telemetry.update();
    }
}