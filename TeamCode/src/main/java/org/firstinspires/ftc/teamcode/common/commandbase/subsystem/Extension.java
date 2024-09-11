package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDController;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;

@Config
public class Extension extends SubsystemBase {

    private final Bot bot;
    private final DcMotor leftElevationMotor, rightElevationMotor, extensionMotor;

    public static double ex_kP = 0.0, ex_kI = 0.0, ex_kD = 0.0;
    public static double ev_kP = 0.0005, ev_kI = 0.0, ev_kD = 0.000005, ev_kF = 0.000025;

    private PIDController exPID = new PIDController(ex_kP, ex_kI, ex_kD);
    private PIDFController evPIDF = new PIDFController(ev_kP, ev_kI, ev_kD, ev_kF);

    public double exSetpoint = 0.0, evSetpoint = 0.0;


    public Extension(Bot bot) {
        this.bot = bot;

        leftElevationMotor = bot.hMap.get(DcMotor.class, "leftElevationMotor");
        rightElevationMotor = bot.hMap.get(DcMotor.class, "rightElevationMotor");
        rightElevationMotor.setDirection(DcMotor.Direction.REVERSE);

        leftElevationMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftElevationMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightElevationMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightElevationMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

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

    // evSetpoint * 6.2732 = target ticks
    public void runPID() {
        int evTargetTicks = (int) (evSetpoint);
        //bot.telem.addData("evTargetTicks", evTargetTicks);
        int evCurrentPosition = getElevationMotorPosition();
        //bot.telem.addData("evCurrentPos", evCurrentPosition);
        double evPower = evPIDF.calculate(evCurrentPosition, evTargetTicks);
        //bot.telem.addData("evPower", evPower);
        setElevationMotorPower(evPower);

        //double exTargetTicks = (int) (exSetpoint * 0.2495);
        //double exCurrentPosition = getExtensionMotorPosition();
        //double exPower = exPID.calculate(exCurrentPosition, exTargetTicks);
        //setExtensionMotorPower(exPower);
    }
}