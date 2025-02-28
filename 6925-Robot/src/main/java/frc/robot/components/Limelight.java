package frc.robot.components;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.lang.Math;


/** 
 * This class focuses on vision processing for the robot limelight. First, it sets the camera modes for
 * the limelight LED. This is also paired with a boolean statement that tells whether the LED is on or off. It has methods that use the limelight to get the tx and ty values as well as the targe area.
 * Then, there is another method that determines whether a target is in sight.
 * There is also a test program that is set up for the robot. It uses arcade drive to both
 * steer towards and follow the target.
 * Finally, there is a method that uses some set values in order to find the distance from the limelight to the target.
 */

public class Limelight{

    private NetworkTable table;
    private boolean isLedOn = false;
    
     
    public Limelight(){ 
        this.table = NetworkTableInstance.getDefault().getTable("limelight"); //Sets the limelight status

    }

    // Turns on the limelight LED
    public void LedOn(){
        this.table.getEntry("ledMode").setNumber(3);
        isLedOn = true;
    }

    // Turns off he Limelight LED
    public void LedOff(){
        this.table.getEntry("ledMode").setNumber(1);
        isLedOn = false;
    }
    public boolean getLedStatus(){
        return this.isLedOn;
    }

    // Get's the vertical value
    public double getY(){
        NetworkTableEntry ty = this.table.getEntry("ty");
        return ty.getDouble(0.0);
    }

    // Gets the horizontal value
    public double getX(){
        NetworkTableEntry tx = this.table.getEntry("tx");
        return tx.getDouble(0.0);

    }

    // Gets the target area
    public double getArea(){
        NetworkTableEntry ta = this.table.getEntry("ta");
        return ta.getDouble(0.0);

    }

    
    // Checks if there is a valid target
    public boolean hasValidTarget(){
        NetworkTableEntry tv = this.table.getEntry("tv");
        double _tv =  tv.getDouble(0.0);
        if(_tv >= 1.0){
            return true; 
        }
        return false;
    }

    // Robot steers and moves towards target at set speed
    public double[] moveToTarget(){
        final double STEER_K = 0.03;                    // how hard to turn toward the target
        final double DRIVE_K = 0.26;                    // how hard to drive fwd toward the target
        final double DESIRED_TARGET_AREA = 13.0;        // Area of the target when the robot reaches the wall
        final double MAX_DRIVE = 0.7;                   // Simple speed limit so we don't drive too fast

    
        if(!hasValidTarget()){
            double[] driveValues = {0.0, 0.0};
            return driveValues;
        }else{
            double steer_cmd = this.getX() * STEER_K;
            // try to drive forward until the target area reaches our desired area
            double drive_cmd = (DESIRED_TARGET_AREA - this.getArea()) * DRIVE_K;
                    // don't let the robot drive too fast into the goal
            if (drive_cmd > MAX_DRIVE)
            {
            drive_cmd = MAX_DRIVE;
            }
            double[] driveValues = {drive_cmd, steer_cmd};
            return driveValues;
        }


    }

    public double getDistance(){
      //Finds the distance from the limelight to the target area
        double h1 = DriveConstants.robotShooterHeight; //Hight of the limelight mounted on the robot (CHANGE LATER)
        double h2 = 1.90; //Hight of the goal
        double a1 = DriveConstants.robotShooterAngle; //Fixed angle of shooter on robot (CHANGE LATER)
        double a2 = this.getY(); 
        
        double distance = (h2-h1)/Math.tan(Math.toRadians(a1)-Math.toRadians(a2));
        if(distance < 0){
            return 0;
        }else{
            return distance;
        }
        
    }

}
