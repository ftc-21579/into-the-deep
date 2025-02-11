package org.firstinspires.ftc.teamcode.opmode.tuning;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension.extensionProfile;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot.pivotProfile;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.AnalogInput;
import org.firstinspires.ftc.teamcode.common.hardware.AbsoluteAnalogEncoder;
import org.firstinspires.ftc.teamcode.common.util.PidfCoefficients;
import org.firstinspires.ftc.teamcode.common.util.PidfController;
import org.firstinspires.ftc.teamcode.common.util.MotionState;

@Config
@TeleOp(name = "Trajectory Tuner", group = "Tuning")
public class TrajectoryTuner extends OpMode {
    private MultipleTelemetry telem;

    private DcMotorEx pivotMotor;
    private DcMotorEx topExtensionMotor;
    private DcMotorEx bottomExtensionMotor;
    private AbsoluteAnalogEncoder pivotEncoder;
    private PidfController pivotPidf;
    private PidfController extensionPidf;

    private final double encoderOffset = 60.0;
    private final double ticksperCM = 10.37339803;

    public static double pivotSetpoint = 0;
    public static double extensionSetpoint = 0;

    public static double velocity = 10;

    // Constants for tuning
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
        telem = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        pivotMotor = hardwareMap.get(DcMotorEx.class, "pivot");
        topExtensionMotor = hardwareMap.get(DcMotorEx.class, "topExtension");
        bottomExtensionMotor = hardwareMap.get(DcMotorEx.class, "bottomExtension");

        topExtensionMotor.setDirection(DcMotorEx.Direction.REVERSE);
        topExtensionMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        bottomExtensionMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        pivotEncoder = new AbsoluteAnalogEncoder(hardwareMap.get(AnalogInput.class, "pivotEncoder"));

        pivotPidf = new PidfController(new PidfCoefficients(0, 0, 0, this::calculatePivotKf));
        extensionPidf = new PidfController(new PidfCoefficients(0, 0, 0, this::calculateExtensionKf));
    }

    @Override
    public void loop() {
        double t = getRuntime();

        // Update PIDF controllers
        double pivotPosition = getPositionDEG();
        double extensionPosition = getPositionCM();
        pivotPidf.set(pivotSetpoint);
        extensionPidf.set(extensionSetpoint);
        MotionState pivotState = pivotProfile.state(t);
        MotionState extensionState = extensionProfile.state(t);
        pivotPidf.update(getRuntime(), pivotPosition, new MotionState(pivotSetpoint, 0), new MotionState(extensionPosition, 0));
        extensionPidf.update(getRuntime(), extensionPosition, new MotionState(pivotSetpoint, 0), new MotionState(extensionSetpoint, 0));

        // Set motor powers
        pivotMotor.setPower(pivotPidf.get());
        topExtensionMotor.setPower(extensionPidf.get());
        bottomExtensionMotor.setPower(extensionPidf.get());

        // Telemetry for monitoring
        telem.addData("Pivot Position", pivotPosition);
        telem.addData("Pivot Target", pivotSetpoint);
        telem.addData("Pivot Power", pivotPidf.get());
        telem.addData("Pivot Target Position", pivotState.x);
        telem.addData("Pivot Target Velocity", pivotState.v);
        telem.addData("Pivot Target Acceleration", pivotState.a);
        telem.addData("Pivot kF", calculatePivotKf(new Object[]{new MotionState(pivotPosition, 0)}));
        telem.addData("Extension Position", extensionPosition);
        telem.addData("Extension Target", extensionSetpoint);
        telem.addData("Extension Power", extensionPidf.get());
        telem.addData("Extension Target Position", extensionState.x);
        telem.addData("Extension Target Velocity", extensionState.v);
        telem.addData("Extension Target Acceleration", extensionState.a);
        telem.addData("Extension kF", calculateExtensionKf(new Object[]{new MotionState(extensionPosition, 0)}));
        telem.update();
    }

    private double calculatePivotKf(Object[] a) {
        MotionState pivotState = (MotionState) a[0];
        return (pivotKgs + pivotKgd * getPositionCM()) * Math.cos(Math.toRadians(getPositionDEG())) +
                pivotKs * Math.signum(pivotState.v) + pivotKv * pivotState.v + pivotKa * pivotState.a;
    }

    private double calculateExtensionKf(Object[] a) {
        MotionState extensionState = (MotionState) a[0];
        return (extensionKgs + extensionKgd * getPositionCM()) * Math.sin(Math.toRadians(getPositionDEG())) +
                extensionKs * Math.signum(extensionState.v) + extensionKv * extensionState.v + extensionKa * extensionState.a;
    }

    public double getPositionDEG() {
        return (Math.toDegrees(pivotEncoder.getCurrentPosition()) - encoderOffset);
    }

    public double getPositionCM() {
        return topExtensionMotor.getCurrentPosition() / ticksperCM;
    }
}
