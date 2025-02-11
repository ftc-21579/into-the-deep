package org.firstinspires.ftc.teamcode.opmode.tuning;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.hardware.AbsoluteAnalogEncoder;

@TeleOp(name="Velocity/Acceleration Tuner", group="Tuning")
public class VelocityAccelerationTuner extends LinearOpMode {
    private DcMotorEx bottomExtensionMotor, topExtensionMotor, pivotMotor;
    private AbsoluteAnalogEncoder pivotEncoder;
    private final double encoderOffset = 60.0;
    private final double ticksperCM = 10.37339803;

    private double lastPivotPosition = 0, lastExtensionPosition = 0;
    private double lastPivotTime = 0, lastExtensionTime = 0;
    private ElapsedTime runtime = new ElapsedTime();
    private double loopTime = 0;
    private static final double LOOP_TIME_STEP = 0.02;

    boolean pivotMotionComplete = false;
    boolean extensionMotionComplete = false;

    // Motion limits (adjust these for safety)
    private static final double PIVOT_MIN_ANGLE = 0;     // Min pivot angle (degrees)
    private static final double PIVOT_MAX_ANGLE = 90;   // Max pivot angle (degrees)
    private static final int EXTENSION_MIN_POSITION = 0;  // Fully retracted
    private static final int EXTENSION_MAX_POSITION = 30; // Fully extended

    private double maxPivotVelocity = 0, maxPivotAcceleration = 0;
    private double maxExtensionVelocity = 0, maxExtensionAcceleration = 0;

    private enum TuningStage {
        WAITING,
        MEASURE_PIVOT_VELOCITY,
        MEASURE_EXTENSION_VELOCITY,
        MEASURE_PIVOT_ACCELERATION,
        MEASURE_EXTENSION_ACCELERATION,
        COMPLETE
    }

    private TuningStage currentStage = TuningStage.WAITING;

    @Override
    public void runOpMode() {
        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();

        pivotMotor = hardwareMap.get(DcMotorEx.class, "pivot");
        bottomExtensionMotor = hardwareMap.get(DcMotorEx.class, "bottomExtension");
        topExtensionMotor = hardwareMap.get(DcMotorEx.class, "topExtension");
        topExtensionMotor.setDirection(DcMotor.Direction.REVERSE);
        pivotEncoder = new AbsoluteAnalogEncoder(hardwareMap.get(AnalogInput.class, "pivotEncoder"));

        // Set zero power behavior to BRAKE
        pivotMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bottomExtensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        topExtensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        ElapsedTime timer = new ElapsedTime();
        double lastPivotVelocity = 0, lastExtensionVelocity = 0, lastTime = 0;

        telemetry.addLine("Ready! Press Start to begin tuning.");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            double startTime = runtime.seconds();
            double elapsedTime = runtime.seconds() - startTime;
            if (elapsedTime < LOOP_TIME_STEP) {
                sleep((long) ((LOOP_TIME_STEP - elapsedTime) * 1000));
            }

            double currentTime = timer.seconds();
            loopTime = currentTime - lastTime;
            lastTime = currentTime;
            telemetry.addData("Loop Time", loopTime);

            previousGamepad1.copy(currentGamepad1);
            currentGamepad1.copy(gamepad1);
            switch (currentStage) {
                case WAITING:
                    extensionMotionComplete = false;
                    pivotMotionComplete = false;
                    telemetry.addLine("Press Dpad Up to measure Pivot Max Velocity.");
                    telemetry.addLine("Press Dpad Right to measure Extension Max Velocity.");
                    telemetry.addLine("Press Dpad Down to measure Pivot Acceleration.");
                    telemetry.addLine("Press Dpad Left to measure Extension Acceleration.");
                    telemetry.addData("Pivot Angle (deg)", getPositionDEG());
                    telemetry.addData("Extension Position (cm)", getPositionCM());
                    if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                        currentStage = TuningStage.MEASURE_PIVOT_VELOCITY;
                    } else if (currentGamepad1.dpad_right && !previousGamepad1.dpad_right) {
                        currentStage = TuningStage.MEASURE_EXTENSION_VELOCITY;
                    } else if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                        currentStage = TuningStage.MEASURE_PIVOT_ACCELERATION;
                    } else if (currentGamepad1.dpad_left && !previousGamepad1.dpad_left) {
                        currentStage = TuningStage.MEASURE_EXTENSION_ACCELERATION;
                    }
                    break;

