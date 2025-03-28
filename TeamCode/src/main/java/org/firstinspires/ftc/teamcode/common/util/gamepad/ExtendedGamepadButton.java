package org.firstinspires.ftc.teamcode.common.util.gamepad;

import androidx.annotation.NonNull;

import com.arcrobotics.ftclib.command.button.Button;
import com.arcrobotics.ftclib.gamepad.GamepadEx;

/**
 * A {@link Button} that gets its state from a {@link GamepadEx}.
 *
 * @author Jackson
 */
public class ExtendedGamepadButton extends Button {

    private final ExtendedGamepadEx m_gamepad;
    private final ExtendedGamepadKeys.Button[] m_buttons;

    /**
     * Creates a gamepad button for triggering commands.
     *
     * @param gamepad the gamepad with the buttons
     * @param buttons the specified buttons
     */
    public ExtendedGamepadButton(ExtendedGamepadEx gamepad, @NonNull ExtendedGamepadKeys.Button... buttons) {
        m_gamepad = gamepad;
        m_buttons = buttons;
    }

    /**
     * Gets the value of the joystick button.
     *
     * @return The value of the joystick button
     */
    @Override
    public boolean get() {
        boolean res = true;
        for (ExtendedGamepadKeys.Button button : m_buttons)
            res = res && m_gamepad.getButton(button);
        return res;
    }

}