package frc.robot.common;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.components.DriveConstants;
import frc.robot.components.Drivetrain;

public class AutoCommand {

    private Path trajectoryPath;
    private Trajectory trajectory;
    private RamseteController controller;
    private Timer timer;
    private Drivetrain drive;
    private SimpleMotorFeedforward driveLeftFeedWord;
    private SimpleMotorFeedforward driveRightFeedWord;
    private PIDController leftPID;
    private PIDController rightPID;
    private LinearFilter filter = LinearFilter.singlePoleIIR(0.1, 0.02);
    private LinearFilter filter2 = LinearFilter.singlePoleIIR(0.1, 0.02);
    public boolean isStarted = false;
    public AutoCommand(Path trajectoryPath, Drivetrain drive) {
        this.trajectoryPath = trajectoryPath;
        try {
            this.trajectory = TrajectoryUtil.fromPathweaverJson(this.trajectoryPath);
        } catch (IOException e) {
            DriverStation.reportError("Unable to open trajectory: " + trajectoryPath, e.getStackTrace());
        }
        this.timer = new Timer();
        this.drive = drive;
        this.controller = new RamseteController();
        this.driveLeftFeedWord = new SimpleMotorFeedforward(DriveConstants.ksVolts,
                                   DriveConstants.kvVoltSecondsPerMeter,
                                   DriveConstants.kaVoltSecondsSquaredPerMeter);
        this.driveRightFeedWord = new SimpleMotorFeedforward(DriveConstants.ksVolts,
        DriveConstants.kvVoltSecondsPerMeter,
        DriveConstants.kaVoltSecondsSquaredPerMeter);
        this.leftPID = new PIDController(DriveConstants.kPDriveVel,0,0);
        this.rightPID = new PIDController(DriveConstants.kPDriveVel, 0,0);
                        
    }
    public void start(){
        drive.resetOdomentry(trajectory.getInitialPose());
        drive.zeroHeading();
        timer.start();
        isStarted = true;
    }
    public void update() {
        if(isStarted){
            
            Trajectory.State goal = trajectory.sample(timer.get());
            ChassisSpeeds adjustedSpeeds = controller.calculate(this.drive.getPose(), goal);
            DifferentialDriveWheelSpeeds wheelSpeeds = DriveConstants.kDriveKinematics.toWheelSpeeds(adjustedSpeeds);
            double leftCommand = wheelSpeeds.leftMetersPerSecond;
            double rightCommand = wheelSpeeds.rightMetersPerSecond; 
            double leftSpeed = filter.calculate(this.drive.leftEncoder.getVelocity());
            double rightSpeed = filter2.calculate((this.drive.rightEncoder.getVelocity()));
            double leftFeed = driveLeftFeedWord.calculate(leftCommand);
            double rightFeed = driveRightFeedWord.calculate(rightCommand);
            if(leftFeed > 10){
                leftFeed = 0;
            }
            if(rightFeed > 10){
                rightFeed = 0;
            }
            this.drive.tankDriveVolts(leftFeed,rightFeed);
            // CURRENT issue is that the robot is not moving the distance that we set it to
            // we have checked to see if the distance of the enocder is correct which it is 
            // it could be the average distance traveled which is the distance 
            SmartDashboard.putNumber("Left Feed", leftFeed);
            SmartDashboard.putNumber("right Feed", rightFeed);
            SmartDashboard.putNumber("left Command", leftCommand);
            SmartDashboard.putNumber("right command", rightCommand);
            drive.update();
        }else{
            start();
        }
        
    }



}
