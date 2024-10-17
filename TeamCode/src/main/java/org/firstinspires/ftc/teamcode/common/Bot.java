package org.firstinspires.ftc.teamcode.common;

import com.arcrobotics.ftclib.command.Robot;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;

public class Bot extends Robot {
    private final IMU imu;
    public final Telemetry telem;
    public final HardwareMap hMap;

    private final MecanumDrivetrain drivetrain;
    //private final Vision vision;

    public Bot(Telemetry telem, HardwareMap hMap) {
        this.telem = telem;
        this.hMap = hMap;

        // TODO: Adjust IMU parameters to match hub orientation
        imu = hMap.get(IMU.class, "imu");
        imu.initialize(
            new IMU.Parameters(
                new RevHubOrientationOnRobot(
                    RevHubOrientationOnRobot.LogoFacingDirection.UP,
                    RevHubOrientationOnRobot.UsbFacingDirection.RIGHT
                )
            )
        );

        /* Subsystems */
        //vision = new Vision(this);
        drivetrain = new MecanumDrivetrain(this);
    }


    /**
     * Get the IMU object for the robot
     * @return the IMU object
     */
    public IMU getImu() { return imu; }

    /**
     * Get the MecanumDrivetrain subsystem of the robot
     * @return the mecanum subsystem of the robot
     */
    public MecanumDrivetrain getDrivetrain() { return drivetrain; }

    /**
     * Get the Vision subsystem of the robot
     * @return the vision subsystem object
     */
    //public Vision getVision() { return vision; }
}
