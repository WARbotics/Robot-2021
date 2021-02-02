package frc.robot.components;

import com.revrobotics.CANSparkMax;





public class Intake {

    private CANSparkMax Motor;
    private Boolean intakeRunning = false;


    public Intake(CANSparkMax Motor){
        this.intakeMotor = Motor;
    }

    public Boolean isIntakeRunning (){
        return intakeRunning;
    }


    public void on(){

        this.intakeMotor.set(.95);
        intakeRunning = true;

    }

    public void off(){
        this.intakeMotor.set(0);
        intakeRunning  = false;
    }

    public void reverse(){
        this.intakeMotor.set(-1);
        intakeRunning = true;
    }

    public boolean isRunning(){
        return this.intakeRunning;
    }
}


