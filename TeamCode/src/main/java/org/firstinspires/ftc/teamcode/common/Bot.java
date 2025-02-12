package org.firstinspires.ftc.teamcode.common;

import com.arcrobotics.ftclib.command.Robot;
import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.util.Constants;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Ascent;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Intake;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Extension;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Pivot;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.Color;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.common.pedroPathing.constants.LConstants;

public class Bot extends Robot {
    public final Telemetry telem;
    public final HardwareMap hMap;
    public final Gamepad gamepad;
    private ElapsedTime clock = new ElapsedTime();

    public BotState state = BotState.DEPOSIT;
    private GameElement targetElement = GameElement.SAMPLE;
    private TargetMode targetMode = TargetMode.HIGH_BASKET;
    private static Color allianceColor = Color.NONE;
    private Color gameElementColor = Color.NONE;
    private boolean robotCentric = true;
    private boolean enableDrive = true;

    private final Intake claw;
    private final Extension extension;
    private final Wrist wrist;
    private final Pivot pivot;
    private final Ascent ascent;
    private final Follower follower;

    private final RevBlinkinLedDriver blinkin;

    private final Pose startPose = new Pose(0, 0, 0);


    public Bot(Telemetry telem, HardwareMap hMap, Gamepad gamepad, boolean enableDrive) {
        this.telem = telem;
        this.hMap = hMap;
        this.gamepad = gamepad;
        clock.reset();

        blinkin = hMap.get(RevBlinkinLedDriver.class, "blinkin");

        /* Subsystems */
        claw = new Intake(this);
        wrist = new Wrist(this);
        pivot = new Pivot(this);
        extension = new Extension(this);
        ascent = new Ascent(this);

        Constants.setConstants(FConstants.class, LConstants.class);
        follower = new Follower(hMap);
        follower.setStartingPose(startPose);
    }

    public void resetTime() {
        clock.reset();
    }

    public double getTime() {
        return clock.seconds();
    }

    /**
     * Get the Claw subsystem of the robot
     * @return the claw subsystem of the robot
     */
    public Intake getClaw() { return claw; }

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
     * Get the Pivot subsystem of the robot
     * @return the pivot subsystem of the robot
     */
    public Ascent getAscent() { return ascent; }

    public Follower getFollower() { return follower; }

    public void setRobotCentric(boolean robotCentric) { this.robotCentric = robotCentric; }

    public boolean getRobotCentric() { return robotCentric; }

    public void setEnableDrive(boolean enableDrive) { this.enableDrive = enableDrive; }

    public boolean getEnableDrive() { return enableDrive; }

    /**
     * Get the Blinkin subsystem of the robot
     * @return the blinkin subsystem of the robot
     */
    public RevBlinkinLedDriver getBlinkin() { return blinkin; }

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
     * Get the target element of the robot
     * @param element - the target element of the robot
     */
    public void setTargetElement(GameElement element) {
        targetElement = element;
        //telem.addData("element", targetElement);

        if (targetElement == GameElement.SAMPLE) {
            gamepad.setLedColor(255, 255, 0, Gamepad.LED_DURATION_CONTINUOUS);
        } else if (targetElement == GameElement.SPECIMEN) {
            gamepad.setLedColor(0, 0, 255, Gamepad.LED_DURATION_CONTINUOUS);
        }
    }

    /**
     * Get the target element of the robot
     * @return GameElement - the target element of the robot
     */
    public GameElement getTargetElement() { return targetElement; }

    /**
     * Get the target mode of the robot
     * @return TargetMode - the target mode of the robot
     */
    public TargetMode getTargetMode() { return targetMode; }

    /**
     * Set the target mode of the robot
     * @param targetMode - the target mode to set the robot to
     */
    public void setTargetMode(TargetMode targetMode) { this.targetMode = targetMode; }

    public void setGameElementColor(Color color) { gameElementColor = color; }

    public void setAllianceColor(Color color) { allianceColor = color; }

    public Gamepad getGamepad() {
        return gamepad;
    }

    public Color getAllianceColor() {
        return allianceColor;
    }
}
