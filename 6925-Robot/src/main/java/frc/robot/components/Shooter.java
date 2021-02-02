package frc.robot.components;

import java.lang.Math;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.robot.components.ShootingTrajectory;
import com.revrobotics.CANSparkMax;


public class Shooter{
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
      
        
        shooter.configFactoryDefault();
       

        shooter.config_kF(0, kF, 30);
        shooter.config_kP(0, kP, 30);
        shooter.config_kI(0, kI, 30);
        shooter.config_kD(0, kD, 30);
      
        this.shootingTrajectory = new ShootingTrajectory()
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
        double shooter = (shooter.getSelectedSensorVelocity()/4096)*(2*0.0762*Math.PI);
        double[] temp = {shooter};
        return temp;
    

    public void setVelocity(double velocity){
        shooter.set(ControlMode.Velocity, velocity);
        shooterSpeed = convertRPM(shooter.getIntegratedSensorVelocity());
    }

   public void runShooter(){
       double velocity = shooterTrajectory.initialVelocity();
       this.velocity = velocity;
       this.shooter.set(velocity);
   }
    
    public void runConveyor(){
        if (shooter.getIntegratedSensorVelocity() > minShooterValue && shooter.getIntergratedSensorVelocity() < maxShooterValue){
            this.conveyor.set(1);
        }
    }

    public void conveyorOff{
        this.conveyor.set(0);
    }

    public void shootOff(){
        this.shooter.set(0);
    }
}