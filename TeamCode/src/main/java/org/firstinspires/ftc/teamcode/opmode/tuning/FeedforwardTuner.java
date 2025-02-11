package org.firstinspires.ftc.teamcode.opmode.tuning;

import static java.lang.Math.signum;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.common.hardware.AbsoluteAnalogEncoder;
import org.firstinspires.ftc.teamcode.common.util.PidfCoefficients;
import org.firstinspires.ftc.teamcode.common.util.PidfController;
import org.firstinspires.ftc.teamcode.common.util.MotionState;

@Config
@TeleOp(name = "Feedforward Tuner", group = "Tuning")
public class FeedforwardTuner extends OpMode {

    private DcMotorEx pivotMotor;
    private DcMotorEx topExtensionMotor;
    private DcMotorEx bottomExtensionMotor;
    private AbsoluteAnalogEncoder pivotEncoder;
    private PidfController pivotPidf;
    private PidfController extensionPidf;

    private final double encoderOffset = 60.0;
    private final double ticksperCM = 10.37339803;

    private double pivotSetpoint = 0;
    private double extensionSetpoint = 0;

    // New FF constants for tuning
    public static double pivotKgs = 0.2;
    public static double pivotKgd = 0.005;
    public static double pivotKs = 0.0;
    public static double pivotKv = 0.0;
    public static double pivotKa = 0.0;

    public static double extensionKgs = 0.1;
    public static double extensionKgd = 0.0;
    public static double extensionKs = 0.0;
    public static double extensionKv = 0.0;
    public static double extensionKa = 0.0;

    @Override
    public void init() {
        HardwareMap hwMap = hardwareMap;

        pivotMotor = hwMap.get(DcMotorEx.class, "pivot");
        topExtensionMotor = hwMap.get(DcMotorEx.class, "topExtension");
        bottomExtensionMotor = hwMap.get(DcMotorEx.class, "bottomExtension");

        topExtensionMotor.setDirection(DcMotorEx.Direction.REVERSE);

        topExtensionMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        bottomExtensionMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        pivotEncoder = new AbsoluteAnalogEncoder(hardwareMap.get(AnalogInput.class, "pivotEncoder"));

        // Initialize PIDF controllers with P, I, and D set to 0
        pivotPidf = new PidfController(new PidfCoefficients(0, 0, 0, this::calculatePivotKf));
        extensionPidf = new PidfController(new PidfCoefficients(0, 0, 0, this::calculateExtensionKf));
    }

    @Override
    public void loop() {
        // Set extension and pivot setpoints using gamepad buttons
        if (gamepad1.dpad_up) {
            extensionSetpoint = 35;
        } else if (gamepad1.dpad_down) {
            extensionSetpoint = 0;
        }

        if (gamepad1.dpad_right) {
            pivotSetpoint = 90;
        } else if (gamepad1.dpad_left) {
            pivotSetpoint = 0;
        }

        // Update PIDF controllers
        double pivotPosition = getPositionDEG();
        double extensionPosition = getPositionCM();
        pivotPidf.set(pivotSetpoint);
        extensionPidf.set(extensionSetpoint);
        pivotPidf.update(getRuntime(), pivotPosition, new MotionState(pivotPosition, 0), new MotionState(extensionPosition, 0));
        extensionPidf.update(getRuntime(), extensionPosition, new MotionState(pivotPosition, 0), new MotionState(extensionPosition, 0));

        // Set motor powers
        pivotMotor.setPower(pivotPidf.get());
        topExtensionMotor.setPower(extensionPidf.get());
        bottomExtensionMotor.setPower(extensionPidf.get());

        // Telemetry for monitoring
        telemetry.addData("Pivot Position", pivotPosition);
        telemetry.addData("Pivot Target", pivotSetpoint);
        telemetry.addData("Pivot Power", pivotPidf.get());
        telemetry.addData("Pivot kF", calculatePivotKf(new Object[]{new MotionState(pivotPosition, 0)}));
        telemetry.addData("Extension Position", extensionPosition);
        telemetry.addData("Extension Target", extensionSetpoint);
        telemetry.addData("Extension Power", extensionPidf.get());
        telemetry.addData("Extension kF", calculateExtensionKf(new Object[]{new MotionState(extensionPosition, 0)}));
        telemetry.update();
    }

    private double calculatePivotKf(Object[] a) {
        MotionState pivotState = (MotionState) a[0];
        return (pivotKgs + pivotKgd * getPositionCM()) * Math.cos(Math.toRadians(getPositionDEG())) + pivotKs +
                pivotKs * Math.signum(pivotState.v) + pivotKv * pivotState.v + pivotKa * pivotState.a;
    }

    private double calculateExtensionKf(Object[] a) {
        MotionState extensionState = (MotionState) a[0];
        return (extensionKgs + extensionKgd * getPositionCM()) * Math.sin(Math.toRadians(getPositionDEG())) + extensionKs +
                extensionKs * Math.signum(extensionState.v) + extensionKv * extensionState.v + extensionKa * extensionState.a;
    }

    public double getPositionDEG() {
        return (Math.toDegrees(pivotEncoder.getCurrentPosition()) - encoderOffset);
    }

    public double getPositionCM() {
        return topExtensionMotor.getCurrentPosition() / ticksperCM;
    }
}