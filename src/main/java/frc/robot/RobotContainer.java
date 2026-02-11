// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer
{
    // The robot's subsystems and commands are defined here...
    DriveSubsystem m_drive = new DriveSubsystem();
    Launcher m_launcher = new Launcher();
    Intake m_intake = new Intake();

    // Replace with CommandPS4Controller or CommandJoystick if needed
    private final CommandXboxController controller =
        new CommandXboxController(OperatorConstants.kDriverControllerPort);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer()
    {
        // Configure the trigger bindings
        configureBindings();
        CommandScheduler.getInstance().setDefaultCommand(m_drive, m_drive.driveCommand(controller, true));
    }

    void configureBindings()
    {
        controller.rightTrigger().onTrue(m_launcher.launch()).onFalse(m_launcher.stop());
        controller.b().onTrue(m_intake.intake()).onFalse(m_intake.stop());
    }
}
