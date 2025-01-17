package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.Config;

@com.acmerobotics.dashboard.config.Config
public class Extension extends SubsystemBase {

    private final Bot bot;

    private final DcMotor extensionMotor;

    private final PIDFController extensionController;
    public static double setpointCM = 0.0, highChamberTarget = 17.0, lowBasketTarget = 20.0, highBasketTarget = 58.0, ticksperCM = 21.65;
    public static double minExtension = 0.0, depositMaxExtension = 61, intakeMaxExtension = 45;

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
        double target = bot.getPivot().getPosition() * -0.009 + setpointCM;

        double power = extensionController.calculate(
                extensionMotor.getCurrentPosition(),
                target * ticksperCM
        );

        extensionMotor.setPower(power);
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
    public int getPositionCM() {
        return extensionMotor.getCurrentPosition();
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
