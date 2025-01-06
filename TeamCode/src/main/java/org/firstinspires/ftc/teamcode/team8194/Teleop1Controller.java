package org.firstinspires.ftc.teamcode.team8194;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

/**
 * Extension of 2 Driver Control mode which binds all controls of the secondary controller to
 * the first controller. To be used if only one driver is available.
 */
@TeleOp(name = "1 Driver ONLY Control", group = "8194/Teleop")
public class Teleop1Controller extends Teleop2Controllers {
    @Override
    protected Gamepad getSecondaryGamepad() {
        return gamepad1;
    }
}
