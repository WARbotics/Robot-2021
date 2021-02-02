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



    public void setRobot(double robotHeight){
        this.robotHeight = robotHeight;
    }
    public void setXDistance(double xDistance){
        this.xDistance = xDistance;
    }
    public void setYDistance(double yDistance){
        this.yDistance = yDistance;
    }

    public void setShootingTheta(double theta){
        this.shootingAngleDegrees = theta;
    }

    public double initialVelocity() {
        double shootingAngleRadians = Math.toRadians(shootingAngleDegrees);
        double numerator = -1* gravity * Math.pow(xDistance, 2.0);
        double denominator = (2 * Math.pow(Math.cos(shootingAngleRadians), 2.0) * (yDistance - robotHeight - Math.tan(shootingAngleRadians) * xDistance));
        double initVelocity = Math.sqrt( numerator/ denominator);
        return initVelocity;
    }
}