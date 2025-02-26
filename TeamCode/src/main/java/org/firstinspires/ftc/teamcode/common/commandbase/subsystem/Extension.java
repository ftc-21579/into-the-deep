package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot.pivotProfile;
import static java.lang.Math.max;
import static java.lang.Math.signum;
import static java.lang.Math.sin;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.util.AsymProfile;
import org.firstinspires.ftc.teamcode.common.util.AsymProfile.AsymConstraints;
import org.firstinspires.ftc.teamcode.common.util.DelayProfile;
import org.firstinspires.ftc.teamcode.common.util.MotionProfile;
import org.firstinspires.ftc.teamcode.common.util.MotionState;
import org.firstinspires.ftc.teamcode.common.util.PidfCoefficients;
import org.firstinspires.ftc.teamcode.common.util.PidfController;

import java.util.function.ToDoubleFunction;

@com.acmerobotics.dashboard.config.Config
public class Extension extends SubsystemBase {

    private final Bot bot;

    private double t;

    private final DcMotorEx bottomExtensionMotor, topExtensionMotor;

    public static double setpointCM = 0.0, highChamberTarget = 21.0, lowBasketTarget = 20.0, highBasketTarget = 62.0, ticksperCM = 10.37339803;
    public static double minExtension = 0.0, depositMaxExtension = 62, intakeMaxExtension = 50;

    public static ToDoubleFunction<Object[]> extensionKf = a -> {
        MotionState pivotState = (MotionState) a[0];
        MotionState extensionState = (MotionState) a[1];
        return (Config.extension_Kgs + Config.extension_Kgd * extensionState.x) * sin(Math.toRadians(pivotState.x)) +
                Config.extension_Ks * signum(extensionState.v) + Config.extension_Kv * extensionState.v + Config.extension_Ka * extensionState.a;
    };
    public static PidfCoefficients extensionCoeffs = new PidfCoefficients(
            Config.extension_kP, Config.extension_kI, Config.extension_kD, extensionKf);
    public static final AsymConstraints extensionConstraints = new AsymConstraints(Config.extension_Vm, Config.extension_Ai, Config.extension_Af);
    private PidfController extensionPidf = new PidfController(extensionCoeffs);
    public static MotionProfile extensionProfile = new DelayProfile(0, new MotionState(0, 0), 0);
    private double extensionOffset = 0;
    private double zeroTime = 0;

    /**
     * Constructor for the Extension subsystem.
     * @param bot The robot instance
     */
    public Extension(Bot bot) {
        this.bot = bot;

        bottomExtensionMotor = bot.hMap.get(DcMotorEx.class, "bottomExtension");
        bottomExtensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        topExtensionMotor = bot.hMap.get(DcMotorEx.class, "topExtension");
        topExtensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        topExtensionMotor.setDirection(DcMotor.Direction.REVERSE);

        resetOffset();
    }

    /**
     * Returns the rest time for the extension mechanism.
     * @return The rest time
     */
    public double restTime() {
        return max(pivotProfile.tf(), extensionProfile.tf());
    }

    /**
     * Resets the extension offset.
     */
    public void resetOffset() {
        extensionOffset = topExtensionMotor.getCurrentPosition();
    }

    @Override
    public void periodic() {
        t = bot.getTime();
        MotionState pivotState = pivotProfile.state(t);
        MotionState extensionState = extensionProfile.state(t);

        // Update the feedforward function with the current motion state
        ToDoubleFunction<Object[]> newExtensionKf = a -> (Config.extension_Kgs + Config.extension_Kgd * extensionState.x) * sin(Math.toRadians(pivotState.x)) +
                Config.extension_Ks * signum(extensionState.v) + Config.extension_Kv * extensionState.v + Config.extension_Ka * extensionState.a;

        // Update the PIDF coefficients with the new feedforward function
        extensionPidf.setCoeffs(new PidfCoefficients(
                Config.extension_kP, Config.extension_kI, Config.extension_kD, newExtensionKf));
        extensionConstraints.setAsymConstraints(Config.extension_Vm, Config.extension_Ai, Config.extension_Af);

        // Check if the extension is at rest and update the zero time
        if (t > restTime() && extensionState.x == 0 && Double.isNaN(zeroTime)) {
            zeroTime = restTime();
        }

        // If the extension is set to 0, apply a small negative power to hold the position
        if (!Double.isNaN(zeroTime)) {
            double power = (t - zeroTime < 0.25) ? -0.5 : -0.1;
            topExtensionMotor.setPower(power);
            bottomExtensionMotor.setPower(power);
            extensionPidf.reset(t);
        } else {
            // Update the PIDF controller with the current setpoint and motion state
            extensionPidf.set(extensionState.x);
            extensionPidf.update(t, getPositionCM(), pivotState, extensionState);
            topExtensionMotor.setPower(extensionPidf.get());
            bottomExtensionMotor.setPower(extensionPidf.get());
        }

        /*
        // Add telemetry data for debugging and monitoring
        bot.telem.addData("Time", t);
        bot.telem.addData("Rest Time", restTime());
        bot.telem.addData("Zero Time", zeroTime);

        bot.telem.addData("Extension Position", getPositionCM());
        bot.telem.addData("Extension Target", getSetpointCM());
        bot.telem.addData("Extension Velocity", topExtensionMotor.getVelocity() / ticksperCM);
        bot.telem.addData("Extension Power", topExtensionMotor.getPower());
        bot.telem.addData("Extension Current", topExtensionMotor.getCurrent(CurrentUnit.AMPS));
        bot.telem.addData("Extension Target Position", extensionState.x);
        bot.telem.addData("Extension Target Velocity", extensionState.v);
        bot.telem.addData("Extension Target Acceleration", extensionState.a);
        bot.telem.addData("Extension Offset", extensionOffset);
        bot.telem.addData("Extension PID Coefficients", extensionPidf.getCoeffs());
        bot.telem.addData("Extension kF", newExtensionKf.applyAsDouble(new Object[]{pivotState, extensionState}));

         */
    }

    /**
     * Sets the setpoint for the extension in centimeters.
     * @param extensionExt The desired extension in centimeters
     */
    public void setSetpointCM(double extensionExt) {
        setpointCM = Math.max(minExtension, Math.min(depositMaxExtension, extensionExt));
        if (!Double.isNaN(zeroTime)) {
            zeroTime = Double.NaN;
            resetOffset();
        }
        extensionProfile = AsymProfile.extendAsym(extensionProfile, extensionConstraints,
                t, new MotionState(extensionExt, 0));
    }

    /**
     * Gets the setpoint for the extension in centimeters.
     * @return The current setpoint of the extension in centimeters
     */
    public double getSetpointCM() {
        return setpointCM;
    }

    /**
     * Gets the current position of the extension in centimeters.
     * @return The current position of the extension in centimeters
     */
    public double getPositionCM() {
        return (topExtensionMotor.getCurrentPosition() / ticksperCM) - (extensionOffset / ticksperCM);
    }

    /**
     * Gets the maximum extension of the extension in centimeters.
     * @return The maximum extension of the extension in centimeters
     */
    public double getMaxExtension() {
        switch (bot.getState()) {
            case INTAKE:
                return intakeMaxExtension;
            case DEPOSIT:
                return depositMaxExtension;
        }
        return intakeMaxExtension;
    }

    /**
     * Gets the minimum extension of the extension in centimeters.
     * @return The minimum extension of the extension in centimeters
     */
    public double getMinExtension() {
        return minExtension;
    }
}