package org.firstinspires.ftc.teamcode.common.commandbase.subsystem;

import com.acmerobotics.dashboard.config.Config;
import com.arcrobotics.ftclib.command.SubsystemBase;
import com.mineinjava.quail.util.geometry.Pose2d;
import com.mineinjava.quail.util.geometry.Vec2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.hardware.GoBildaPinpointDriver;

import java.util.Locale;

@Config
public class MecanumDrivetrain extends SubsystemBase {
    private final Bot bot;

    private final DcMotorEx frontLeft, frontRight, backLeft, backRight;
    private GoBildaPinpointDriver odo;
    public static boolean fieldCentric = false, headingLock = false;

    private static Pose2D pose;


    public MecanumDrivetrain(Bot bot) {
        this.bot = bot;

        frontLeft = bot.hMap.get(DcMotorEx.class, "frontLeft");
        frontRight = bot.hMap.get(DcMotorEx.class, "frontRight");
        backLeft = bot.hMap.get(DcMotorEx.class, "backLeft");
        backRight = bot.hMap.get(DcMotorEx.class, "backRight");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        this.odo = bot.hMap.get(GoBildaPinpointDriver.class, "odo");
        odo.setOffsets(0, 0); // TODO: Set offsets
        odo.setEncoderResolution(234.057142857);
        odo.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        ); // TODO: Set encoder directions
    }

    @Override
    public void periodic() {
        pose = odo.getPosition();
        bot.telem.addData("Pose",
                String.format(
                        Locale.US, "{X: %.3f, Y: %.3f, H: %.3f}",
                        pose.getX(DistanceUnit.CM),
                        pose.getY(DistanceUnit.CM),
                        pose.getHeading(AngleUnit.DEGREES)
                )
        );

        bot.telem.addData("Pinpoint Frequency", odo.getFrequency());
    }

    public void teleopDrive(Vec2d leftStick, double rx, double multiplier) {
        double x = leftStick.x * multiplier;
        double y = -leftStick.y * multiplier;

        rx *= 0.8;

        if (!fieldCentric) {
            y *= 1.1; // counteract imperfect strafe
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            double[] powers = {frontLeftPower, frontRightPower, backLeftPower, backRightPower};
            double[] normalizedPowers = normalizeWheelSpeeds(powers);

            frontLeft.setPower(normalizedPowers[0]);
            frontRight.setPower(normalizedPowers[1]);
            backLeft.setPower(normalizedPowers[2]);
            backRight.setPower(normalizedPowers[3]);

            return;
        }

        double botHeading = bot.getImu().getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX *= 1.1; // counteract imperfect strafe

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        double[] powers = {frontLeftPower, frontRightPower, backLeftPower, backRightPower};
        double[] normalizedPowers = normalizeWheelSpeeds(powers);

        frontLeft.setPower(normalizedPowers[0]);
        frontRight.setPower(normalizedPowers[1]);
        backLeft.setPower(normalizedPowers[2]);
        backRight.setPower(normalizedPowers[3]);
    }

    // TODO: Try to implement heading lock
    public void toggleHeadingLock() {
        headingLock = !headingLock;
    }

    public void toggleFieldCentric() {
        fieldCentric = !fieldCentric;
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

    public Pose2d getOdoPositionDEG() {
        return new Pose2d(
                pose.getX(DistanceUnit.CM),
                pose.getY(DistanceUnit.CM),
                pose.getHeading(AngleUnit.DEGREES)
        );
    }

    public Pose2d getOdoPositionRAD() {
        return new Pose2d(
                pose.getX(DistanceUnit.CM),
                pose.getY(DistanceUnit.CM),
                pose.getHeading(AngleUnit.RADIANS)
        );
    }

    public void setOdoPositionDEG(Pose2d pose) {
        Pose2D pose2D = new Pose2D(
                DistanceUnit.CM,
                pose.x,
                pose.y,
                AngleUnit.DEGREES,
                pose.heading
        );

        odo.setPosition(pose2D);
    }
}
