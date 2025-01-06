package org.firstinspires.ftc.teamcode.team8194;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "2 Driver Control", group = "8194/Teleop")
public abstract class Teleop2Controllers extends LinearOpMode {
    /** The maximum speed the drivetrain motors can be driven at. */
    private static final double MAX_DRIVE_SPEED = 0.7;

    /** The maximum speed the drivetrain motors can be driven at while slow mode is on (B is held down.) */
    private static final double SLOW_MODE_MAX_DRIVE_SPEED = 0.3;

    /** The maximum speed the motor controlling the linear extender's arm will move at. */
    private static final double MAX_ARM_MOVE_SPEED = 0.7;

    /** The position in motor ticks of the linear extender motor when the arm is entirely retracted. */
    private static final int LINEAR_EXTENDER_DOWN_POS = 0;

    /** The position in motor ticks of the linear extender motor when the arm is raised to its max length. */
    private static final int LINEAR_EXTENDER_UP_POS = 1000;

    /** The position of the arm servo when the arm is retracted. */
    private static final double ARM_SERVO_IN_POS = 0.0;

    /** The position of the arm servo when the arm is extended to grab game pieces. */
    private static final double ARM_SERVO_OUT_POS = 0.7;

    /** The position of the claw servo when the claw is open. */
    private static final double CLAW_SERVO_OPEN_POS = 0;

    /** The position of the claw servo when the claw is closed. */
    private static final double CLAW_SERVO_CLOSED_POS = 0;

    /** Whether or not the arm should be extended outwards. */
    private boolean armOut = false;


    private boolean isArmOutButtonHeld = false;

    /** Whether or not the claw should be open. */
    private boolean clawOpen = false;

    private boolean isClawOpenButtonHeld = false;

    // Drivetrain Motors
    private DcMotorEx leftBack      = null;
    private DcMotorEx rightBack     = null;
    private DcMotorEx leftFront     = null;
    private DcMotorEx rightFront    = null;
    private DcMotorEx armExtender = null;
    private Servo armServo = null;
    private Servo clawServo = null;

    /**
     * @return the gamepad which will control robot motion.
     */
    protected Gamepad getPrimaryGamepad() {
        return gamepad1;
    }

    /**
     * @return the gamepad which will control the linear extender, arm, and claw.
     */
    protected Gamepad getSecondaryGamepad() {
        return gamepad2;
    }

    /**
     * Initializes devices based on the Robot Controller's hardware configuration.
     */
    private void loadHardwareMap() {
        // Initialize motors
        leftBack    = hardwareMap.get(DcMotorEx.class, "leftBackWheel");
        rightBack   = hardwareMap.get(DcMotorEx.class, "rightBackWheel");
        leftFront   = hardwareMap.get(DcMotorEx.class, "leftFrontWheel");
        rightFront  = hardwareMap.get(DcMotorEx.class, "rightFrontWheel");

        // Set motor directions
        leftBack    .setDirection(DcMotor.Direction.FORWARD);
        rightBack   .setDirection(DcMotor.Direction.FORWARD);
        leftFront   .setDirection(DcMotor.Direction.FORWARD);
        rightFront  .setDirection(DcMotor.Direction.FORWARD);

        // Initialize and reset linear extender motor
        armExtender = hardwareMap.get(DcMotorEx.class, "armExtender");
        armExtender.setDirection(DcMotor.Direction.FORWARD);
        armExtender.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        armServo = hardwareMap.get(Servo.class, "armServo");
        armServo.setDirection(Servo.Direction.FORWARD);

        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawServo.setDirection(Servo.Direction.FORWARD);
    }

