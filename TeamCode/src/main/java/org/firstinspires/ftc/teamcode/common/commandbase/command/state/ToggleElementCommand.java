package org.firstinspires.ftc.teamcode.common.commandbase.command.state;

import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.ParallelCommandGroup;
import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.command.WaitCommand;
import com.arcrobotics.ftclib.geometry.Vector2d;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

import org.firstinspires.ftc.teamcode.common.Bot;
import org.firstinspires.ftc.teamcode.common.commandbase.command.BlinkinCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.extension.SetExtensionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.pivot.SetPivotAngleCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.command.wrist.SetWristPositionCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.subsystem.Wrist;
import org.firstinspires.ftc.teamcode.common.intothedeep.BotState;
import org.firstinspires.ftc.teamcode.common.intothedeep.Color;
import org.firstinspires.ftc.teamcode.common.intothedeep.GameElement;
import org.firstinspires.ftc.teamcode.common.intothedeep.TargetMode;
import org.firstinspires.ftc.teamcode.common.util.BlinkinUtil;

public class ToggleElementCommand extends CommandBase {
    private final Bot bot;

    public ToggleElementCommand(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void initialize() {
        BlinkinUtil.setBlinkinDriver(bot.getBlinkin());
        RevBlinkinLedDriver.BlinkinPattern previousPattern = BlinkinUtil.getPattern();
        if (bot.getTargetElement() == GameElement.SAMPLE) {
            bot.setTargetElement(GameElement.SPECIMEN);
            bot.setTargetMode(TargetMode.SPEC_INTAKE);
            if (bot.getAllianceColor() == Color.RED) {
                new SequentialCommandGroup(
                        new BlinkinCommand(bot.getBlinkin(), RevBlinkinLedDriver.BlinkinPattern.STROBE_RED),
                        new WaitCommand(500),
                        new BlinkinCommand(bot.getBlinkin(), previousPattern)
                ).schedule();
            } else {
                new SequentialCommandGroup(
                        new BlinkinCommand(bot.getBlinkin(), RevBlinkinLedDriver.BlinkinPattern.STROBE_BLUE),
                        new WaitCommand(500),
                        new BlinkinCommand(bot.getBlinkin(), previousPattern)
                ).schedule();
            }
            if (bot.getState() == BotState.INTAKE) {
                new ParallelCommandGroup(
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, Wrist.wristDown)),
                        new SetPivotAngleCommand(bot.getPivot(), 10),
                        new SetExtensionCommand(bot.getExtension(), 0)
                ).schedule();
            }
        } else {
            bot.setTargetElement(GameElement.SAMPLE);
            bot.setTargetMode(TargetMode.HIGH_BASKET);
            new SequentialCommandGroup(
                    new BlinkinCommand(bot.getBlinkin(), RevBlinkinLedDriver.BlinkinPattern.STROBE_GOLD),
                    new WaitCommand(500),
                    new BlinkinCommand(bot.getBlinkin(), previousPattern)
            ).schedule();
            if (bot.getState() == BotState.INTAKE) {
                new ParallelCommandGroup(
                        new SetWristPositionCommand(bot.getWrist(), new Vector2d(0, Wrist.wristDown)),
                        new SetPivotAngleCommand(bot.getPivot(), 10),
                        new SetExtensionCommand(bot.getExtension(), 0)
                ).schedule();
            }
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
