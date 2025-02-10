package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot.pivotProfile;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.signum;
import static java.lang.Math.sin;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.util.AsymProfile;
import org.firstinspires.ftc.teamcode.common.util.AsymProfile.AsymConstraints;
import org.firstinspires.ftc.teamcode.common.util.DelayProfile;
import org.firstinspires.ftc.teamcode.common.util.MotionProfile;
import org.firstinspires.ftc.teamcode.common.util.MotionState;
import org.firstinspires.ftc.teamcode.common.util.PidfCoefficients;
import org.firstinspires.ftc.teamcode.common.util.PidfController;

import java.util.function.Function;
import java.util.function.ToDoubleFunction;

@com.acmerobotics.dashboard.config.Config
public class Extension extends SubsystemBase {

    private final Bot bot;

    private double t;

    private final DcMotorEx bottomExtensionMotor, topExtensionMotor;

    public static double setpointCM = 0.0, highChamberTarget = 17.0, lowBasketTarget = 20.0, highBasketTarget = 60.0, ticksperCM = 10.37339803;
    public static double minExtension = 0.0, depositMaxExtension = 60, intakeMaxExtension = 50;

    public static double extensionKp = Config.extension_kP * 0;
    public static double extensionKi = Config.extension_kI;
    public static double extensionKd = Config.extension_kD;
    public static double extensionKgs = Config.extension_Kgs;
    public static double extensionKgd = Config.extension_Kgd;
    public static double extensionKs = Config.extension_Ks;
    public static double extensionKv = Config.extension_Kv;
    public static double extensionKa = Config.extension_Ka;
    public static double telemExtensionkF = 0.0;
    public static ToDoubleFunction<Object[]> extensionKf = a -> {
        MotionState pivotState = (MotionState) a[0];
        MotionState extensionState = (MotionState) a[1];
        double extensionKf = (extensionKgs + extensionKgd * extensionState.x) * sin(Math.toRadians(pivotState.x)) +
                extensionKs * signum(extensionState.v) + extensionKv * extensionState.v + extensionKa * extensionState.a;
        telemExtensionkF = extensionKf;
        return extensionKf;
    };
    public static PidfCoefficients extensionCoeffs = new PidfCoefficients(
            extensionKp, extensionKi, extensionKd, extensionKf);
    public static double extensionVm = 125;
    public static double extensionAi = 2500;
    public static double extensionAf = 500;
    public static final AsymConstraints extensionConstraints = new AsymConstraints(extensionVm, extensionAi, extensionAf);
    private PidfController extensionPidf = new PidfController(extensionCoeffs);
    public static MotionProfile extensionProfile = new DelayProfile(0, new MotionState(0, 0), 0);
    private double extensionOffset = 0;
    private double zeroTime = 0;

    public Extension(Bot bot) {
        this.bot = bot;

        bottomExtensionMotor = bot.hMap.get(DcMotorEx.class, "bottomExtension");
        bottomExtensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        topExtensionMotor = bot.hMap.get(DcMotorEx.class, "topExtension");
        topExtensionMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        topExtensionMotor.setDirection(DcMotor.Direction.REVERSE);

        resetOffset();
    }

    public double restTime() {
        return max(pivotProfile.tf(), extensionProfile.tf());
    }
    public void resetOffset() {
        extensionOffset = topExtensionMotor.getCurrentPosition();
    }

    @Override
    public void periodic() {
        t = bot.getTime();
        double extensionExt = getPositionCM() - (extensionOffset / ticksperCM);
        MotionState pivotState = pivotProfile.state(t);
        MotionState extensionState = extensionProfile.state(t);

        ToDoubleFunction<Object[]> newLiftKf = a -> {
            double number = (extensionKgs + extensionKgd * extensionState.x) * sin(Math.toRadians(pivotState.x)) +
                    extensionKs * signum(extensionState.v) + extensionKv * extensionState.v + extensionKa * extensionState.a;
            telemExtensionkF = number;
            bot.telem.addData("Calculated Extension kF", number);
            return number;
        };

        extensionPidf.setCoeffs(new PidfCoefficients(
                extensionKp, extensionKi, extensionKd, newLiftKf));

        bot.telem.addData("Pivot State", pivotState.x);
        bot.telem.addData("Lift State", extensionState.x);
        bot.telem.addData("Rest Time", restTime());
        bot.telem.addData("Zero Time", zeroTime);
        bot.telem.addData("PID Coefficients", extensionCoeffs.getPidfCoefficients());
        bot.telem.addData("Extension kF", extensionKf.applyAsDouble(new Object[]{pivotState, extensionState}));
        bot.telem.addData("Extension kF Telemetry", telemExtensionkF);

        if (t > restTime() && extensionState.x == 0 && Double.isNaN(zeroTime)) {
            zeroTime = restTime();
        }

        if (!Double.isNaN(zeroTime)) {
            double power = (t - zeroTime < 0.25) ? -0.5 : -0.1;
            topExtensionMotor.setPower(power);
            bottomExtensionMotor.setPower(power);
            extensionPidf.reset(t);
        } else {
            extensionPidf.set(extensionState.x);
            extensionPidf.update(t, extensionExt, pivotState, extensionState);
            topExtensionMotor.setPower(extensionPidf.get());
            bottomExtensionMotor.setPower(extensionPidf.get());
            bot.telem.addLine("Normal PIDF");
        }

        bot.telem.addData("Extension Position", getPositionCM());
        bot.telem.addData("Extension Target", getSetpointCM());
        bot.telem.addData("extensionState.x", extensionState.x);
        bot.telem.addData("extensionState.v", extensionState.v);
        bot.telem.addData("extensionState.a", extensionState.a);
        bot.telem.addData("extensionExt", extensionExt);
        bot.telem.addData("extensionOffset", extensionOffset);
        bot.telem.addData("Time", t);
        bot.telem.addData("Extension Power", topExtensionMotor.getPower());
    }

    public void setSetpoint(double extensionExt) {
        setpointCM = Math.max(minExtension, Math.min(depositMaxExtension, extensionExt));
        if (!Double.isNaN(zeroTime)) {
            zeroTime = Double.NaN;
            resetOffset();
            bot.telem.addData("RESET 2 OMG", zeroTime);
        }
        extensionProfile = AsymProfile.extendAsym(extensionProfile, extensionConstraints,
                t, new MotionState(extensionExt, 0));
    }

    /**
     * Set the setpoint for the extension in centimeters
     * @param setpoint the setpoint in centimeters
     */
    public void setSetpointCM(double setpoint) {
        setpointCM = setpoint;
    }

    /**
     * Get the setpoint for the extension in centimeters
     * @return the current setpoint of the extension in centimeters
     */
    public double getSetpointCM() {
        return setpointCM;
    }

    /**
     * Get the current position of the extension in centimeters
     * @return the current position of the extension in centimeters
     */
    public double getPositionCM() {
        return topExtensionMotor.getCurrentPosition() / ticksperCM;
    }

    /**
     * Get the maximum extension of the extension in centimeters
     * @return the maximum extension of the extension in centimeters
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
     * Get the minimum extension of the extension in centimeters
     * @return the minimum extension of the extension in centimeters
     */
    public double getMinExtension() {
        return minExtension;
    }

}
