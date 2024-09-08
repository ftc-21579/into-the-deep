package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(group = "Test")
public class LiftTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        DcMotor liftMotor = hardwareMap.get(DcMotor.class, "leftElevationMotor");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                liftMotor.setPower(1.0);
            } else if (gamepad1.left_bumper) {
                liftMotor.setPower(-1.0);
            } else {
                liftMotor.setPower(0.0);
            }

            telemetry.addData("Ticks", (int) (liftMotor.getCurrentPosition()));
            telemetry.addData("Pos", (int) (liftMotor.getCurrentPosition() / 6.2732));
            telemetry.addData("Pos2", (int) (liftMotor.getCurrentPosition() * 0.3556));
            telemetry.update();
        }
    }

}
