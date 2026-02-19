package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import frc.robot.Constants.MotorConstants;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase
{
    SparkMax m_lowerMotor;
    SparkMax m_upperMotor;

    double motorSpeed = 0.5;

    @SuppressWarnings("removal")
    public Intake()
    {
        m_lowerMotor = new SparkMax(IntakeConstants.kIntakeLowerMotorID, MotorType.kBrushless);
        m_lowerMotor.configure(MotorConstants.coastMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        m_upperMotor = new SparkMax(IntakeConstants.kIntakeUpperMotorID, MotorType.kBrushless);
        m_upperMotor.configure(MotorConstants.coastMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public Command intake()
    {
        return Commands.runOnce(() -> {
            setMotors(motorSpeed);
        });
    }

    public Command outtake()
    {
        return Commands.runOnce(() -> {
            setMotors(-motorSpeed);
        });
    }

    public Command stop()
    {
        return Commands.runOnce(() -> {
            setMotors(0);
        });
    }

    void setMotors(double speed)
    {
        m_lowerMotor.set(speed);
        m_upperMotor.set(-speed);
    }
}
