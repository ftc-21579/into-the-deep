package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension.extensionProfile;

import static java.lang.Math.cos;
import static java.lang.Math.signum;
import static java.lang.Math.sin;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.util.AsymProfile;
import org.firstinspires.ftc.teamcode.common.util.AsymProfile.AsymConstraints;
import org.firstinspires.ftc.teamcode.common.hardware.AbsoluteAnalogEncoder;
import org.firstinspires.ftc.teamcode.common.util.DelayProfile;
import org.firstinspires.ftc.teamcode.common.util.MotionProfile;
import org.firstinspires.ftc.teamcode.common.util.MotionState;
import org.firstinspires.ftc.teamcode.common.util.PidfCoefficients;
import org.firstinspires.ftc.teamcode.common.util.PidfController;

import java.util.function.ToDoubleFunction;

@com.acmerobotics.dashboard.config.Config
public class Pivot extends SubsystemBase {

    private final Bot bot;

    private double t;

    private final DcMotorEx pivotMotor;
    private final AbsoluteAnalogEncoder pivotEncoder;

    public double setpointDEG = 0.0, minAngle = 0.0, maxAngle = 100;
    private final double encoderOffset = 60.0;

    public static ToDoubleFunction<Object[]> pivotkF = a -> {
        MotionState pivotState = (MotionState)a[0];
        MotionState extensionState = (MotionState)a[1];
        return (Config.pivot_Kgs + Config.pivot_Kgd * extensionState.x) * cos(Math.toRadians(pivotState.x))
                + Config.pivot_kV * pivotState.v + Config.pivot_kA * pivotState.a;
    };
    public static final PidfCoefficients pivotCoeffs = new PidfCoefficients(
            Config.pivot_kP, Config.pivot_kI, Config.pivot_kP, pivotkF);
    public static final AsymConstraints pivotConstraints = new AsymConstraints(Config.pivot_Vm, Config.pivot_Ai, Config.pivot_Af);
    private PidfController pivotPidf = new PidfController(pivotCoeffs);
    public static MotionProfile pivotProfile = new DelayProfile(0, new MotionState(0, 0), 0);

    /**
     * Constructor for the Pivot subsystem.
     * @param bot The robot instance
     */
    public Pivot(Bot bot) {
        this.bot = bot;

        pivotMotor = bot.hMap.get(DcMotorEx.class, "pivot");

        pivotEncoder = new AbsoluteAnalogEncoder(
                bot.hMap.get(AnalogInput.class, "pivotEncoder")
        );
    }

    @Override
    public void periodic() {
        t = bot.getTime();
        double pivotAngle = getPositionDEG();
        MotionState pivotState = pivotProfile.state(t);
        MotionState extensionState = extensionProfile.state(t);

        // Update the feedforward function with the current motion state
        ToDoubleFunction<Object[]> newPivotKf = a -> (Config.pivot_Kgs + Config.pivot_Kgd * extensionState.x) * cos(Math.toRadians(pivotState.x)) +
                Config.pivot_kV * pivotState.v + Config.pivot_kA * pivotState.a;

        // Update the PIDF coefficients with the new feedforward function
        pivotPidf.setCoeffs(new PidfCoefficients(
                Config.pivot_kP, Config.pivot_kI, Config.pivot_kD, newPivotKf));
        pivotConstraints.setAsymConstraints(Config.pivot_Vm, Config.pivot_Ai, Config.pivot_Af);

        // Update the PIDF controller with the current setpoint and motion state
        if (pivotState.x == 0) {
            pivotMotor.setPower(-0.1);
            pivotPidf.reset(t);
        } else {
            pivotPidf.set(pivotState.x);
            pivotPidf.update(t, pivotAngle, pivotState, extensionState);
            // Set the motor power based on the PIDF controller output
            pivotMotor.setPower(pivotPidf.get());
        }

        // Add telemetry data for debugging and monitoring
        bot.telem.addData("Pivot Position", getPositionDEG());
        bot.telem.addData("Pivot Target", getSetpointDEG());
        bot.telem.addData("Pivot Velocity", pivotMotor.getVelocity());
        bot.telem.addData("Pivot Power", pivotMotor.getPower());
        bot.telem.addData("Pivot Current", pivotMotor.getCurrent(CurrentUnit.AMPS));
        bot.telem.addData("Pivot Target Position", pivotState.x);
        bot.telem.addData("Pivot Target Velocity", pivotState.v);
        bot.telem.addData("Pivot Target Acceleration", pivotState.a);
        bot.telem.addData("Pivot PID Coefficients", pivotPidf.getCoeffs());
        bot.telem.addData("Pivot kF", newPivotKf.applyAsDouble(new Object[]{pivotState, extensionState}));

        bot.telem.update();
    }

    /**
     * Sets the setpoint for the pivot in degrees.
     * @param pivotAng The desired pivot angle in degrees
     * @param safetyIgnore Whether to ignore safety limits
     */
    public void setSetpointDEG(double pivotAng, boolean safetyIgnore) {
        if (!safetyIgnore) {
            pivotAng = Math.max(minAngle, Math.min(maxAngle, pivotAng));
            setpointDEG = pivotAng;
        } else {
            setpointDEG = pivotAng;
        }
        pivotProfile = AsymProfile.extendAsym(pivotProfile, pivotConstraints,
                bot.getTime(), new MotionState(pivotAng, 0));
    }

    /**
     * Get the setpoint of the pivot in degrees
     * @return the setpoint in degrees
     */
    public double getSetpointDEG() {
        return setpointDEG;
    }

    /**
     * Get the current position of the pivot in degrees
     * @return the position in degrees
     */
    public double getPositionDEG() {
        return (Math.toDegrees(pivotEncoder.getCurrentPosition()) - encoderOffset);
    }

    /**
     * Get the current position of the pivot in radians
     * @return the position in radians
     */
    public double getPositionRAD() {
        return (Math.toRadians(getPositionDEG()));
    }
}
