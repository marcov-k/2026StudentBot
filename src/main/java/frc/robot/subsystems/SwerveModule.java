package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import frc.robot.Constants.ModuleConstants;

@SuppressWarnings("removal")
public class SwerveModule
{
    private final SparkFlex drivingSparkMax;
    private final SparkMax turningSparkMax;

    private final RelativeEncoder drivingEncoder;
    private final AbsoluteEncoder turningEncoder;

    private final SparkClosedLoopController drivingPIDController;
    private final SparkClosedLoopController turningPIDController;

    private double chassisAngularOffset = 0;
    private SwerveModuleState desiredState = new SwerveModuleState(0.0, new Rotation2d());

    public SwerveModule(int drivingCANId, int turningCANId, double thisChassisAngularOffset)
    {
        drivingSparkMax = new SparkFlex(drivingCANId, MotorType.kBrushless);
        turningSparkMax = new SparkMax(turningCANId, MotorType.kBrushless);

        drivingEncoder = drivingSparkMax.getEncoder();
        turningEncoder = turningSparkMax.getAbsoluteEncoder();
        drivingPIDController = drivingSparkMax.getClosedLoopController();
        turningPIDController = turningSparkMax.getClosedLoopController();

        drivingSparkMax.configure(ModuleConstants.drivingConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        turningSparkMax.configure(ModuleConstants.turningConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        chassisAngularOffset = thisChassisAngularOffset;
        desiredState.angle = new Rotation2d(turningEncoder.getPosition());
        drivingEncoder.setPosition(0);
    }

    public SwerveModuleState getState()
    {
        return new SwerveModuleState(drivingEncoder.getVelocity(), new Rotation2d(turningEncoder.getPosition() - chassisAngularOffset));
    }

    public SwerveModulePosition getPosition()
    {
        return new SwerveModulePosition(drivingEncoder.getPosition(), new Rotation2d(turningEncoder.getPosition() - chassisAngularOffset));
    }

    public void setDesiredState(SwerveModuleState thisDesiredState)
    {
        SwerveModuleState correctedDesiredState = new SwerveModuleState();
        correctedDesiredState.speedMetersPerSecond = desiredState.speedMetersPerSecond;
        correctedDesiredState.angle = desiredState.angle.plus(Rotation2d.fromRadians(chassisAngularOffset));
        correctedDesiredState.optimize(new Rotation2d(turningEncoder.getPosition()));
        drivingPIDController.setReference(correctedDesiredState.speedMetersPerSecond, ControlType.kVelocity);
        turningPIDController.setReference(correctedDesiredState.angle.getRadians(), ControlType.kPosition);
        desiredState = thisDesiredState;
    }

    public void resetEncoders()
    {
        drivingEncoder.setPosition(0);
    }
}