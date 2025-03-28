package org.firstinspires.ftc.teamcode.common.util;

import com.arcrobotics.ftclib.command.Command;
import com.arcrobotics.ftclib.command.CommandBase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public abstract class SussyCommandGroupBase extends CommandBase implements Command {

    private static final Set<Command> m_groupedCommands =
            Collections.newSetFromMap(new WeakHashMap<>());

    public static void registerGroupedCommands(Command... commands) {
        m_groupedCommands.addAll(Arrays.asList(commands));
    }
}