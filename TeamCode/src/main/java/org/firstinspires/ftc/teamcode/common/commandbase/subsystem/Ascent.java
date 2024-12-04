package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.mineinjava.quail.RobotMovement;
import com.mineinjava.quail.util.geometry.Pose2d;
import com.mineinjava.quail.util.geometry.Vec2d;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.common.Bot;

@com.acmerobotics.dashboard.config.Config
public class Ascent extends SubsystemBase {
    private final Bot bot;

    private final DcMotorEx backLeft, backRight, frontLeft, frontRight;

    private final PIDFController ascentController;

    public static double setpointCM = 0.0, ticksperCM = 100;

    private final Servo leftPTO, rightPTO;

    public static double hang = 1.0;

    public static double engaged = 0.0, release = 0.2, locked = 0.28;

    public Ascent(Bot bot) {
        this.bot = bot;

        backLeft = bot.hMap.get(DcMotorEx.class, "backLeft");
        backRight = bot.hMap.get(DcMotorEx.class, "backRight");
        frontLeft = bot.hMap.get(DcMotorEx.class, "frontLeft");
        frontRight = bot.hMap.get(DcMotorEx.class, "frontRight");

        //backRight.setDirection(DcMotorSimple.Direction.REVERSE);

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

    @Override
    public void periodic() {
        double target = setpointCM;

        double leftPower = ascentController.calculate(
                backRight.getCurrentPosition(),
                target * ticksperCM
        );

        double rightPower = ascentController.calculate(
                backRight.getCurrentPosition(),
                target * ticksperCM
        );

        backLeft.setPower(leftPower);
        backRight.setPower(leftPower);
        frontLeft.setPower(rightPower);
        frontRight.setPower(rightPower);
    }

    public void setSetpointCM(double setpoint) {
        setpointCM = setpoint;
    }

    public void engagePTO() {
        leftPTO.setPosition(engaged);
        rightPTO.setPosition(engaged);
    }

    public void releaseArms() {
        leftPTO.setPosition(release);
        rightPTO.setPosition(release);
    }

    public void lockArms() {
        leftPTO.setPosition(locked);
        rightPTO.setPosition(locked);
    }
}
