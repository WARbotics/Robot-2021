package frc.robot.common;

import edu.wpi.first.wpilibj.Spark;

public class LED {
    
    private Spark ledDriver; 
    private LEDMode mode = LEDMode.IDLE;

    public LED(Spark ledDriver){
        this.ledDriver = ledDriver;
    }

    public LEDMode getLMode(){
        return mode;
    }

    public enum LEDMode {
        IDLE(-0.93), 
        SHOOTING(-0.85), 
        STARTUP(-0.81);

        private double motorValue;
        private LEDMode(double motorValue){
            this.motorValue = motorValue;
        }
        public double getMotorValue(){
            return this.motorValue;
        }
    }

    public void setLEDMode(LEDMode mode){
        this.mode = mode;
        ledDriver.set(this.mode.getMotorValue());
    }

}
