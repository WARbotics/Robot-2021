package frc.robot.components;
import java.lang.*;
import java.util.*;
import frc.robot.components.Limelight;

public class ShootingTrajectory {

    private double yDistance;
    private double xDistance;
    private double distance;
    private double robotHeight = 1.0; //needs to change to measured height
    private double shootingAngleDegrees = 45; //needs to change to measured shooting angle in degrees
    private double gravity = 9.82;

    public void ShootingTrajectory () {

    }

    public void setRobot(double robotHeight){
        this.robotHeight = robotHeight;
    }//set robot height to the data in the parameter

    public void setXDistance(double xDistance){
        this.xDistance = xDistance;
    }//set the horizontal distance from the robot shooter to the targe

    public void setYDistance(double yDistance){
        this.yDistance = yDistance;
    }//set the vertical distance from the robot shooter to the target

    public void setShootingTheta(double theta){
        this.shootingAngleDegrees = theta;
    }//set the shooting angle in degrees

    public double initialVelocity() {
        double shootingAngleRadians = Math.toRadians(shootingAngleDegrees);//convert the shooting angle in radians
        double numerator = gravity * Math.pow(xDistance, 2.0);
        double denominator = (2 * Math.pow(Math.cos(shootingAngleRadians), 2.0) * (yDistance - robotHeight - Math.tan(shootingAngleRadians) * xDistance));
        double initVelocity = Math.sqrt( numerator/ denominator);//calculating the initial velocity using the equation
        return initVelocity;
    }
}