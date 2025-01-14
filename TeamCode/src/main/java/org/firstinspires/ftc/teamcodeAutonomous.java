package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name = "5709Auto", group = "Blue")
public class teamAuto extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    //Motor Declarations
    private DcMotorEx leftBack = null;
    private DcMotorEx rightBack = null;

    private DcMotorEx leftFront = null;
    private DcMotorEx rightFront = null;
    private CRServo clawLeft = null;


    public void turn(double pow, int time){
        runtime.reset();
        while (runtime.seconds() < time) {
            leftBack.setPower(pow);
            rightBack.setPower(-pow);
            leftFront.setPower(pow);
            rightFront.setPower(-pow);
        }
        //neg = left, pos = right
    }

    public void moveStraight(double pow, int time){
        runtime.reset();
        while (runtime.seconds() < time) {
            leftBack.setPower(pow);
            rightBack.setPower(pow);
            leftFront.setPower(pow);
            rightFront.setPower(pow);
        }
    }
    
    public void stopBot(){
        leftBack.setPower(0);
        rightBack.setPower(0);
        leftFront.setPower(0);
        rightFront.setPower(0);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.setAutoClear(false);

        Telemetry.Item timeVal = telemetry.addData("Elapsed Time:", runtime);
        //------------------------------------------------------
        Telemetry.Item SPACER1 = telemetry.addData("---------------------", "-");
        //------------------------------------------------------
        Telemetry.Item leftEncode = telemetry.addData("Left Encoder:", 0);
        Telemetry.Item rightEncode = telemetry.addData("Right Encoder:", 0);
        Telemetry.Item leftEncodef = telemetry.addData("Left Encoder (Front):", 0);
        Telemetry.Item rightEncodef = telemetry.addData("Right Encoder (Front):", 0);

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

        leftBack.setDirection(DcMotorEx.Direction.REVERSE);
        rightBack.setDirection(DcMotorEx.Direction.FORWARD);
        rightFront.setDirection(DcMotorEx.Direction.FORWARD);
        leftFront.setDirection(DcMotorEx.Direction.REVERSE);



        clawLeft = hardwareMap.get(CRServo.class, "clawLeft");
        clawLeft.setDirection(CRServo.Direction.FORWARD);

        waitForStart();
        runtime.reset();

        moveStraight(0.5, 1);
        
    }
}
