package org.firstinspires.ftc.teamcode.common.util;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;

@FunctionalInterface
public interface PathSupplier {
    Action getPath(TrajectoryActionBuilder builder);
}