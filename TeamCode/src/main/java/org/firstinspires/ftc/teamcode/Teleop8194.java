package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Disabled
@TeleOp(name = "8194 Teleop", group = "8194")
public class Teleop8194 extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    //Motor Declarations
    private DcMotorEx leftBack = null;
    private DcMotorEx rightBack = null;

    private DcMotorEx leftFront = null;
    private DcMotorEx rightFront = null;

    private DcMotorEx bucketExtender = null;
    private DcMotorEx intakeExtender = null;

    private Servo bucketServo = null;
    private Servo clawRotateServo = null;

    private CRServo clawServo = null;

    private static final int LINEAR_EXTENDER_MIN = 0;
    private static final int LINEAR_EXTENDER_MAX = 40000;
    private static final int LINEAR_EXTENDER_CHANGE_RATE = 30;

    private int bucketExtenderPosition = 0;
    private int bucketExtenderOriginalPosition = 0;

    public void runOpMode() throws InterruptedException {
//        try {
            //Telemetry Initialization ################################################################
            telemetry.setAutoClear(false);

            Telemetry.Item timeVal = telemetry.addData("Elapsed Time:", runtime);
            //------------------------------------------------------
            Telemetry.Item SPACER1 = telemetry.addData("---------------------", "-");
            //------------------------------------------------------
            Telemetry.Item G1LYText = telemetry.addData("G1 LY Val:", "-");
            Telemetry.Item G1LXText = telemetry.addData("G1 LX Val:", "-");
            Telemetry.Item G1RXText = telemetry.addData("G1 RX Val:", "-");
            //------------------------------------------------------
            Telemetry.Item SPACER5 = telemetry.addData("---------------------", "-");
            //------------------------------------------------------
            Telemetry.Item G2LYText = telemetry.addData("G2 LY Val:", "-");
            Telemetry.Item G2RYText = telemetry.addData("G2 RY Val:", "-");
            Telemetry.Item G2LTText = telemetry.addData("G2 LTrigger Val:", "-");
            Telemetry.Item G2RTText = telemetry.addData("G2 RTrigger Val:", "-");
            Telemetry.Item BucketTargetText = telemetry.addData("Bucket Encoder Target: ", "-");
            Telemetry.Item BucketEncoderText = telemetry.addData("Bucket Encoder Value: ", "-");
            //##########################################################################################

            //Motor Initialization #####################################################################
            leftBack = hardwareMap.get(DcMotorEx.class, "leftBackWheel");
            rightBack = hardwareMap.get(DcMotorEx.class, "rightBackWheel");
            leftFront = hardwareMap.get(DcMotorEx.class, "leftFrontWheel");
            rightFront = hardwareMap.get(DcMotorEx.class, "rightFrontWheel");

            leftBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            bucketExtender = hardwareMap.get(DcMotorEx.class, "bucketExtenderMotor");
            leftBack.setDirection(DcMotorEx.Direction.REVERSE);
            rightBack.setDirection(DcMotorEx.Direction.FORWARD);
            rightFront.setDirection(DcMotorEx.Direction.FORWARD);
            leftFront.setDirection(DcMotorEx.Direction.REVERSE);

            bucketExtenderPosition = bucketExtenderOriginalPosition = bucketExtender.getCurrentPosition();
            bucketExtender.setTargetPosition(bucketExtender.getCurrentPosition());
            bucketExtender.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            bucketServo = hardwareMap.get(Servo.class, "bucketServo");
            bucketServo.setPosition(0);
            telemetry.addData("Status:", "Initalized");
            waitForStart();
            runtime.reset();
            while (opModeIsActive()) {
                // Velocity Vars
                double leftBackVelocity = leftBack.getVelocity();
                double rightBackVelocity = rightBack.getVelocity();
                double leftFrontVelocity = leftFront.getVelocity();
                double rightFrontVelocity = rightFront.getVelocity();

                //Inputs
                double G1LY = -gamepad1.left_stick_y;
                double G1LX = gamepad1.left_stick_x;
                double G1RX = gamepad1.right_stick_x;

                double G2LY = -gamepad2.left_stick_y;
                double G2RY = -gamepad2.right_stick_y;
                double lTrigger = gamepad2.left_trigger;
                double rTrigger = gamepad2.right_trigger;


                //Target Setting;
                double denominator = Math.max(Math.abs(G1LY) + Math.abs(G1LX) + Math.abs(G1RX), 1);
                double frontLeftPower = (G1LY + G1LX + G1RX) / denominator;
                double backLeftPower = (G1LY - G1LX + G1RX) / denominator;
                double frontRightPower = (G1LY - G1LX - G1RX) / denominator;
                double backRightPower = (G1LY + G1LX - G1RX) / denominator;

                //Power Setting
                leftBack.setPower(backLeftPower);
                rightBack.setPower(backRightPower);
                leftFront.setPower(frontLeftPower);
                rightFront.setPower(frontRightPower);

                if(gamepad1.x){
                    bucketServo.setPosition(0);
                }
                else if (gamepad1.y){
                    bucketServo.setPosition(1);
                }

                if (gamepad1.dpad_up) {
                    bucketExtenderPosition += LINEAR_EXTENDER_CHANGE_RATE;
                } else if (gamepad1.dpad_down) {
                    bucketExtenderPosition -= LINEAR_EXTENDER_CHANGE_RATE;
                }

                if (bucketExtenderPosition > (bucketExtenderOriginalPosition + LINEAR_EXTENDER_MAX)) {
                    bucketExtenderPosition = (bucketExtenderOriginalPosition + LINEAR_EXTENDER_MAX);
                }
                if (bucketExtenderPosition < (bucketExtenderOriginalPosition + LINEAR_EXTENDER_MIN)) {
                    bucketExtenderPosition = (bucketExtenderOriginalPosition + LINEAR_EXTENDER_MIN);
                }

                bucketExtender.setPower(1);
                bucketExtender.setTargetPosition(bucketExtenderPosition);

                //Telemetry Updates
                timeVal.setValue(getRuntime());
                G1LYText.setValue(G1LY);
                G1LXText.setValue(G1LX);
                G1RXText.setValue(G1RX);

                G2LYText.setValue(G2LY);
                G2RYText.setValue(G2RY);
                G2RTText.setValue(rTrigger);
                G2LTText.setValue(lTrigger);
                BucketTargetText.setValue(bucketExtenderPosition);
                BucketEncoderText.setValue(bucketExtender.getCurrentPosition());
                telemetry.update();

            }
//        } catch (Exception ignored) {


    }
}