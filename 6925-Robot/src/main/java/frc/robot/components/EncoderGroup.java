package frc.robot.components;

import com.revrobotics.CANEncoder;

public class EncoderGroup {
    public CANEncoder encoderLeader; 
    public CANEncoder encoderFollower;
    private double conversionFactor = 21.4523 ; // (One rotation in rotation values) 

    // 4096
    public EncoderGroup(CANEncoder encoderLeader, CANEncoder encoderFollower){
        this.encoderLeader = encoderLeader;
        this.encoderFollower = encoderFollower;
    }

    public void setConversionFactor(double factor){
        this.conversionFactor = factor;
    }

    public double getDistance(){
        return ((this.encoderFollower.getPosition() + this.encoderLeader.getPosition()) / this.conversionFactor)*0.478779;
    } 
    public double getVelocity(){
        return ((this.encoderLeader.getVelocity()+this.encoderFollower.getVelocity())/this.conversionFactor)*.47877;
    }
    
    public void reset(){
        this.encoderFollower.setPosition(0);
        this.encoderLeader.setPosition(0);
    }




}
