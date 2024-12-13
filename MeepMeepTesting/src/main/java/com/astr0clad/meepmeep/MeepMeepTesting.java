package com.astr0clad.meepmeep;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(13, 18)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(12, -63, Math.toRadians(-90)))
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(6, -34), Math.toRadians(90))
                .waitSeconds(.5)
                .splineToLinearHeading(new Pose2d(24, -48, Math.toRadians(0)), Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(40, -38, Math.toRadians(45)), Math.toRadians(45))
                .waitSeconds(.5)
                .turn(Math.toRadians(-45))
                .strafeTo(new Vector2d(-56, -56))
                .waitSeconds(.5)
                .strafeToLinearHeading(new Vector2d(-58, -48), Math.toRadians(90))
                .waitSeconds(.5)
                .strafeToLinearHeading(new Vector2d(-56, -56), Math.toRadians(45))
                .splineToLinearHeading(new Pose2d(48, -48, Math.toRadians(90)), Math.toRadians(0))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_INTO_THE_DEEP_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}