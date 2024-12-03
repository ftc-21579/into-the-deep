package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
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

@Config
public class Ascent extends SubsystemBase {
    private final Bot bot;

    private final DcMotorEx backLeft, backRight;

    private final Servo leftPTO, rightPTO;

    private static final int hang = 10;

    private static final double engaged = 0.0, release = 0.1, locked = 0.2;

    public Ascent(Bot bot) {
        this.bot = bot;

        backLeft = bot.hMap.get(DcMotorEx.class, "backLeft");
        backRight = bot.hMap.get(DcMotorEx.class, "backRight");

        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftPTO = bot.hMap.get(Servo.class, "leftPTO");
        rightPTO = bot.hMap.get(Servo.class, "rightPTO");

        rightPTO.setDirection(Servo.Direction.REVERSE);
    }

    public void winchArms() {
        backLeft.setTargetPosition(hang);
        backRight.setTargetPosition(hang);
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
