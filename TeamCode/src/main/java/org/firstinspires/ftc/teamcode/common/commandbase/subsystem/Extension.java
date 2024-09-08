package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.mineinjava.quail.util.MiniPID;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;

@Config
public class Extension extends SubsystemBase {

    private final Bot bot;
    private final DcMotor leftElevationMotor, rightElevationMotor, extensionMotor;

    public static double ex_kP = 0.01, ex_kI = 0.0, ex_kD = 0.0;
    public static double ev_kP = 0.01, ev_kI = 0.0, ev_kD = 0.0;

    private MiniPID exPID = new MiniPID(ex_kP, ex_kI, ex_kD),
            evPID = new MiniPID(ev_kP, ex_kI, ex_kD);

    public double exSetpoint = 0.0, evSetpoint = 0.0;


    public Extension(Bot bot) {
        this.bot = bot;

        leftElevationMotor = bot.hMap.get(DcMotor.class, "leftElevationMotor");
        rightElevationMotor = bot.hMap.get(DcMotor.class, "rightElevationMotor");
        rightElevationMotor.setDirection(DcMotor.Direction.REVERSE);

        leftElevationMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftElevationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightElevationMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightElevationMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        extensionMotor = bot.hMap.get(DcMotor.class, "extensionMotor");

    }

    //region Elevation Motor

    // 537.7 cpr on motor
    // 4.2:1 on sprocket
    // target ticks = (Angle in RAD * 128.3) / 2pi

    public void setElevationMotorPower(double power) {
        leftElevationMotor.setPower(power);
        rightElevationMotor.setPower(power);
    }

    public void setElevationSetpoint(double setpoint) {
        evSetpoint = setpoint;
    }

    public int getElevationMotorPosition() {
        return leftElevationMotor.getCurrentPosition();
    }

    //endregion


    //region Extension Motor

    public void setExtensionMotorPower(double power) {
        extensionMotor.setPower(power);
    }

    public void setExtensionSetpoint(double setpoint) {
        exSetpoint = setpoint;
    }

    public int getExtensionMotorPosition() {
        return extensionMotor.getCurrentPosition();
    }

    //endregion

    public void runPID() {
        double evTargetTicks = (int) (evSetpoint * 6.2732);
        bot.telem.addData("evTargetTicks", evTargetTicks);
        double evCurrentPosition = -getElevationMotorPosition();
        bot.telem.addData("evCurrentPos", evCurrentPosition);
        double evPower = evPID.getOutput(evCurrentPosition, evTargetTicks);
        double evPowerNorm = Math.max(-1, Math.min(1, evPower));
        bot.telem.addData("evPower", evPowerNorm);
        setElevationMotorPower(-evPowerNorm);

        double exTargetTicks = (int) (exSetpoint * 0.2495);
        double exCurrentPosition = getExtensionMotorPosition();
        double exPower = exPID.getOutput(exCurrentPosition, exTargetTicks);
        setExtensionMotorPower(exPower);
    }
}