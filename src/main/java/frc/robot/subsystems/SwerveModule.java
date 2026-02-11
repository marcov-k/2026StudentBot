package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

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
    
    public static final class ModuleConstants
    {
        public static final int kDrivingMotorPinionTeeth = 14;
        public static final double kDrivingMotorFreeSpeedRps = 94.6;
        public static final double kWheelDiameterMeters = 0.0762;
        public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
        public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
        public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters) / kDrivingMotorReduction;
        public static final SparkFlexConfig drivingConfig = new SparkFlexConfig();
        public static final SparkMaxConfig turningConfig = new SparkMaxConfig();
        static {
            double drivingFactor = kWheelCircumferenceMeters / kDrivingMotorReduction;
            double turningFactor = 2 * Math.PI;
            double drivingVelocityFeedForward = 1 / kDriveWheelFreeSpeedRps;

            drivingConfig.idleMode(IdleMode.kCoast).smartCurrentLimit(50);
            drivingConfig.encoder.positionConversionFactor(drivingFactor).velocityConversionFactor(drivingFactor / 60.0);
            drivingConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder).pid(0.04,0,0).velocityFF(drivingVelocityFeedForward).outputRange(-1,1);

            turningConfig.idleMode(IdleMode.kBrake).smartCurrentLimit(20);
            turningConfig.absoluteEncoder.inverted(true).positionConversionFactor(turningFactor).velocityConversionFactor(turningFactor / 60.);
            turningConfig.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder).pid(1,0,0).outputRange(-1, 1).positionWrappingEnabled(true).positionWrappingInputRange(0, turningFactor);
        }
    }

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