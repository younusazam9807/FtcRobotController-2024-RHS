package org.firstinspires.ftc.teamcode;
// TODO: imports

/**
 * Runs a servo device to a position controlled by the left joystick.
 */
@TeleOp(name = "Drivetrain Motor Direction", group = "8194/Tests")
public class ServoPositionTest extends LinearOpMode {

    private static final double MAX_SERVO_MOVE_SPEED = 0.1;

    private double servoPosition = 0;

    // Servos
    private Servo testServo = null;

    private void init() {
        // Initialize servos
        testServo = hardwareMap.get(Servo.class, "testServo");

        // Set servos to run forwards
        testServo.setDirection(Servo.Direction.FORWARDS);
    }

    private void moveServo() {
        double speedMod = gamepad1.left_stick_y * MAX_SERVO_MOVE_SPEED;
        servoPosition = Math.clamp(servoPosition + speedMod, 0, 1);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        init();
        while (opModeIsActive()) {
            driveMotors();
        }
    }
    
}