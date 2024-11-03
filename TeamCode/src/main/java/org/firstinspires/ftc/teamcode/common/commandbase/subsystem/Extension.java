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
        bot.telem.addData("ext act pwr", extensionMotor.getPower());
        bot.telem.addData("Ext Power", power);
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
