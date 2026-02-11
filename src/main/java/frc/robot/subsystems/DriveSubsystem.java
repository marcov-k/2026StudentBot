package frc.robot.subsystems;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

import com.studica.frc.AHRS;

public class DriveSubsystem extends SubsystemBase
{
    final double kSpeedLimit = 0.2;

    final SwerveModule frontLeft;
    final SwerveModule frontRight;
    final SwerveModule rearLeft;
    final SwerveModule rearRight;

    // Declare NavX AHRS Gyroscope
    private final AHRS gyro;

    public DriveSubsystem()
    {
        // Initialize 4 instances of SwerveModules
        frontLeft = new SwerveModule(
            DriveConstants.kFlDriveId,
            DriveConstants.kFlTurnId,
            DriveConstants.kFlOffset);

        frontRight = new SwerveModule(
            DriveConstants.kFrDriveId,
            DriveConstants.kFrTurnId,
            DriveConstants.kFrOffset);

        rearLeft = new SwerveModule(
            DriveConstants.kRlDriveId,
            DriveConstants.kRlTurnId,
            DriveConstants.kRlOffset);

        rearRight = new SwerveModule(
            DriveConstants.kRrDriveId,
            DriveConstants.kRrTurnId,
            DriveConstants.kRrOffset);

        // Initialize NavX AHRS Gyroscope
        gyro = new AHRS(AHRS.NavXComType.kMXP_SPI);
    }

    // Drive Method
    public void drive(double forward, double strafe, double rotation, boolean fieldRelative)
    {
        // Convert the commanded speeds into the correct units for the drivetrain, and convert controller left and forward into positive numbers as expected for swerve
        forward = -forward * DriveConstants.kMaxSpeedMPS;
        strafe = -strafe * DriveConstants.kMaxSpeedMPS;
        rotation = -rotation * DriveConstants.kMaxAngularSpeed;

        // Grab the current angle from the Gyroscope and invert it because ChassisSpeeds expects counter clockwise positive. 
        double currentangle = gyro.getYaw() * -1.0;

        // Calculate Swerve Module States
        var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
            fieldRelative
                ? ChassisSpeeds.fromFieldRelativeSpeeds(forward, strafe, rotation, Rotation2d.fromDegrees(currentangle))
                : new ChassisSpeeds(forward, strafe, rotation)
        );

        // Desaturate Swerve Module States 
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, DriveConstants.kMaxSpeedMPS);

        // Set Swerve Module States
        frontLeft.setDesiredState(swerveModuleStates[0]);
        frontRight.setDesiredState(swerveModuleStates[1]);
        rearLeft.setDesiredState(swerveModuleStates[2]);
        rearRight.setDesiredState(swerveModuleStates[3]);

    }

    // Drive Command
    public Command driveCommand(CommandXboxController controller, boolean fieldRelative)
    {
        return Commands.run(
            () -> {
                double forward = MathUtil.applyDeadband(controller.getLeftY() * kSpeedLimit, 0.02);
                double strafe = MathUtil.applyDeadband(controller.getLeftX() * kSpeedLimit, 0.02);
                double rotate = MathUtil.applyDeadband(controller.getRightX() * kSpeedLimit, 0.02);
                this.drive(forward, strafe, rotate, fieldRelative);
            }
        , this);
    } 
}
