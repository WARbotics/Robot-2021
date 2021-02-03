package frc.robot.components;

import com.revrobotics.CANSparkMax;




/*
*In the Intake has two modes and they are Intake on ad Intake off. We also have a seprete button to reverse the intake.
* We have boolean in our code to tell the operator if the intake is on.
*/


public class Intake {

    private CANSparkMax Motor;
    private Boolean intakeRunning = false;
/*
*This states that the Intake motor is a CANSparkMax motor
*/

    public Intake(CANSparkMax Motor){
        this.intakeMotor = Motor;
    }

/*
*This Boolean tells when the Intake is on

*/
    public Boolean isIntakeRunning (){
        return intakeRunning;
    }

/*
*Intake is on and going forward 
*/
    public void on(){

        this.intakeMotor.set(.95);
        intakeRunning = true;

    }
/*
*Intake is off
*/
    public void off(){
        this.intakeMotor.set(0);
        intakeRunning  = false;
    }
/*
*Intake is going reveresed
*/
    public void reverse(){
        this.intakeMotor.set(-1);
        intakeRunning = true;
    }

    public boolean isRunning(){
        return this.intakeRunning;
    }
}


