package frc.robot.components;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.common.PID;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import frc.robot.components.EncoderGroup;
import java.lang.Math;

public class Drivetrain {
    //Sensor 
    private AHRS navx;
    //Motors 
    private CANSparkMax leftFront;

    private CANSparkMax leftRear;

    private CANSparkMax rightFront;

    private CANSparkMax rightRear;

    private SpeedControllerGroup left, right;
    private EncoderGroup leftEncoder, rightEncoder; 
    public DifferentialDrive drive;

    private double deadBand = 0.0;
    private PID PID = new PID(0.30, 0.00, 0.01);
    private double speed = 0;
    private double rotation = 0;
    private DifferentialDriveOdometry odometry;
    private Field2d field = new Field2d();

    public Drivetrain(CANSparkMax leftFront,CANSparkMax leftRear,CANSparkMax rightFront,  CANSparkMax rightRear,AHRS navX) {
        
        // LEFT FRONT
        this.leftFront = leftFront;

        // LEFT REAR
        this.leftRear = leftRear;
    
        this.left = new SpeedControllerGroup(leftFront, leftRear);

        // RIGHT FRONT
        this.rightFront = rightFront;

        // RIGHT REAR
        this.rightRear = rightRear;
        this.right = new SpeedControllerGroup(this.rightFront, this.rightRear);

        this.leftEncoder = new EncoderGroup(this.leftFront.getEncoder(), this.leftRear.getEncoder());
        this.rightEncoder = new EncoderGroup(this.rightFront.getEncoder(), this.rightRear.getEncoder());
        this.drive = new DifferentialDrive(left, right);

        resetEncoders();

        //Sensor 
        this.navx = navX; 
        this.odometry = new DifferentialDriveOdometry(this.getRotation2d()); // Get the current angle and converts to rads 


    }


    public void setDeadBand(double deadBand) {
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
        drive.curvatureDrive(this.speed, this.rotation, isQuickTurn);
    }

    public void tankDriveVolts(double leftVolts, double rightVolts) {
        left.setVoltage(leftVolts);
        right.setVoltage(-rightVolts);
        drive.feed();
      }

    private Rotation2d getRotation2d(){
        return Rotation2d.fromDegrees(this.navx.getAngle());
    }

    private void resetEncoders(){
        this.rightEncoder.reset();
        this.leftEncoder.reset();
    }
    
    public Pose2d getPose() {
        return odometry.getPoseMeters();
    }

    private void resetOdomentry(Pose2d pose){
        this.resetEncoders();
        odometry.resetPosition(pose, this.getRotation2d());
    }
    

    public double getAverageEncoderDistance() {
        return (leftEncoder.getDistance() + rightEncoder.getDistance()) / 2.0;
    }

    public void zeroHeading() {
        this.navx.reset();
    }
    public double getHeading(){
        return this.navx.getRoll();
    }
    public double getTurnRate() {
        return -(this.navx.getRate());
    }
    
    public void update(){
        SmartDashboard.putNumber("Average Encoder Distance", this.getAverageEncoderDistance());
        SmartDashboard.putNumber("Robot Heading", this.getHeading());
        SmartDashboard.putNumber("Robot Turning Rate", this.getTurnRate());
        SmartDashboard.putNumber("Left Encoder Group Distance", this.leftEncoder.getDistance());
        SmartDashboard.putNumber("Right Encoder Group Distance", this.rightEncoder.getDistance());
        odometry.update(this.getRotation2d(), this.leftEncoder.getDistance(), this.rightEncoder.getDistance());
        field.setRobotPose(odometry.getPoseMeters());
        SmartDashboard.putData("Field", field);
    }

}