                case MEASURE_PIVOT_VELOCITY:
                    telemetry.addLine("Measuring Pivot Max Velocity...");
                    measurePivotVelocity();
                    telemetry.addLine("Pivot Velocity Measurement Complete!");
                    telemetry.addData("Max Pivot Velocity (deg/sec)", maxPivotVelocity);
                    telemetry.addLine("Press Dpad Up to return to the main menu.");
                    if (currentGamepad1.dpad_up && !previousGamepad1.dpad_up) {
                        currentStage = TuningStage.WAITING;
                    }
                    break;

                case MEASURE_EXTENSION_VELOCITY:
                    telemetry.addLine("Measuring Extension Max Velocity...");
                    measureExtensionVelocity();
                    telemetry.addLine("Extension Velocity Measurement Complete!");
                    telemetry.addData("Max Extension Velocity (cm/sec)", maxExtensionVelocity);
                    telemetry.addLine("Press Dpad Right to return to the main menu.");
                    if (currentGamepad1.dpad_right && !previousGamepad1.dpad_right) {
                        currentStage = TuningStage.WAITING;
                    }
                    break;

                case MEASURE_PIVOT_ACCELERATION:
                    telemetry.addLine("Measuring Pivot Acceleration...");
                    measurePivotAcceleration();
                    telemetry.addLine("Pivot Acceleration Measurement Complete!");
                    telemetry.addData("Max Pivot Acceleration (deg/sec^2)", maxPivotAcceleration);
                    telemetry.addLine("Press Dpad Down to return to the main menu.");
                    if (currentGamepad1.dpad_down && !previousGamepad1.dpad_down) {
                        currentStage = TuningStage.WAITING;
                    }
                    break;

                case MEASURE_EXTENSION_ACCELERATION:
                    telemetry.addLine("Measuring Extension Acceleration...");
                    measureExtensionAcceleration();
                    telemetry.addLine("Extension Acceleration Measurement Complete!");
                    telemetry.addData("Max Extension Acceleration (cm/sec^2)", maxExtensionAcceleration);
                    telemetry.addLine("Press Dpad Left to return to the main menu.");
                    if (currentGamepad1.dpad_left && !previousGamepad1.dpad_left) {
                        currentStage = TuningStage.WAITING;
                    }
                    break;

