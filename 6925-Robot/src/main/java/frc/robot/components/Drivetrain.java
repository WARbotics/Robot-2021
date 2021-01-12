package frc.robot.components;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.common.PID;
import edu.wpi.first.wpilibj.Encoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;

public class Drivetrain {
    //Encoder

    //Motors 
    private CANSparkMax leftFront;
    private CANEncoder leftFrontEncoder;
    private CANSparkMax leftRear;
    private CANEncoder leftRearEncoder;
    private CANSparkMax rightFront;
    private CANEncoder rightFrontEncoder;
    private CANSparkMax rightRear;
    private CANEncoder righRearEncoder;
    private SpeedControllerGroup left, right;
    public DifferentialDrive drive;

    private double deadBand = 0.0;
    private PID PID = new PID(0.30, 0.00, 0.01);
    private double speed = 0;
    private double rotation = 0;

    public Drivetrain(CANSparkMax leftFront,CANSparkMax leftRear,CANSparkMax rightFront,  CANSparkMax rightRear) {
        this.leftFront = leftFront;
        this.leftFrontEncoder = this.leftFront.getEncoder();
        this.leftRear = leftRear;
        this.leftRearEncoder = this.leftRearEncoder.getEncoder();
    
        this.left = new SpeedControllerGroup(leftFront, leftRear);

        this.rightFront = rightFront;
        this.rightFrontEncoder = this.rightFrontEncoder.getEncoder();
        this.rightRear = rightRear;
        this.rightRearEncoder = this.rightRearEncoder.getEncoder();
        this.right = new SpeedControllerGroup(rigthFront, rightRear);
        this.drive = new DifferentialDrive(left, right);
    }


    public void setDeadBand(double deadband) {
        this.deadBand = deadBand;
    }

    public double getSpeed() {
        return speed;
    }

    public double getRotation() {
        return rotation;
    }

    public void curveDrive(double speed, double rotation, boolean isQuickTurn) {
        if (Math.abs(speed) <= this.deadBand) {
            speed = 0;
        }
        if (Math.abs(rotation) <= this.deadBand) {
            rotation = 0;
        }
        this.speed = speed;
        this.rotation = rotation;
        PID.setActual(this.speed);
        drive.curvatureDrive(PID.getRate(), this.rotation, isQuickTurn);
    }
    
    public double getDistance(){
        // Returns the average distance between both encoders and this should only be used for known driving forward
        return (leftEncoder.getDistance() + rightEncoder.getDistance())/2;
    }

}