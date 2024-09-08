package org.firstinspires.ftc.teamcode.opmode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(group = "Test")
public class LiftTest extends LinearOpMode {

    @Override
    public void runOpMode() {

        DcMotor liftMotor = hardwareMap.get(DcMotor.class, "extensionMotor");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.right_bumper) {
                liftMotor.setPower(1.0);
            } else if (gamepad1.left_bumper) {
                liftMotor.setPower(-1.0);
            } else {
                liftMotor.setPower(0.0);
            }
        }
    }

}
