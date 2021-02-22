/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.common.AutoCommand;
import frc.robot.common.TrajectoryImporter;
import frc.robot.components.DriveConstants;
import frc.robot.components.Drivetrain;
import frc.robot.components.OI;
import frc.robot.components.Shooter;
import frc.robot.components.ShootingTrajectory;
import frc.robot.components.OI.DriveMode;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import frc.robot.components.Limelight;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.networktables.*;
import frc.robot.components.Intake;
import frc.robot.common.LED;
import frc.robot.common.LED.LEDMode;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private OI input; 
  private AHRS navX;
  private Drivetrain drive;
  private Limelight vision; 
  private AutoCommand testAuto;
  private Shooter shooter;
  private Intake intake;
  private AutoCommand salmonAuto;
  private AutoCommand barrelAuto;
  private AutoCommand bounceAuto;
  private AutoCommand turnTestAuto;
  private ShootingTrajectory trajectory;
  private LED led;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", "Default");
    m_chooser.addOption("Test Straight Line", "Test-Straight-line");
    m_chooser.addOption("Salmon", "Salmon");
    m_chooser.addOption("Barrel", "Barrel");
    m_chooser.addOption("Bounce", "Bounce");
    m_chooser.addOption("Turn Test", "TurnTest");
    SmartDashboard.putData("Auto choices", m_chooser);

    //NavX
    navX = new AHRS(Port.kMXP);
    navX.calibrate();

    //Drivetrain
    drive = new Drivetrain(new CANSparkMax(1,MotorType.kBrushless),
                          new CANSparkMax(2,MotorType.kBrushless),
                          new CANSparkMax(3,MotorType.kBrushless),
                          new CANSparkMax(4,MotorType.kBrushless), 
                          navX);

    // Input
    Joystick driveStick = new Joystick(0);
    Joystick operator = new Joystick(1);
    input = new OI(driveStick, operator);

    //Shooter
    TalonFX shooterMotor = new TalonFX(7);
    TalonFX shooterFollower = new TalonFX(8);
    CANSparkMax conveyorMotor = new CANSparkMax(6, MotorType.kBrushless);

    this.shooter = new Shooter(shooterMotor,  conveyorMotor, shooterFollower, 5);

    //Intake 
    this.intake = new Intake(new CANSparkMax(5, MotorType.kBrushless)); 
    
    //LED 
    this.led = new LED(new Spark(0));
    //Vision
    vision = new Limelight();

    try {
       testAuto = new AutoCommand(TrajectoryImporter.getTrajectory("paths/test.wpilib.json"), drive);
       salmonAuto = new AutoCommand(TrajectoryImporter.getTrajectory("paths/Salmon-path.wpilib.json"),drive);
       barrelAuto = new AutoCommand(TrajectoryImporter.getTrajectory("paths/Barrel-racing-path.wpilib.json"),drive);
       bounceAuto = new AutoCommand(TrajectoryImporter.getTrajectory("paths/Bounce-path.wpilib.json"),drive);
       turnTestAuto =  new AutoCommand(TrajectoryImporter.getTrajectory("paths/turntest.wpilib.json"), drive);
     } catch (Exception IOException) {
       System.out.println("Major ERROR. AUTO Files did not load");
     }
    
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    this.led.setLEDMode(LEDMode.STARTUP);
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    drive.resetOdomentry(new Pose2d());
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case "Test-Straight-line":
        this.testAuto.update();
        break;
      case "Salmon":
        this.salmonAuto.update();
        break;
      case "Barrel":
        this.barrelAuto.update();
       break;
      case "Bounce":
        this.bounceAuto.update();
        break;
      case "TurnTest":
        this.turnTestAuto.update();
        break;
      case "Default":

      default:
        // Put default auto code here
        break;
    }
    this.led.setLEDMode(LEDMode.IDLE);
  }

  /**
   * This function is called once when teleop is enabled.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double driveY = -input.driver.getRawAxis(1);
    double zRotation = input.driver.getRawAxis(2);
    double rightDriveY = input.driver.getRawAxis(3);
    SmartDashboard.putString("Drivemode", input.getDriveMode().name()); // What is the current driving mode 
    // Driving Modes logic
    if (input.getDriveMode() == DriveMode.SPEED) {
      drive.drive.arcadeDrive(driveY, zRotation);
      // Speed
    } else if (input.getDriveMode() == DriveMode.PRECISION) {
      // Double check that they are the right controls
      // Precision
      drive.drive.tankDrive(driveY * .70, -rightDriveY * .70);
      // make turning senetive but forward about .50
    } else {
      drive.drive.arcadeDrive(driveY, zRotation*.85);
    }

    //Shooter
    /*
    *When we press button 4 the limelight will turn on and if the limelight spots the target it runs the conveyor and shooter.
    */
    if (input.operator.getRawButton(1)){
      vision.LedOn();
      
      shooter.runShooter(vision.getDistance());
      shooter.runConveyor();
      this.led.setLEDMode(LEDMode.SHOOTING);
      SmartDashboard.putNumber("LimeLight_distance", vision.getDistance());
    }else{
      vision.LedOff();
      shooter.shootOff();
      shooter.conveyorOff();
    }

    if(input.driver.getRawButton(8)){
      this.intake.on();
    }else if(input.driver.getRawButton(7)){
      this.intake.reverse();
    }else{
      this.intake.off();
    }
    /*
    if(input.operator.getRawButton(3)){
      this.shooter.runShooter();
    }else{
      this.shooter.shootOff();
    }
    */
    if(input.operator.getRawButton(2)){
      this.shooter.runConveyor();
    }else{
      this.shooter.conveyorOff();
    }

    
    
    
    // Driving modes
    if (input.driver.getRawButton(1)) {
      // Set Speed Mode
      /*
      *when we press button 1 the robot goes into speed mood
      */
      input.setDriveMode(DriveMode.SPEED);      
    } else if (input.driver.getRawButton(2)) {
      // Precision
      /*
      *when we press button 2 the robot goes into precision mode.
      */
      input.setDriveMode(DriveMode.PRECISION);
    } else if (input.driver.getRawButton(3)) {
      // Default
      /*
      *when we press button 3 the robot goes into a defulat drive mode.
      */
      input.setDriveMode(DriveMode.DEFAULT);
    }
    /*
    *When we press button 4 the robot goes to zero, zero on the limelight.
    */
    if(input.driver.getRawButton(4)){
      double[] movementValue = this.vision.moveToTarget();
      if(this.vision.hasValidTarget()){
        this.drive.drive.arcadeDrive(movementValue[0], movementValue[1]);
      }else{
        this.drive.drive.arcadeDrive(0, 0);
      }
    }

    
    /*
    *This tells the smart dashboard the limelight X, Y, and area values are
    */
    drive.update();
    SmartDashboard.putNumber("Shooter rotates main", this.shooter.getVelocity()[0]);
    SmartDashboard.putNumber("Limelight X", vision.getX());
    SmartDashboard.putNumber("Limelight Y", vision.getY());
    SmartDashboard.putNumber("Limelight Area", vision.getArea());
    this.led.setLEDMode(LEDMode.IDLE);

  }


  /**
   * This function is called once when the robot is disabled.
   */
  @Override
  public void disabledInit() {
  }

  /**
   * This function is called periodically when disabled.
   */
  @Override
  public void disabledPeriodic() {
  }

  /**
   * This function is called once when test mode is enabled.
   */
  @Override
  public void testInit() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {

  }

}
