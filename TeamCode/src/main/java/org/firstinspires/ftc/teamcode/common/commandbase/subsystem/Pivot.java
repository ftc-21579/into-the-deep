package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension.depositMaxExtension;
import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension.extensionProfile;

import static java.lang.Math.cos;
import static java.lang.Math.max;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotorEx;

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

    public static double pivotKp = Config.pivot_kP * 0;
    public static double pivotKi = Config.pivot_kI;
    public static double pivotKd = Config.pivot_kD;
    public static double pivotKgs = Config.pivot_Kgs;
    public static double pivotKgd = Config.pivot_Kgd;
    public static double pivotKv = Config.pivot_kV;
    public static double pivotKa = Config.pivot_kA;
    public static ToDoubleFunction<Object[]> pivotkF = a -> {
        MotionState pivotState = (MotionState)a[0];
        MotionState extensionState = (MotionState)a[1];
        return (pivotKgs + pivotKgd * extensionState.x) * cos(Math.toRadians(pivotState.x))
                + pivotKv * pivotState.v + pivotKa * pivotState.a;};
    public static final PidfCoefficients pivotCoeffs = new PidfCoefficients(
            pivotKp, pivotKi, pivotKd, pivotkF);
    public static double pivotVm = 350;
    public static double pivotAi = 500;
    public static double pivotAf = 500;
    public static final AsymConstraints pivotConstraints = new AsymConstraints(pivotVm, pivotAi, pivotAf);
    private PidfController pivotPidf = new PidfController(pivotCoeffs);
    public static MotionProfile pivotProfile = new DelayProfile(0, new MotionState(0, 0), 0);

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

        pivotPidf.set(pivotState.x);
        pivotPidf.update(t, pivotAngle, pivotState, extensionState);

        pivotMotor.setPower(pivotPidf.get());

        bot.telem.addData("Pivot Angle", getPositionDEG());
        bot.telem.addData("Pivot Target", setpointDEG);
        bot.telem.addData("Pivot Power", pivotPidf.get());
        bot.telem.addData("Pivot kF", pivotkF.applyAsDouble(new Object[]{pivotState, extensionState}));
        bot.telem.update();
    }

    public void setSetpoint(double pivotAng) {
        setpointDEG = Math.max(minAngle, Math.min(maxAngle, pivotAng));
        pivotProfile = AsymProfile.extendAsym(pivotProfile, pivotConstraints,
                bot.getTime(), new MotionState(pivotAng, 0));
    }

    /**
     * Set the setpoint of the pivot in degrees
     * @param setpoint the setpoint in degrees
     */
    public void setSetpointDEG(double setpoint) {
        setpointDEG = Math.max(minAngle, Math.min(maxAngle, setpoint));
    }

    /**
     * Set the setpoint of the pivot in degrees, ignoring safety limits
     * USE WITH EXTREME CAUTION, AUTOMATIONS ONLY
     * @param setpoint
     */
    public void setSetpointIGNORE(double setpoint) {
        setpointDEG = setpoint;
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
