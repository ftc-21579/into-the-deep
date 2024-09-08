package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;

public class Extension extends SubsystemBase {

    private final Bot bot;
    private final DcMotor leftElevationMotor, rightElevationMotor, extensionMotor;


    public Extension(Bot bot) {
        this.bot = bot;

        leftElevationMotor = bot.hMap.get(DcMotor.class, "leftElevationMotor");
        rightElevationMotor = bot.hMap.get(DcMotor.class, "rightElevationMotor");
        rightElevationMotor.setDirection(DcMotor.Direction.REVERSE);

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

    public int getElevationMotorPosition() {
        return leftElevationMotor.getCurrentPosition();
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