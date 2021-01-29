package frc.robot.components;

import com.revrobotics.CANSparkMax;



public class Intake {
    CANSparkMax Intake Motor;
    private boolean intakeRunning = false;

    public Intake(spark Motor){
        this.IntakeMotor=Motor;
    }


    public void on(){
        this.IntakeMotor.setPower(.95);
        intakeRunning true;

    }
}
    public void off(){
        this.IntakeMotor.setPower(0);
        intakeRunning false;
    }
}
    public void reverse(){
        this.IntakeMotor.setPower(-1);
        intakeRunning true;
    }
}


