package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.Config;

@com.acmerobotics.dashboard.config.Config
public class Extension extends SubsystemBase {

    private final Bot bot;

    private DcMotor extensionMotor;

    private final PIDFController extensionController;
    public static double setpointCM = 0.0, ticksperCM = 2.1;
    public static double minExtension = 0.0, depositMaxExtension = 300.0, intakeMaxExtension = 150.0;

    public Extension(Bot bot) {
        this.bot = bot;

        extensionMotor = bot.hMap.get(DcMotor.class, "extension");
        extensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extensionMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        extensionController = new PIDFController(
                Config.extension_kP,
                Config.extension_kI,
                Config.extension_kD,
                Config.extension_kF
        );
        //extensionController.setTolerance(Config.extension_tolerance);
    }

    @Override
    public void periodic() {
        double power = extensionController.calculate(
                extensionMotor.getCurrentPosition(),
                setpointCM * ticksperCM
        );

        extensionMotor.setPower(power);

        bot.telem.addData("Ext Encoder", extensionMotor.getCurrentPosition());
        bot.telem.addData("Ext Target", setpointCM * ticksperCM);
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

    public double getMaxExtension() {
        switch (bot.getState()) {
            case INTAKE:
                return intakeMaxExtension;
            case DEPOSIT:
                return depositMaxExtension;
        }
        return intakeMaxExtension;
    }

    public double getMinExtension() {
        return minExtension;
    }

}
