package frc.robot.components;

import java.lang.Math;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.robot.components.ShootingTrajectory;
import com.revrobotics.CANSparkMax;


public class Shooter{
    /**
     * In the Shooter class, we used functions from the shooter trajectory and the limelight code to find out the
    *correct velocity for the shooter in order to launch power cells as accurately and precisely as possible.
    * It has three motors, two for the shooter and one for the conveyor. It sets one of the shooter motors as a follower.
    *It then sets all of our values which included our speed, our wheel radius, our conversion factors, the threshold,
    *the velocity, and our maximum and minimum values for our velocity. It also sets up and PID system.
    *It then goes into converting the velocity and RPM values. It then has functions that get and set the velocity. 
    *We then have a method that begins to run the shooter and
    *sets the motor to the velocity it found. Once the shooter is running, the next method checks the current speed
    *of the motor and if the speed is within the parameters, the conveyor turns on and launches the power cell.
    *There is also a method to turn the shooter and conveyor off.
    *
     */
    



    private TalonFX shooter;
    private TalonFX shooterFollower;
    private CANSparkMax conveyor;

    
    private double shooterSpeed;
    private double kF = 0;
    private double kP = .25;
    private double kI = 0;
    private double kD = 0;
    private double wheelRadius;
    private double wheelConversionFactor;
    private double threshold;
    private double velocity;
    private double minShooterValue = velocity-theshold;
    private double maxShooterValue = velocity+theshold;
    
   
    
    private boolean isRunning = false;

    private ShootingTrajectory shootingTrajectory;



    public Shooter(TalonFX shooter,CANSparkMax conveyor,TalonFX shooterFollower, double threshold){
        this.shooter = shooter;
        this.shooterFollower = shooterFollower;
        shooterFollower.follow(shooter);
        this.conveyor = conveyor;
        this.threshold = threshold;
      
        
        shooter.configFactoryDefault(); //Set the config of the robot
       

        shooter.config_kF(0, kF, 30); // Set the confif for the PID
        shooter.config_kP(0, kP, 30);
        shooter.config_kI(0, kI, 30);
        shooter.config_kD(0, kD, 30);
      
        this.shootingTrajectory = new ShootingTrajectory() //Instantiate the shooter trajectory 
        // Add shooting trajectory values here

    }

    public double convertVelocity(double velocity){
        return (velocity*wheelConversionFactor)/2*Math.PI*wheelRadius;
        //Convert to correct RPM and values later
    }

    public double convertRPM(double RPM){
        return ((RPM * (2*Math.PI))*wheelRadius)/(wheelConversionFactor);
        //Convetr to correct RPM later
    }
    
    public double[] getVelocity(){
        double shooter = (shooter.getSelectedSensorVelocity()/4096)*(2*0.0762*Math.PI); //Finds the optimal velocity for the shooter motor
        double[] temp = {shooter};
        return temp;
    

    public void setVelocity(double velocity){
        shooter.set(ControlMode.Velocity, velocity);
        shooterSpeed = convertRPM(shooter.getIntegratedSensorVelocity()); //Set the shooter velocity and convert the shooter speed.
    }

   //Runs and sets the shooter speed to the correct velecity.
    public void runShooter(){
       double velocity = shooterTrajectory.initialVelocity();
       this.velocity = velocity;
       this.shooter.set(velocity);
   }
    
   //Runs the conveyor when shooter is at the correct speed. 
   public void runConveyor(){
        if (shooter.getIntegratedSensorVelocity() > minShooterValue && shooter.getIntergratedSensorVelocity() < maxShooterValue){
            this.conveyor.set(1);
        }
    }

    //Turns off teh conveyor
    public void conveyorOff{
        this.conveyor.set(0);
    }

    //Turns off the shooter
    public void shootOff(){
        this.shooter.set(0);
    }
}