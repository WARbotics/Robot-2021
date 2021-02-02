import java.lang.Math;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.robot.components.ShootingTrajectory;
import com.revrobotics.CANSparkMax;





public class Shooter{
    private TalonFX shooter;
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


    public Shooter(TalonFX shooter, conveyor){
        this.shooter = shooter;
        this.conveyor = conveyor;


        shooter.configFactoryDefault();
       

        Shooter.config_kF(0, kF, 30);
        Shooter.config_kP(0, kP, 30);
        Shooter.config_kI(0, kI, 30);
        Shooter.config_kD(0, kD, 30);

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
        double shooter = (shooter.getSelectedSensorVelocity(0)/4096)*(2*0.0762*Math.PI);
        double[] temp = {shooter};
        return temp;
    }

    public void setVelocity(double velocity){
        double velocity = shooterTrajectory.InitialVelocity();
        shooter.set(ControlMode.Velocity, velocity);
        shooterSpeed = convertRPM(shooter.getIntegratedSensorVelocity());
    }

   public void runShooter(){
       this.shooter.set(InitialVelocity);
   }
    
    public void runConveyor(){
        if (shooter.getIntegratedSensorVelocity() > minShooterValue && shooter.getIntergratedSensorVelocity() < maxShooterValue){
            this.conveyor.set(1);

        }

    }

    public void conveyorOff{
        this.conveyor.set(0);
    }

    public void off(){
        isRunning = falseshooter.set(ControlMode.PercentOutput, 0);
    }
}