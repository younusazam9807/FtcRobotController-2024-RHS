package org.firstinspires.ftc.teamcode.team8194.tests;

import androidx.core.math.MathUtils;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Runs a servo device to a position controlled by the left joystick.
 */
@TeleOp(name = "Drivetrain Motor Direction", group = "8194/Tests")
public class ServoPositionTest extends LinearOpMode {

    private static final double MAX_SERVO_MOVE_SPEED = 0.1;

    private double servoPosition = 0;

    // Servos
    private Servo testServo = null;

    private void initialize() {
        // Initialize servos
        testServo = hardwareMap.get(Servo.class, "testServo");

        // Set servos to run forwards
        testServo.setDirection(Servo.Direction.FORWARD);
    }

    private void moveServo() {
        double speedMod = gamepad1.left_stick_y * MAX_SERVO_MOVE_SPEED;
        servoPosition = MathUtils.clamp(servoPosition + speedMod, 0, 1);

        testServo.setPosition(servoPosition);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initialize();
        while (opModeIsActive()) {
            moveServo();
        }
    }
    
}