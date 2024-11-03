package org.firstinspires.ftc.teamcode.common;

import com.arcrobotics.ftclib.command.Robot;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Claw;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.MecanumDrivetrain;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;

public class Bot extends Robot {
    private final IMU imu;
    public final Telemetry telem;
    public final HardwareMap hMap;

    public BotState state = BotState.INTAKE;

    private final MecanumDrivetrain drivetrain;
    private final Claw claw;
    private final Extension extension;
    private final Wrist wrist;
    private final Pivot pivot;
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
        claw = new Claw(this);
        wrist = new Wrist(this);
        pivot = new Pivot(this);
        extension = new Extension(this);
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
     * Get the Claw subsystem of the robot
     * @return the claw subsystem of the robot
     */
    public Claw getClaw() { return claw; }

    /**
     * Get the Extension subsystem of the robot
     * @return the extension subsystem of the robot
     */
    public Extension getExtension() { return extension; }

    /**
     * Get the Wrist subsystem of the robot
     * @return the wrist subsystem of the robot
     */
    public Wrist getWrist() { return wrist; }

    /**
     * Get the Pivot subsystem of the robot
     * @return the pivot subsystem of the robot
     */
    public Pivot getPivot() { return pivot; }

    /**
     * Get the state of the robot
     * @return BotState - the state of the robot
     */
    public BotState getState() { return state; }

    /**
     * Set the state of the robot
     * @param state - the state to set the robot to
     */
    public void setState(BotState state) { this.state = state; }

    /**
     * Get the Vision subsystem of the robot
     * @return the vision subsystem object
     */
    //public Vision getVision() { return vision; }
}
