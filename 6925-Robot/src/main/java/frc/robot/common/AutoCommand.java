package frc.robot.common;

import java.io.IOException;
import java.nio.file.Path;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;

public class AutoCommand {

    Path trajectoryPath;
    Trajectory trajectory;

    public AutoCommand(Path trajectoryPath) {
        this.trajectoryPath = trajectoryPath;
        try {
            this.trajectory = TrajectoryUtil.fromPathweaverJson(this.trajectoryPath);
        } catch (IOException e) {
            DriverStation.reportError("Unable to open trajectory: " + trajectoryPath, e.getStackTrace());
        }
    }
}
