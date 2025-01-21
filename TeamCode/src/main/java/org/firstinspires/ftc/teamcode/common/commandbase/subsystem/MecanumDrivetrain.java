package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.controller.PIDFController;
import com.arcrobotics.ftclib.geometry.Pose2d;
import com.arcrobotics.ftclib.geometry.Rotation2d;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.GoBildaPinpointDriver;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.LConstants;

@Config
public class MecanumDrivetrain extends SubsystemBase {
    private final Bot bot;

    private final DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private final PIDFController ascentController;
    private GoBildaPinpointDriver odo;
    public static boolean robotCentric = false, headingLock = false;

    private static Follower follower;

    public static Pose2D pose;

    private boolean isEncoderMode = false;

    private static final double TICKS_PER_CM = 384.5 / 11.2;
    private double setPointCM = 0.0;
    private static final double hangCM = 45.0;

    public MecanumDrivetrain(Bot bot) {
        this.bot = bot;

        odo = bot.hMap.get(GoBildaPinpointDriver.class,"odo");
        odo.setOffsets(-82.66924000028, 110.830759999962);
        odo.setEncoderResolution(8192 / (Math.PI * 35));
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.REVERSED);

        if (pose == null) {
            pose = new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.RADIANS, 0);
        }

        odo.setPosition(pose);

        frontLeft = bot.hMap.get(DcMotorEx.class, "frontLeft");
        frontRight = bot.hMap.get(DcMotorEx.class, "frontRight");
        backLeft = bot.hMap.get(DcMotorEx.class, "backLeft");
        backRight = bot.hMap.get(DcMotorEx.class, "backRight");

        ascentController = new PIDFController(
                org.firstinspires.ftc.teamcode.common.Config.ascent_kP,
                org.firstinspires.ftc.teamcode.common.Config.ascent_kI,
                org.firstinspires.ftc.teamcode.common.Config.ascent_kD,
                org.firstinspires.ftc.teamcode.common.Config.ascent_kF
        );

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(bot.hMap);
    }

    @Override
    public void periodic() {
        if (isEncoderMode) {
            double targetTicks = setPointCM * TICKS_PER_CM;

            double leftPower = ascentController.calculate(backLeft.getCurrentPosition(), targetTicks);
            double rightPower = ascentController.calculate(backRight.getCurrentPosition(), targetTicks);

            backLeft.setPower(leftPower);
            backRight.setPower(rightPower);
            frontLeft.setPower(leftPower);
            frontRight.setPower(rightPower);

            // Debugging telemetry
            //bot.telem.addData("BAD BAD BAD BAD BAD FUNNY", targetTicks);
        }

        odo.update();
        pose = odo.getPosition();
    }

    public Follower getFollower() {
        return follower;
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

    public void toggleDriveMode(boolean mode) {
        resetEncoders();
        isEncoderMode = mode;
    }

    private double[] normalizeWheelSpeeds(double[] speeds) {
        if (largestAbsolute(speeds) > 1) {
            double max = largestAbsolute(speeds);
            for (int i = 0; i < speeds.length; i++){
                speeds[i] /= max;
            }
        }
        return speeds;
    }

    private double largestAbsolute(double[] arr) {
        double largestAbsolute = 0;
        for (double d : arr) {
            double absoluteValue = Math.abs(d);
            if (absoluteValue > largestAbsolute) {
                largestAbsolute = absoluteValue;
            }
        }
        return largestAbsolute;
    }

    public double getHeadingDEG() {
        return pose.getHeading(AngleUnit.DEGREES);
    }

    public Pose2d getOdoPositionDEG() {
        return new Pose2d(
                pose.getX(DistanceUnit.CM),
                pose.getY(DistanceUnit.CM),
                new Rotation2d(pose.getHeading(AngleUnit.DEGREES))
        );
    }

    public Pose2d getOdoPositionRAD() {
        return new Pose2d(
                pose.getX(DistanceUnit.CM),
                pose.getY(DistanceUnit.CM),
                new Rotation2d(pose.getHeading(AngleUnit.RADIANS))
        );
    }

    public void setOdoPositionDEG(Pose2d pose) {
        Pose2D pose2D = new Pose2D(
                DistanceUnit.CM,
                pose.getX(),
                pose.getY(),
                AngleUnit.DEGREES,
                pose.getHeading()
        );

        odo.setPosition(pose2D);
    }
}
