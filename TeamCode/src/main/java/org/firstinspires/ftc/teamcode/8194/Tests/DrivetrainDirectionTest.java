package org.firstinspires.ftc.teamcode;
// TODO: imports

/**
 * Runs all motors forwards/backwards based on the Y position of Gamepad1's left joystick.
 * This OpMode can be used to determine which direction to set drivetrain motors in
 * so that they rotate as expected.
 */
@TeleOp(name = "Drivetrain Motor Direction", group = "8194/Tests")
public class DrivetrainDirectionTest extends LinearOpMode {

    private static final double MAX_DRIVE_SPEED = 0.7;

    // Drivetrain Motors
    private DcMotorEx leftBack      = null;
    private DcMotorEx rightBack     = null;
    private DcMotorEx leftFront     = null;
    private DcMotorEx rightFront    = null;

    private void init() {
        // Initialize motors
        leftBack    = hardwareMap.get(DcMotorEx.class, "leftBackWheel");
        rightBack   = hardwareMap.get(DcMotorEx.class, "rightBackWheel");
        leftFront   = hardwareMap.get(DcMotorEx.class, "leftFrontWheel");
        rightFront  = hardwareMap.get(DcMotorEx.class, "rightFrontWheel");

        // Set all motors to run forwards
        leftBack.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setDirection(DcMotor.Direction.FORWARD);
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.FORWARD);
    }

    private void driveMotors() {
        double drive = gamepad1.left_stick_y * MAX_DRIVE_SPEED;
        leftBack    .setPower(drive);
        rightBack   .setPower(drive);
        leftFront   .setPower(drive);
        rightFront  .setPower(drive);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        init();
        while (opModeIsActive()) {
            driveMotors();
        }
    }

}