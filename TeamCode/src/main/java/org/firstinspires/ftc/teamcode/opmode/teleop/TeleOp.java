package org.firstinspires.ftc.teamcode.opmode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.arcrobotics.ftclib.command.CommandOpMode;
import com.arcrobotics.ftclib.command.CommandScheduler;
import com.arcrobotics.ftclib.command.ConditionalCommand;
import com.arcrobotics.ftclib.command.InstantCommand;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.pedropathing.commands.TeleopMovement;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.AutoHangCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.DepositCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.automation.IntakeCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.ManualExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.ManualPivotCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToggleElementCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.state.ToggleScoringTargetCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.ManualWristAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.ManualWristTwistCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Ascent;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.Direction;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "TeleOp")
public class TeleOp extends CommandOpMode {


    private Bot bot;
    private Intake claw;
    private Pivot pivot;
    private Extension extension;
    private Wrist wrist;
    private Ascent ascent;
    private MecanumDrivetrain drivetrain;

    private boolean enableDrive = true;

    private GamepadEx driverGamepad;

    private MultipleTelemetry telem;


    // Gamepad layout
    // https://www.padcrafter.com/?templates=Gamepad+1%2FDriver+Gamepad&plat=1&col=%23242424%2C%23606A6E%2C%23FFFFFF&rightStick=Yaw%2FRotation&leftStick=Translation&dpadUp=Wrist+Up&dpadRight=Wrist+Clockwise&dpadLeft=Wrist+Counter-Clockwise&dpadDown=Wrist+Down&aButton=Toggle+Claw&yButton=Sample%2FSpecimen+Auto+Deposit&xButton=Robot+State+Toggle&bButton=Sample%2FSpecimen+State+Toggle&rightTrigger=Extension+Out&leftTrigger=Extension+In&leftBumper=Pivot+Down&rightBumper=Pivot+Up&backButton=Red+%3D+Sample%2C+Blue+%3D+Specimen%2C+Green+%3D+Ascent&startButton=Options+%3D+Ascent    @Override
    public void initialize() {

        CommandScheduler.getInstance().reset();

        telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        driverGamepad = new GamepadEx(gamepad1);
        gamepad1.setLedColor(255, 255, 0, Gamepad.LED_DURATION_CONTINUOUS);

        bot = new Bot(telem, hardwareMap, gamepad1, enableDrive);

        //region Drivetrain
        drivetrain = bot.getDrivetrain();

        TeleopMovement driveCommand = new TeleopMovement(
                drivetrain.getFollower(),
                MecanumDrivetrain.robotCentric,
                driverGamepad
        ); // use .setJoystickDirections to invert the joystick directions as needed

        register(drivetrain);
        drivetrain.setDefaultCommand(driveCommand);

        //endregion

        //region Claw
        claw = bot.getClaw();

        Button clawToggle = (new GamepadButton(driverGamepad, GamepadKeys.Button.LEFT_STICK_BUTTON))
                .whenPressed(
                        new ConditionalCommand(
                                new DepositCommand(bot),
                                new IntakeCommand(bot),
                                () -> bot.getState() == BotState.DEPOSIT
                        )
                );

        register(claw);
        //endregion

        //region Pivot
        pivot = bot.getPivot();

        Button pivotDownButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_LEFT))
                .whenPressed(
                        new ManualPivotCommand(pivot, Direction.DOWN)
                );
        Button pivotUpButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_RIGHT))
                .whenPressed(
                        new ManualPivotCommand(pivot, Direction.UP)
                );

        register(pivot);
        //endregion

        //region Wrist
        wrist = bot.getWrist();

        Button wristUpButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_UP))
                .whenPressed(
                        new ManualWristAngleCommand(wrist, Direction.UP)
                );
        Button wristDownButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.DPAD_DOWN))
                .whenPressed(
                        new ManualWristAngleCommand(wrist, Direction.DOWN)
                );
        Button wristTwistLeftButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.LEFT_BUMPER))
                .whenPressed(
                        new ManualWristTwistCommand(wrist, Direction.LEFT)
                );
        Button wristTwistRightButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.RIGHT_BUMPER))
                .whenPressed(
                        new ManualWristTwistCommand(wrist, Direction.RIGHT)
                );

        register(wrist);
        //endregion

        //region Extension
        extension = bot.getExtension();

        ManualExtensionCommand extensionCommand = new ManualExtensionCommand(
                extension,
                () -> driverGamepad.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER),
                () -> driverGamepad.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER)
        );

        Button extensionUpButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.RIGHT_STICK_BUTTON))
                .whenPressed(
                        new ConditionalCommand(
                                new SetExtensionCommand(bot.getExtension(), 30),
                                new InstantCommand(() -> {}),
                                () -> bot.getState() == BotState.INTAKE
                        )
                );

        register(extension);
        extension.setDefaultCommand(extensionCommand);
        //endregion

        // region Ascent
        ascent = bot.getAscent();

        Button autoAscent = (new GamepadButton(driverGamepad, GamepadKeys.Button.START))
                .whenPressed(
                        new AutoHangCommand(bot)
                );

        register(ascent);
        // endregion

        //region Automation

        Button toggleTargetButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.Y))
                .whenPressed(
                       new ToggleScoringTargetCommand(bot)
                );

        Button toggleElementButton = (new GamepadButton(driverGamepad, GamepadKeys.Button.X))
                .whenPressed(
                    new ToggleElementCommand(bot)
                );

        //endregion


        schedule(
                new SequentialCommandGroup(
                        new SetExtensionCommand(bot.getExtension(), 0),
                        new SetPivotAngleCommand(bot.getPivot(), 15),
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, 230)),
                        new IntakeCommand(bot)
                )
        );
    }

}