package org.firstinspires.ftc.teamcode.common.commandbase.subsystem.vision.apriltags;

import static org.firstinspires.ftc.teamcode.common.util.UnitConversions.inchesToCentimeters;
import static org.firstinspires.ftc.teamcode.common.util.UnitConversions.degreesToRadians;


/**
 * Represents the center of an AprilTag relative to the camera
 * Distance, Bearing, and Elevation are the most useful
 * All GETTERS are in metric CM and radians
 * The CONSTRUCTOR takes in inches and degrees (intended to be used with the AprilTagProcessor/ftcPose)
 */
public class RelativeAprilTag {
    private final int tagID;
    private final double x; // cm
    private final double y; // cm
    private final double z; // cm
    private final double roll; // rad
    private final double pitch; // rad
    private final double yaw; // rad
    private final double distance; // cm
    private final double bearing; // rad
    private final double elevation; // rad

    /**
     * Constructor for RelativeAprilTag
     * @param tagID The ID of the tag
     * @param x inches in
     * @param y inches in
     * @param z inches in
     * @param roll degrees in
     * @param pitch degrees in
     * @param yaw degrees in
     * @param distance inches in
     * @param bearing degrees in
     * @param elevation degrees in
     */
    public RelativeAprilTag(int tagID, double x, double y, double z, double roll, double pitch, double yaw, double distance, double bearing, double elevation) {
        this.tagID = tagID;
        this.x = inchesToCentimeters(x);
        this.y = inchesToCentimeters(y);
        this.z = inchesToCentimeters(z);
        this.roll = degreesToRadians(roll);
        this.pitch = degreesToRadians(pitch);
        this.yaw = degreesToRadians(yaw);
        this.distance = inchesToCentimeters(distance);
        this.bearing = degreesToRadians(bearing);
        this.elevation = degreesToRadians(elevation);
    }

    public int getTagId() {
        return tagID;
    }

    /**
     * Get the x coordinate of the tag relative to the camera
     * @return x coordinate in centimeters
     */
    public double getX() {
        return x;
    }

    /**
     * Get the y coordinate of the tag relative to the camera
     * @return y coordinate in centimeters
     */
    public double getY() {
        return y;
    }

    /**
     * Get the z coordinate of the tag relative to the camera
     * @return z coordinate in centimeters
     */
    public double getZ() {
        return z;
    }

    /**
     * Get the roll of the tag relative to the camera
     * @return roll in radians
     */
    public double getRoll() {
        return roll;
    }

    /**
     * Get the pitch of the tag relative to the camera
     * @return pitch in radians
     */
    public double getPitch() {
        return pitch;
    }

    /**
     * Get the yaw of the tag relative to the camera
     * @return yaw in radians
     */
    public double getYaw() {
        return yaw;
    }

    /**
     * Get the distance of the tag relative to the camera
     * @return distance in centimeters
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Get the bearing of the tag relative to the camera
     * @return bearing in radians
     */
    public double getBearing() {
        return bearing;
    }

    /**
     * Get the elevation of the tag relative to the camera
     * @return elevation in radians
     */
    public double getElevation() {
        return elevation;
    }
}
