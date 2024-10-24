package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.Config;

public class Extension extends SubsystemBase {

    private final Bot bot;

    private final DcMotor extensionMotor;

    private final PIDFController extensionController;
    private double setpointCM = 0.0;

    public Extension(Bot bot) {
        this.bot = bot;

        extensionMotor = bot.hMap.get(DcMotor.class, "extension");

        extensionController = new PIDFController(
                Config.extension_kP,
                Config.extension_kI,
                Config.extension_kD,
                Config.extension_kF
        );
        extensionController.setTolerance(Config.extension_tolerance);
    }

    @Override
    public void periodic() {
        double power = extensionController.calculate(
                extensionMotor.getCurrentPosition(),
                setpointCM
        );
        extensionMotor.setPower(power);
    }

    public void setSetpointCM(double setpoint) {
        setpointCM = setpoint;
    }

    public double getSetpointCM() {
        return setpointCM;
    }

    public void setPower(double power) {
        extensionMotor.setPower(power);
    }

    public double getPower() {
        return extensionMotor.getPower();
    }

    public int getPositionCM() {
        return extensionMotor.getCurrentPosition();
    }

}
