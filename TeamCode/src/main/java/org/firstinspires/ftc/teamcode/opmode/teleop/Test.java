package org.firstinspires.ftc.teamcode.opmode.teleop;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class Test extends LinearOpMode {
    @Override
    public void runOpMode() {
        //DcMotor motor = hardwareMap.get(DcMotorEx.class, "extension");
        Servo claw = hardwareMap.get(Servo.class, "claw");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.a) {
                claw.setPosition(1.0);
            } else {
                claw.setPosition(0.0);
            }
            //motor.setPower(1.0);
        }
    }
}