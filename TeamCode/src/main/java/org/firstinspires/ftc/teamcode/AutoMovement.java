package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;



/**
 * Unregistered OpMode for autonomous movement.
 * Navigates to the Net to deposit the held item.
 */
public class AutoMovement extends LinearOpMode {
    public enum TeamColor {
        BLUE,
        RED
    }

    protected DcMotor leftFrontWheel = null;
    protected DcMotor rightFrontWheel = null;
    protected DcMotor rightBackWheel = null;
    protected DcMotor leftBackWheel = null;
    protected ElapsedTime runtime = new ElapsedTime();

    private TeamColor getTeamColor() {
        return null;
    }

    private boolean isOnObservationDeck() {
        return false;
    }

    public void turn(double pow, int time){
        runtime.reset();
        while (runtime.seconds() < time) {
            leftBackWheel.setPower(pow);
            rightBackWheel.setPower(-pow);
            leftFrontWheel.setPower(pow);
            rightFrontWheel.setPower(-pow);
        }
        //neg = left, pos = right
    }

    public void moveStraight(double pow, int time){
        runtime.reset();
        while (runtime.seconds() < time) {
            leftBackWheel.setPower(pow);
            rightBackWheel.setPower(pow);
            leftFrontWheel.setPower(pow);
            rightFrontWheel.setPower(pow);
        }
    }

    public void stopBot(){
        leftBackWheel.setPower(0);
        rightBackWheel.setPower(0);
        leftFrontWheel.setPower(0);
        rightFrontWheel.setPower(0);
    }

    public void runOpMode() {
        leftFrontWheel = hardwareMap.get(DcMotor.class, "leftFrontWheel");
        rightFrontWheel = hardwareMap.get(DcMotor.class, "rightFrontWheel");
        leftBackWheel = hardwareMap.get(DcMotor.class, "leftBackWheel");
        rightBackWheel = hardwareMap.get(DcMotor.class, "rightBackWheel");

        leftBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackWheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackWheel.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Set the direction motors will move
        leftFrontWheel.setDirection(DcMotor.Direction.FORWARD);
        rightFrontWheel.setDirection(DcMotor.Direction.REVERSE);
        rightBackWheel.setDirection(DcMotor.Direction.REVERSE);
        leftBackWheel.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        runtime.reset();


    }
}