                case COMPLETE:
                    telemetry.addLine("Tuning Complete!");
                    telemetry.addData("Max Pivot Velocity (deg/sec)", maxPivotVelocity);
                    telemetry.addData("Max Pivot Acceleration (deg/sec^2)", maxPivotAcceleration);
                    telemetry.addData("Max Extension Velocity (cm/sec)", maxExtensionVelocity);
                    telemetry.addData("Max Extension Acceleration (cm/sec^2)", maxExtensionAcceleration);
                    telemetry.update();
                    break;
            }
            telemetry.update();
        }
    }

    private void movePivotSafely(double power, double maxAngle) {
        pivotMotor.setPower(power);
        while (opModeIsActive() && getPositionDEG() < maxAngle) {
            telemetry.addData("Pivot Angle (deg)", getPositionDEG());
            telemetry.update();
        }
        pivotMotor.setPower(0);
        telemetry.addLine("Pivot Motion Complete!");
        telemetry.addData("Pivot Power", pivotMotor.getPower());
    }

    private void moveExtensionSafely(double power, int maxPosition) {
        bottomExtensionMotor.setPower(power);
        topExtensionMotor.setPower(power);
        while (opModeIsActive() && getPositionCM() < maxPosition) {
            telemetry.addData("Extension Position", getPositionCM());
            telemetry.update();
        }
        bottomExtensionMotor.setPower(0);
        topExtensionMotor.setPower(0);
        telemetry.addLine("Extension Motion Complete!");
        telemetry.addData("Extension Power", bottomExtensionMotor.getPower());
    }

    private void measurePivotVelocity() {
        if (!pivotMotionComplete) {
            movePivotSafely(1.0, PIVOT_MAX_ANGLE);
            pivotMotionComplete = true; // Set the flag
        }
        //getPivotVelocity();
        if (pivotMotor.getVelocity() / ticksperCM > maxPivotVelocity) {
            maxPivotVelocity = pivotMotor.getVelocity() / ticksperCM;
        }
    }

    private void measurePivotAcceleration() {
        double initialVelocity = getPivotVelocity();
        double initialTime = runtime.seconds();
        if (!pivotMotionComplete) {
            movePivotSafely(1.0, PIVOT_MAX_ANGLE);
            pivotMotionComplete = true; // Set the flag
        }
        double finalVelocity = getPivotVelocity();
        double finalTime = runtime.seconds();
        double acceleration = (finalVelocity - initialVelocity) / (finalTime - initialTime);
        maxPivotAcceleration = Math.max(maxPivotAcceleration, acceleration);
        telemetry.addData("Pivot Acceleration (deg/sec^2)", acceleration);
    }

    private void measureExtensionVelocity() {
        if (!extensionMotionComplete) {
            moveExtensionSafely(1.0, EXTENSION_MAX_POSITION);
            extensionMotionComplete = true; // Set the flag
        }
        getExtensionVelocity();
    }

    private void measureExtensionAcceleration() {
        double initialVelocity = getExtensionVelocity();
        double initialTime = runtime.seconds();
        if (!extensionMotionComplete) {
            moveExtensionSafely(1.0, EXTENSION_MAX_POSITION);
            extensionMotionComplete = true; // Set the flag
        }
        double finalVelocity = getExtensionVelocity();
        double finalTime = runtime.seconds();
        double acceleration = (finalVelocity - initialVelocity) / (finalTime - initialTime);
        maxExtensionAcceleration = Math.max(maxExtensionAcceleration, acceleration);
        telemetry.addData("Extension Acceleration (cm/sec^2)", acceleration);
    }

    private double getPivotVelocity() {
        double currentTime = runtime.seconds();
        double currentPosition = getPositionDEG();
        double timeDifference = currentTime - lastPivotTime;

        // Ensure the time difference is above a certain threshold
        if (timeDifference > loopTime) {
            double velocity = (currentPosition - lastPivotPosition) / timeDifference;

            lastPivotPosition = currentPosition;
            lastPivotTime = currentTime;

            // Update maxPivotVelocity if the current velocity is higher
            if (velocity > maxPivotVelocity) {
                maxPivotVelocity = velocity;
            }

            return velocity;
        } else {
            return 0; // Return 0 if the time difference is too small
        }
    }

    private double getExtensionVelocity() {
        double currentTime = runtime.seconds();
        double currentPosition = getPositionCM();
        double timeDifference = currentTime - lastExtensionTime;

        // Ensure the time difference is above the loop time
        if (timeDifference > loopTime) {
            double velocity = (currentPosition - lastExtensionPosition) / (currentTime - lastExtensionTime);

            lastExtensionPosition = currentPosition;
            lastExtensionTime = currentTime;

            // Update maxExtensionVelocity if the current velocity is higher
            if (velocity > maxExtensionVelocity) {
                maxExtensionVelocity = velocity;
            }

            return velocity;
        } else {
            return 0; // Return 0 if the time difference is too small
        }
    }

    public double getPositionDEG() {
        return (Math.toDegrees(pivotEncoder.getCurrentPosition()) - encoderOffset);
    }

    public double getPositionCM() {
        return topExtensionMotor.getCurrentPosition() / ticksperCM;
    }
}
