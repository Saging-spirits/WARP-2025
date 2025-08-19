// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.Feed;
import frc.robot.subsystems.Tankdrive;

import frc.robot.subsystems.LedSignaller;
import frc.robot.subsystems.LedSignaller.Pattern;

import java.util.Set;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode;
import edu.wpi.first.util.PixelFormat;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.Command.InterruptionBehavior;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
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

private final Tankdrive mTankdrive = new Tankdrive();
private final Elevator mElevator = new Elevator();
// private final Arm mArm = new Arm();
private final Feed mFeed = new Feed();
public boolean intaking = false;

private final LedSignaller mLed = new LedSignaller();  // <-- ADDED BY MR H

UsbCamera camera;

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
  private final CommandXboxController m_codriverController =
      new CommandXboxController(OperatorConstants.kCoDriverControllerPort);


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    mTankdrive.setLed(mLed);
    
    configureBindings();

    camera = CameraServer.startAutomaticCapture();
    camera.setVideoMode(PixelFormat.kYUYV, 160, 120, 30);
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
    mTankdrive.setDefaultCommand(mTankdrive.Drive(()->m_driverController.getLeftY(), ()->m_driverController.getRightY(), ()-> m_driverController.leftBumper().getAsBoolean(), ()-> m_driverController.rightBumper().getAsBoolean()));

    RobotModeTriggers.teleop().onTrue(mElevator.GotoPosSlow(10, false).withInterruptBehavior(InterruptionBehavior.kCancelIncoming).until(mElevator.atSetpoint()));

    m_codriverController.rightTrigger().whileTrue(mFeed.outtakeFront(() -> m_codriverController.getRightTriggerAxis()));
    m_codriverController.leftTrigger().whileTrue(mFeed.outtakeBack(() -> m_codriverController.getLeftTriggerAxis()));

    m_codriverController.a().whileTrue(mElevator.GotoPos(7, false));
    m_codriverController.y().whileTrue(mElevator.GotoPos(35.5, false));
    m_codriverController.b().whileTrue(mElevator.GotoPos(23, false));
    m_codriverController.x().whileTrue(mElevator.GotoPos(50, false));


    mFeed.back.onTrue(mFeed.intakeBoth().until(mFeed.front).andThen(mFeed.intakeBoth().until(mFeed.front.negate())));

    mFeed.setDefaultCommand(mFeed.intakeBack(() -> 0.5));

    mElevator.setDefaultCommand(mElevator.GotoPosSlow(5.3, false));
    
    // mArm.setDefaultCommand(mArm.Stop());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return mElevator.GotoPosSlow(10, false)
    .until(mElevator.atSetpoint())
    .andThen(mElevator.GotoPosSlow(5.3, false).withTimeout(0.5))
    .andThen(mTankdrive.Drive(() -> -0.5, () -> -0.5, () -> false, () -> false).withTimeout(2 /* TIME TO DRIVE FORWARD */))
    .andThen(
        mTankdrive.Drive(() -> 0, () -> 0, () -> false, () -> false)
          .alongWith(mElevator.GotoPosSlow(24.5 /* ELEVATOR SETPOINT */, false))
          .until(mElevator.atSetpoint())
    )
    .andThen(
        Commands.parallel(
            mElevator.GotoPosSlow(24.5 /* ELEVATOR SETPOINT (same as above) */, false),
            mFeed.outtakeFront(() -> 0.5)
        ).withTimeout(2)
    );
   }
}
