package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="WheelControlTestTeleop", group="DriveTest")
public class WheelControlTestTeleop extends LinearOpMode {
    private DcMotor leftFrontWheel = null;
    private DcMotor rightFrontWheel = null;
    private DcMotor rightBackWheel = null;
    private DcMotor leftBackWheel = null;
    private Servo airplaneLauncher = null;
    private Servo claw = null;
    private DcMotor parallelArm = null;
    private ElapsedTime runtime = new ElapsedTime();
    private int armPosition = 0;

    public void runOpMode() {
        leftFrontWheel = hardwareMap.get(DcMotor.class, "leftFrontWheel");
        rightFrontWheel = hardwareMap.get(DcMotor.class, "rightFrontWheel");
        leftBackWheel = hardwareMap.get(DcMotor.class, "leftBackWheel");
        rightBackWheel = hardwareMap.get(DcMotor.class, "rightBackWheel");
        //linearExtenderUp = hardwareMap.get(DcMotor.class, "linearExtenderUp");
        leftBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set the direction motors will move
        leftFrontWheel.setDirection(DcMotor.Direction.FORWARD);
        rightFrontWheel.setDirection(DcMotor.Direction.REVERSE);
        rightBackWheel.setDirection(DcMotor.Direction.REVERSE);
        leftBackWheel.setDirection(DcMotor.Direction.REVERSE);

        airplaneLauncher = hardwareMap.get(Servo.class, "airplaneLauncher");
        claw = hardwareMap.get(Servo.class, "claw");
        parallelArm = hardwareMap.get(DcMotor.class, "parallelArm");
        parallelArm.setDirection(DcMotor.Direction.FORWARD);
        parallelArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //parallelArm.setTargetPosition(0);
        parallelArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //parallelArm.setPower(0.01);
        armPosition = parallelArm.getCurrentPosition();

        airplaneLauncher.setPosition(0.2);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            armPosition = parallelArm.getCurrentPosition();
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

            // TODO: switch these to gamepad2, they'll be controlled by the second player
            if (gamepad1.back) {
                airplaneLauncher.setPosition(0);
            } else {
                airplaneLauncher.setPosition(0.7);
            }

            if (gamepad1.left_trigger >= 0.8) {
                telemetry.addData("moving claw", true);
                claw.setPosition(1);
            } else {
                claw.setPosition(0.5);
            }

            parallelArm.setPower(0);
            if (gamepad1.a) {
                parallelArm.setPower(-0.4);
            }
            if (gamepad1.x) {
                parallelArm.setPower(0.4);
            }


            telemetry.addData("armPosition", armPosition);

            telemetry.update();

            //parallelArm.setTargetPosition((int)armPositionDouble);


        }
    }
}