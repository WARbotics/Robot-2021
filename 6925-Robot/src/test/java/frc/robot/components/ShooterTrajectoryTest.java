package frc.robot.components;


import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import frc.robot.components.ShootingTrajectory;

public class ShooterTrajectoryTest {
    
    @Test
    public void InitialVelocityTest(){
    
        ShootingTrajectory trajectory = new ShootingTrajectory();
        trajectory.setRobot(.4);
        trajectory.setShootingTheta(45.0);
        trajectory.setXDistance(10.0);
        trajectory.setYDistance(8.0);
        double initVelocity = trajectory.initialVelocity();
        System.out.println(initVelocity);
        assertEquals(20.22, initVelocity, .1);
        /*
        Assuming 
        distance to goal = 10 m P
        theta = 45 degrees
        height of shooting goal = 8 m
        height of robot = .4 m
        */
    
    }
}