    /**
     * Sets the power of drivetrain motors according to the stick inputs of the driver gamepad.
     * The left stick moves the motors relative to the current facing direction,
     * while the right stick rotates the robot left and right.
     * If B is held down, the maximum move speed will be reduced for precise motion.
     */
    private void driveMotors() {
        Gamepad driveGamepad = getPrimaryGamepad();
        double gamepad1leftY    = -driveGamepad.left_stick_y;
        double gamepad1leftX    = driveGamepad.left_stick_x;
        double gamepad1rightX   = driveGamepad.right_stick_x;

        double maxPower;
        if (driveGamepad.b) {
            maxPower = SLOW_MODE_MAX_DRIVE_SPEED;
        } else {
            maxPower = MAX_DRIVE_SPEED;
        }

        // Translate controller inputs into mecanum wheel movement directions
        double denominator = Math.max(Math.abs(gamepad1leftY) + Math.abs(gamepad1leftX) + Math.abs(gamepad1rightX), maxPower);
        double leftFrontPower   = (gamepad1leftY + gamepad1leftX + gamepad1rightX) / denominator;
        double leftBackPower    = (gamepad1leftY - gamepad1leftX + gamepad1rightX) / denominator;
        double rightFrontPower  = (gamepad1leftY - gamepad1leftX - gamepad1rightX) / denominator;
        double rightBackPower   = (gamepad1leftY + gamepad1leftX - gamepad1rightX) / denominator;

        leftBack    .setPower(leftBackPower);
        rightBack   .setPower(rightBackPower);
        leftFront   .setPower(leftFrontPower);
        rightFront  .setPower(rightFrontPower);
    }

    /**
     * Configures the linear extender to run to a position, setting mode and max speed.
     * @param position Position to move to in motor ticks
     */
    private void setLinearExtenderPosition(int position) {
        armExtender.setTargetPosition(position);
        armExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armExtender.setPower(MAX_ARM_MOVE_SPEED);
    }

    /**
     * Sets the movement target of the linear extender based on the direction of the secondary gamepad.
     */
    private void moveLinearExtender() {
        Gamepad linearExtenderGamepad = getSecondaryGamepad();
        if (linearExtenderGamepad.dpad_up) {
            setLinearExtenderPosition(LINEAR_EXTENDER_UP_POS);
        } else if (linearExtenderGamepad.dpad_down) {
            setLinearExtenderPosition(LINEAR_EXTENDER_DOWN_POS);
        }
    }

    /**
     * Toggles whether or not the arm servo is configured to extend the arm outwards when Y is pressed
     * on the secondary gamepad.
     */
    private void toggleArm() {
        Gamepad servoGamepad = getSecondaryGamepad();
        if (servoGamepad.y) {
            if (isArmOutButtonHeld) {
                // The button hasn't been released yet, so don't toggle the value
                return;
            }
            // Toggle the arm's state
            armOut = !armOut;
            isArmOutButtonHeld = true;
            // Set servo position based on the new value
            if (armOut) {
                armServo.setPosition(ARM_SERVO_OUT_POS);
            } else {
                armServo.setPosition(ARM_SERVO_IN_POS);
            }
        } else {
            isArmOutButtonHeld = false;
        }
    }

    /**
     * Toggles whether or not the claw is open when the right bumper is pressed
     * on the secondary gamepad.
     */
    private void toggleClaw() {
        Gamepad clawGamepad = getSecondaryGamepad();
        if (clawGamepad.right_bumper) {
            if (isClawOpenButtonHeld) {
                // The button hasn't been released yet, so don't toggle the value
                return;
            }
            // Toggle the claw's open state
            clawOpen = !clawOpen;
            isClawOpenButtonHeld = true;
            // Set servo position based on the new value
            if (clawOpen) {
                clawServo.setPosition(CLAW_SERVO_OPEN_POS);
            } else {
                clawServo.setPosition(CLAW_SERVO_CLOSED_POS);
            }
        } else {
            isClawOpenButtonHeld = false;
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        init();
        loadHardwareMap();
        while (opModeIsActive()) {
            driveMotors();
            moveLinearExtender();
            toggleArm();
            toggleClaw();
        }
    }

}