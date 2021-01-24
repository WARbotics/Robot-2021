package frc.robot.components;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;


public class Limelight{

    private NetworkTable table; 
    public Limelight(){ 
        this.table = NetworkTableInstance.getDefault().getTable("limelight");
    }
    public double getY(){
        NetworkTableEntry ty = this.table.getEntry("ty");
        return ty.getDouble(0.0);
    }

    public double getX(){
        NetworkTableEntry tx = this.table.getEntry("tx");
        return tx.getDouble(0.0);

    }

    public double getArea(){
        NetworkTableEntry ta = this.table.getEntry("ta");
        return ta.getDouble(0.0);

    }

    
    public boolean hasValidTarget(){
        NetworkTableEntry tv = this.table.getEntry("tv");
        int _tv = (int) tv.getNumber(0);
        if(_tv == 1){
            return true; 
        }
        return false;
    }


}
