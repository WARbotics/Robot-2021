package frc.robot.components;
import java.lang.*;
import java.util.*;
import frc.robot.components.Limelight;

public class ShootingTrajectory {

    private double yDistance;
    private double xDistance;
    private double robotHeight = 1.0; //needs to change to measured height
    private double shootingAngleDegrees = 45; //needs to change to measured shooting angle in degrees
    private double gravity = 9.82;

    Limelight limeLight;

    public ShootingTrajectory() {
        
        yDistance = limeLight.getY();
        xDistance = limeLight.getX();
    }

    public double InitialVelocity() {
        double initVelocity;
        double shootingAngleRadians = Math.toRadians(shootingAngleDegrees);
        if (!limeLight.hasValidTarget()) {
            return 0.0;
        }
        double numerator = gravity * Math.pow(xDistance, 2.0);
        double denominator = (2 * Math.pow(Math.cos(shootingAngleRadians), 2.0) * (yDistance - robotHeight - Math.tan(shootingAngleRadians) * xDistance));
        initVelocity = Math.sqrt( numerator/ denominator);
        return initVelocity;
    }
}