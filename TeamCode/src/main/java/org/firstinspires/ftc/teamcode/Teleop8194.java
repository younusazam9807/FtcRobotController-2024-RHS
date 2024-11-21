package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="8194 Teleop", group="DriveTest")
public class Teleop8194 extends LinearOpMode {
    private static final int LINEAR_EXTENDER_MIN = 1000;
    private static final int LINEAR_EXTENDER_MAX = 0;
    private static final int LINEAR_EXTENDER_CHANGE_RATE = 1;
    private static final double CLAW_ARM_MOVE_AMOUNT = -0.1;

    private DcMotor leftFrontWheel = null;
    private DcMotor rightFrontWheel = null;
    private DcMotor rightBackWheel = null;
    private DcMotor leftBackWheel = null;
    private DcMotor linearExtender = null;
    private Servo clawArmControl = null;
    private CRServo clawSpinner = null;
    private ElapsedTime runtime = new ElapsedTime();

    private int linearExtenderPosition = 0;
    private int linearExtenderOriginalPosition = 0;
    private int clawArmOriginalPosition = 0;

    public void runOpMode() {
        leftFrontWheel = hardwareMap.get(DcMotor.class, "leftFrontWheel");
        rightFrontWheel = hardwareMap.get(DcMotor.class, "rightFrontWheel");
        leftBackWheel = hardwareMap.get(DcMotor.class, "leftBackWheel");
        rightBackWheel = hardwareMap.get(DcMotor.class, "rightBackWheel");

        linearExtender = hardwareMap.get(DcMotor.class, "linearExtender");
        clawArmControl = hardwareMap.get(Servo.class, "clawArmControl");
        leftBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        linearExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set the direction motors will move
        leftFrontWheel.setDirection(DcMotor.Direction.FORWARD);
        rightFrontWheel.setDirection(DcMotor.Direction.FORWARD);
        rightBackWheel.setDirection(DcMotor.Direction.FORWARD);
        leftBackWheel.setDirection(DcMotor.Direction.FORWARD);

        clawArmControl.setDirection(Servo.Direction.FORWARD);

        linearExtenderPosition = linearExtenderOriginalPosition = linearExtender.getCurrentPosition();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;
            double ry = gamepad1.right_stick_y;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            leftFrontWheel.setPower(frontLeftPower);
            leftBackWheel.setPower(backLeftPower);
            rightFrontWheel.setPower(frontRightPower);
            rightBackWheel.setPower(backRightPower);

            if (gamepad1.y) {
                clawArmControl.setPosition(clawArmOriginalPosition);
            } else if (gamepad1.x) {
                clawArmControl.setPosition(clawArmOriginalPosition + CLAW_ARM_MOVE_AMOUNT);
            }

            if (gamepad1.left_bumper) {
                clawSpinner.setPower(1);
            } else {
                clawSpinner.setPower(0.5);
            }

            // TODO: Move to gamepad2
            if (gamepad1.dpad_up) {
                linearExtenderPosition += LINEAR_EXTENDER_CHANGE_RATE;
            } else if (gamepad1.dpad_down) {
                linearExtenderPosition -= LINEAR_EXTENDER_CHANGE_RATE;
            }

            if (linearExtenderPosition > (linearExtenderOriginalPosition + LINEAR_EXTENDER_MAX)) {
                linearExtenderPosition = (linearExtenderOriginalPosition + LINEAR_EXTENDER_MAX);
            }
            if (linearExtenderPosition < (linearExtenderOriginalPosition + LINEAR_EXTENDER_MIN)) {
                linearExtenderPosition = (linearExtenderOriginalPosition + LINEAR_EXTENDER_MIN);
            }

            linearExtender.setTargetPosition(linearExtenderPosition);

            telemetry.addData("clawArmControl Position:", clawArmControl.getPosition());
            telemetry.addData("linearExtender Position:", linearExtender.getCurrentPosition());
            telemetry.addData("linearExtender Target:", linearExtenderPosition);
            telemetry.update();
        }
    }
}
