package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.mineinjava.quail.util.geometry.Vec2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.AutoScoreCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.claw.ToggleClawCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.drive.TeleOpDriveCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ManualExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotDownCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotUpCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToggleElementCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToggleStateCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.ManualWristAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.ManualWristTwistCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;

import java.util.List;

@TeleOp(name="Test Tele", group="TeleOp")
@Disabled
public class TestTele extends LinearOpMode {


    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        Bot bot = new Bot(telemetry, hardwareMap, gamepad1, true);
        GamepadEx driver = new GamepadEx(gamepad1);

        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        waitForStart();

        new SetPivotAngleCommand(bot.getPivot(), 10).initialize();
        new SetWristPositionCommand(bot.getWrist(), new Vec2d(0, 135)).initialize();

        while (opModeIsActive()) {

            driver.readButtons();

            new TeleOpDriveCommand(
                    bot.getDrivetrain(),
                    () -> -driver.getRightX(),
                    () -> driver.getLeftY(),
                    () -> -driver.getLeftX(),
                    () -> 0.8
            ).schedule();

            if (driver.wasJustPressed(GamepadKeys.Button.B)) {
                new ToggleClawCommand(bot.getClaw()).schedule();
            }

            if (driver.wasJustPressed(GamepadKeys.Button.LEFT_BUMPER)) {
                new ManualPivotDownCommand(bot, bot.getPivot()).schedule();
            }
            if (driver.wasJustPressed(GamepadKeys.Button.RIGHT_BUMPER)) {
                new ManualPivotUpCommand(bot, bot.getPivot()).schedule();
            }

            if (driver.wasJustPressed(GamepadKeys.Button.DPAD_UP)) {
                new ManualWristAngleCommand(bot.getWrist(), Direction.UP);
            }
            if (driver.wasJustPressed(GamepadKeys.Button.DPAD_DOWN)) {
                new ManualWristAngleCommand(bot.getWrist(), Direction.DOWN);
            }
            if (driver.wasJustPressed(GamepadKeys.Button.DPAD_LEFT)) {
                new ManualWristTwistCommand(bot.getWrist(), Direction.LEFT);
            }
            if (driver.wasJustPressed(GamepadKeys.Button.DPAD_RIGHT)) {
                new ManualWristTwistCommand(bot.getWrist(), Direction.RIGHT);
            }

            new ManualExtensionCommand(
                    bot.getExtension(),
                    () -> driver.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER),
                    () -> driver.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
            );


            if (driver.wasJustPressed(GamepadKeys.Button.A)) {
                new ToggleStateCommand(bot);
            }
            if (driver.wasJustPressed(GamepadKeys.Button.Y)) {
                new AutoScoreCommand(bot).schedule();
            }
            if (driver.wasJustPressed(GamepadKeys.Button.X)) {
                new ToggleElementCommand(bot);
            }

            CommandScheduler.getInstance().run();

        }

    }
}
