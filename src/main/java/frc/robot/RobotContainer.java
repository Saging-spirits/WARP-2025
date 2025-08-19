// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.DriveBasePWM;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Feed;

import java.util.Set;

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
//  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

private final DriveBasePWM mTankdrive = new DriveBasePWM();
private final Elevator mElevator = new Elevator();
private final Arm mArm = new Arm();
private final Feed mFeed = new Feed();




  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final Controller m_driverController =
      new Controller(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();
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
  private void configureBindings() {
    mTankdrive.setDefaultCommand(mTankdrive.DriveTank(() -> m_driverController.getLeftY(), () -> m_driverController.getRightY(), ()-> m_driverController.leftBumper().getAsBoolean(), ()-> m_driverController.rightBumper().getAsBoolean()));    m_driverController.y().whileTrue(           //POS 1 (Y)
      Commands.sequence(
        mArm.GotoPosSlow(-13).until(mArm.atSetpoint2()), 
        Commands.parallel(
          mArm.GotoPosSlow(-13), 
          mElevator.GotoPosSlow(0)
          )
      ));

      m_driverController.b().whileTrue(         //POS2 B
        Commands.sequence(
          mElevator.GotoPosSlow(-25).until(mElevator.atSetpoint()),
          Commands.parallel(
            mElevator.GotoPosSlow(-25),
            mArm.GotoPos(-40)
          )
        ));

        m_driverController.a().whileTrue(         //POS3 A
          Commands.sequence(
            mElevator.GotoPosSlow(-50).until(mElevator.atSetpoint()),
            Commands.parallel(
              mElevator.GotoPosSlow(-50),
              mArm.GotoPos(-40)
            )
        ));

        m_driverController.x().whileTrue(       //POS 4 X
          Commands.sequence(
            mElevator.GotoPosSlow(-70).until(mElevator.atSetpoint()),
            Commands.parallel(
              mElevator.GotoPosSlow(-70),
              mArm.GotoPos(20)
            )
        ));

  
  

      m_driverController.leftTrigger().whileTrue(mFeed.intake(() -> m_driverController.getLeftTrigger()));
      m_driverController.rightTrigger().whileTrue(mFeed.outtake(() -> m_driverController.getRightTrigger()));

    mFeed.setDefaultCommand(mFeed.stop());
    mElevator.setDefaultCommand(mElevator.Stop());
    mArm.setDefaultCommand(mArm.Stop());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  //public Command getAutonomousCommand() {
    // An example command will be run in autonomous
  //  return Autos.exampleAuto(m_exampleSubsystem);
  //}
}
