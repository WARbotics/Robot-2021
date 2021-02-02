package frc.robot.components;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.revrobotics.CANSparkMax;

import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import frc.robot.components.Drivetrain;

public class DrivetrainTest{



    @Test
    public void curveSpeedTest() {
       // Arrange

        Drivetrain drive = mock(Drivetrain.class);
        drive.curveDrive(0,0,false);
        assertEquals(drive.getSpeed(), 0, .001); 
        assertEquals(drive.getRotation(), 0, .001);


    }

}