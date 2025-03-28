package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.common.Bot;

@com.acmerobotics.dashboard.config.Config
public class Ascent extends SubsystemBase {
    private final Bot bot;

    private final DcMotorEx backLeft, backRight, frontLeft, frontRight;
    private final PIDFController ascentController;

    private final Servo leftPTO, rightPTO;

    private static final double TICKS_PER_CM = 384.5 / 11.2;
    private double setPointCM = 0.0;
    private static final double hangCM = -45.0;

    public static double engaged = 0.0, locked = 0.2;

    public enum PTOState {
        ENGAGED, LOCKED
    }

    public Ascent(Bot bot) {
        this.bot = bot;

        backLeft = bot.hMap.get(DcMotorEx.class, "backLeft");
        backRight = bot.hMap.get(DcMotorEx.class, "backRight");
        frontLeft = bot.hMap.get(DcMotorEx.class, "frontLeft");
        frontRight = bot.hMap.get(DcMotorEx.class, "frontRight");

        frontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        backLeft.setDirection(DcMotorEx.Direction.REVERSE);

        ascentController = new PIDFController(
                org.firstinspires.ftc.teamcode.common.Config.ascent_kP,
                org.firstinspires.ftc.teamcode.common.Config.ascent_kI,
                org.firstinspires.ftc.teamcode.common.Config.ascent_kD,
                org.firstinspires.ftc.teamcode.common.Config.ascent_kF
        );

        leftPTO = bot.hMap.get(Servo.class, "leftPTO");
        rightPTO = bot.hMap.get(Servo.class, "rightPTO");

        rightPTO.setDirection(Servo.Direction.REVERSE);
    }

    public void periodic() {
        if (!bot.getEnableDrive()) {
            double targetTicks = setPointCM * TICKS_PER_CM;

            double leftPower = ascentController.calculate(backLeft.getCurrentPosition(), targetTicks);
            double rightPower = ascentController.calculate(backRight.getCurrentPosition(), targetTicks);

            backLeft.setPower(leftPower);
            backRight.setPower(rightPower);
            frontLeft.setPower(leftPower);
            frontRight.setPower(rightPower);
        }
    }

    public void resetEncoders() {
        backLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        backLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontLeft.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void winchArms() {
        resetEncoders();
        setPointCM = hangCM;
    }

    public void engagePTO() {
        leftPTO.setPosition(engaged);
        rightPTO.setPosition(engaged);
    }

    public void lockArms() {
        leftPTO.setPosition(locked);
        rightPTO.setPosition(locked);
    }
}
