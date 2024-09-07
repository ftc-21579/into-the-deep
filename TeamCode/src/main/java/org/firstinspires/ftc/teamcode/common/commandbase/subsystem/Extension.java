package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;

public class Extension extends SubsystemBase {

    private final Bot bot;
    private final DcMotor elevationMotor, extensionMotor;


    public Extension(Bot bot) {
        this.bot = bot;

        this.elevationMotor = bot.hMap.get(DcMotor.class, "elevationMotor");
        elevationMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        this.extensionMotor = bot.hMap.get(DcMotor.class, "extensionMotor");

    }

    //region Elevation Motor

    // 537.7 cpr on motor
    // 4.2:1 on sprocket
    // target ticks = (Angle in RAD * 128.3) / 2pi
    public void setElevation(double targetRad, double power) {
        double targetTicks = (targetRad * 128.3) / (2 * Math.PI);

        elevationMotor.setTargetPosition((int) targetTicks);
        elevationMotor.setPower(power);
    }

    public void setElevationMotorPower(double power) {
        elevationMotor.setPower(power);
    }

    public int getElevationMotorPosition() {
        return elevationMotor.getCurrentPosition();
    }

    //endregion


    //region Extension Motor

    public void setExtensionMotorPower(double power) {
        extensionMotor.setPower(power);
    }

    public int getExtensionMotorPosition() {
        return extensionMotor.getCurrentPosition();
    }

    //endregion
}