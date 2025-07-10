// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.DriveBase;
import frc.robot.subsystems.DriveBasePWM;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Scoring;


import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DriveBasePWM driveBase= new DriveBasePWM();
 // private final Elevator elevator = new Elevator();
  //private final Scoring scoring = new Scoring();


  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(0);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings

  }
 


  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  public void configureBindings() {
   //Forwards().whileTrue(driveBase.DriveForward());
   driveBase.setDefaultCommand(driveBase.DriveTank(() -> m_driverController.getLeftY(), () -> m_driverController.getRightY(), ()-> m_driverController.leftBumper().getAsBoolean(), ()-> m_driverController.rightBumper().getAsBoolean()));
   //Left().whileTrue(driveBase.TurnLeft());
   //Backwards().whileTrue(driveBase.DriveBackward());
   //Right().whileTrue(driveBase.TurnRight());
  //  m_driverController.x().whileTrue(elevator.GoToSetpoint(Elevator.SetPoint.L1));
  //  m_driverController.y().whileTrue(elevator.GoToSetpoint(Elevator.SetPoint.L2));
  //  m_driverController.b().whileTrue(elevator.GoToSetpoint(Elevator.SetPoint.L3));
  //  m_driverController.a().whileTrue(elevator.GoToSetpoint(Elevator.SetPoint.Reset));
  //  m_driverController.rightTrigger().whileTrue(scoring.intakeCommand());
  //  m_driverController.leftTrigger().whileTrue(scoring.outputCommand());
  //  scoring.setDefaultCommand(scoring.intakestopCommand());

  }



  private Trigger Forwards(){
    return new Trigger(()->{
      return m_driverController.getLeftY()<-0.1;
    });
  }

  private Trigger Backwards(){
    return new Trigger(()->{
      return m_driverController.getLeftY()>0.1;
    });
  }


  

  private Trigger Left(){
    return new Trigger(()->{
      return m_driverController.getLeftX()<-0.1;
    });
  }





  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
 public Command getAutonomousCommand() {
    // An example command will be run in autonomous
  //return driveBase.DriveForward().withTimeout(1).andThen(driveBase.Stop()).withTimeout(0.2).andThen(scoring.intakeCommand()).withTimeout(1).andThen(scoring.intakestopCommand());
    return Commands.none();
  }
}
