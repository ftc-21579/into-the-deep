package org.firstinspires.ftc.teamcode.common.util;

import static org.firstinspires.ftc.teamcode.common.util.SussyCommandGroupBase.registerGroupedCommands;
import static com.arcrobotics.ftclib.command.CommandGroupBase.requireUngrouped;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;
import com.arcrobotics.ftclib.command.CommandGroupBase;

/**
 * A command that runs another command repeatedly, restarting it when it ends, until this command is
 * interrupted. While this class does not extend {@link CommandGroupBase}, it is still considered a
 * CommandGroup, as it allows one to compose another command within it; the command instances that
 * are passed to it cannot be added to any other groups, or scheduled individually.
 *
 * <p>As a rule, CommandGroups require the union of the requirements of their component commands.
 *
 * @author Ryan
 */
public class SussyRepeatCommand extends CommandBase {

    protected final Command m_command;

    /**
     * Creates a new SussyRepeatCommand. Will run another command repeatedly but extremely sussy, restarting it whenever it
     * ends, until this command is interrupted.
     *
     * @param command the sussy command to run repeatedly
     */
    public SussyRepeatCommand(Command command) {
        requireUngrouped(command);
        registerGroupedCommands(command);
        m_command = command;
        m_requirements.addAll(command.getRequirements());
    }


    @Override
    public void initialize() {
        m_command.initialize();
    }


    @Override
    public void execute() {
        if (m_command.isFinished()) {
            // restart command
            //m_command.end(false);
            m_command.initialize();
        } else {
            m_command.execute();
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        m_command.end(interrupted);
    }

    @Override
    public boolean runsWhenDisabled() {
        return m_command.runsWhenDisabled();
    }
}