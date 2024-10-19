package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.hardware.AbsoluteAnalogEncoder;

public class Pivot extends SubsystemBase {

    private final Bot bot;

    private final DcMotor pivotMotor;
    private final AbsoluteAnalogEncoder pivotEncoder;

    private final PIDFController pivotController;
    private static double setpointDEG = 0.0;

    public Pivot(Bot bot) {
        this.bot = bot;

        pivotMotor = bot.hMap.get(DcMotor.class, "pivot");
        pivotEncoder = new AbsoluteAnalogEncoder(
                bot.hMap.get(AnalogInput.class, "pivotEncoder")
        );

        pivotController = new PIDFController(
                Config.pivot_kP,
                Config.pivot_kI,
                Config.pivot_kD,
                Config.pivot_kF
        );
        pivotController.setTolerance(Config.pivot_tolerance);
    }

    @Override
    public void periodic() {

        double power = pivotController.calculate(
                Math.toDegrees(pivotEncoder.getCurrentPosition()),
                setpointDEG
        );
        pivotMotor.setPower(power);
    }


    //region Setpoint methods
    public void setSetpointDEG(double setpoint) {
        setpointDEG = setpoint;
    }

    public double getSetpointDEG() {
        return setpointDEG;
    }

    public void setSetpointRAD(double setpoint) {
        setpointDEG = Math.toDegrees(setpoint);
    }

    public double getSetpointRAD() {
        return Math.toRadians(setpointDEG);
    }
    //endregion

    //region manual methods
    public void setPower(double power) {
        pivotMotor.setPower(power);
    }

    public double getPower() {
        return pivotMotor.getPower();
    }

    public double getPosition() {
        return Math.toDegrees(pivotEncoder.getCurrentPosition());
    }
    //endregion


}
