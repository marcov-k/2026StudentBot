package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase
{
    SparkMax m_lowerMotor;
    SparkMax m_upperMotor;

    double motorSpeed = 0.5;

    public Intake()
    {
        m_lowerMotor = new SparkMax(IntakeConstants.kIntakeLowerMotorID, MotorType.kBrushless);
        m_upperMotor = new SparkMax(IntakeConstants.kIntakeUpperMotorID, MotorType.kBrushless);
    }

    public Command intake()
    {
        return Commands.runOnce(() -> {
            setMotors(motorSpeed);
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
        m_lowerMotor.set(-speed);
        m_upperMotor.set(speed);
    }
}
