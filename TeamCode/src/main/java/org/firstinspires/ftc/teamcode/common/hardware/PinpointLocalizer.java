package org.firstinspires.ftc.teamcode.common.hardware;

import com.mineinjava.quail.localization.Localizer;
import com.mineinjava.quail.util.geometry.Pose2d;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class PinpointLocalizer implements Localizer {

    private final GoBildaPinpointDriver odo;

    public PinpointLocalizer(GoBildaPinpointDriver odo) {
        this.odo = odo;
    }

    @Override
    public Pose2d getPose() {
        Pose2D pose = odo.getPosition();

        return new Pose2d(
                pose.getX(DistanceUnit.CM),
                pose.getY(DistanceUnit.CM),
                pose.getHeading(AngleUnit.RADIANS)
        );
    }

    @Override
    public void setPose(Pose2d pose) {
        odo.setPosition(new Pose2D(
                DistanceUnit.CM,
                pose.x,
                pose.y,
                AngleUnit.DEGREES,
                pose.heading
        ));
    }
}
