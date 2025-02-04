package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.Config;

@com.acmerobotics.dashboard.config.Config
public class Extension extends SubsystemBase {

    private final Bot bot;

    private final DcMotor bottomExtensionMotor, topExtensionMotor;

    private final PIDFController extensionController;
    public static double setpointCM = 0.0, highChamberTarget = 17.0, lowBasketTarget = 20.0, highBasketTarget = 60.0, ticksperCM = 10.37339803;
    public static double minExtension = 0.0, depositMaxExtension = 60, intakeMaxExtension = 50;

    public Extension(Bot bot) {
        this.bot = bot;

        bottomExtensionMotor = bot.hMap.get(DcMotor.class, "bottomExtension");
        if (bottomExtensionMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            bottomExtensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            bottomExtensionMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }

        topExtensionMotor = bot.hMap.get(DcMotor.class, "topExtension");
        if (topExtensionMotor.getMode() != DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            topExtensionMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            topExtensionMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        topExtensionMotor.setDirection(DcMotor.Direction.REVERSE);

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
        double calculatedkF = Config.extension_kF * (Math.cos(bot.getPivot().getPositionRAD() - Math.toRadians(90)));
        extensionController.setF(calculatedkF);

        double target = setpointCM;

        double power = extensionController.calculate(
                topExtensionMotor.getCurrentPosition(),
                target * ticksperCM
        );

        topExtensionMotor.setPower(power);
        bottomExtensionMotor.setPower(power);

        bot.telem.addData("Extension Position", getPositionCM());
        bot.telem.addData("Extension Target", getSetpointCM());
        bot.telem.addData("Calculated Extension kF", extensionController.getF());
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
