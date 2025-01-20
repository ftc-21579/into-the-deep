package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import static org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension.depositMaxExtension;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.Config;
import org.firstinspires.ftc.teamcode.common.hardware.AbsoluteAnalogEncoder;

@com.acmerobotics.dashboard.config.Config
public class Pivot extends SubsystemBase {

    private final Bot bot;

    private final DcMotor pivotMotor;
    private final AbsoluteAnalogEncoder pivotEncoder;

    private final PIDFController pivotController;
    public double setpointDEG = 0.0, minAngle = 0.0, maxAngle = 100;

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
                Config.pivot_min_kF
        );
    }

    @Override
    public void periodic() {
        double kFConstant = (Config.pivot_max_kF - Config.pivot_min_kF) / depositMaxExtension;
        double extensionFF = (kFConstant * (bot.getExtension().getPositionCM()));
        double pivotFF  = (Math.cos(pivotEncoder.getCurrentPosition() - Math.toRadians(60)));
        double calculatedKf = ((extensionFF + Config.pivot_min_kF) * pivotFF);
        pivotController.setF(calculatedKf);

        double power = pivotController.calculate(
                Math.toDegrees(pivotEncoder.getCurrentPosition()),
                setpointDEG + 60
        );
        pivotMotor.setPower(power);

        bot.telem.addData("Pivot Angle", Math.toDegrees(pivotEncoder.getCurrentPosition()) - 60);
        bot.telem.addData("Pivot Target", setpointDEG);
        bot.telem.update();
    }

    /**
     * Set the setpoint of the pivot in degrees
     * @param setpoint the setpoint in degrees
     */
    public void setSetpointDEG(double setpoint) {
        setpointDEG = Math.max(minAngle, Math.min(maxAngle, setpoint));
    }

    /**
     * Set the setpoint of the pivot in degrees, ignoring safety limits
     * USE WITH EXTREME CAUTION, AUTOMATIONS ONLY
     * @param setpoint
     */
    public void setSetpointIGNORE(double setpoint) {
        setpointDEG = setpoint;
    }

    /**
     * Get the setpoint of the pivot in degrees
     * @return the setpoint in degrees
     */
    public double getSetpointDEG() {
        return setpointDEG;
    }

    /**
     * Get the current position of the pivot in degrees
     * @return the position in degrees
     */
    public double getPosition() {
        return Math.toDegrees(pivotEncoder.getCurrentPosition());
    }
}
