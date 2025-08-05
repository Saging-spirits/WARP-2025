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

import java.util.Set;

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




  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

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
    mTankdrive.setDefaultCommand(mTankdrive.Drive(()->m_driverController.getLeftY(), ()->m_driverController.getRightY()));
    // m_driverController.y().whileTrue(           //POS 1 (Y)
    //   Commands.sequence(
    //     mArm.GotoPosSlow(-13).until(mArm.atSetpoint2()), 
    //     Commands.parallel(
    //       mArm.GotoPosSlow(-13), 
    //       mElevator.GotoPosSlow(0)
    //       )
    //   ));

    //   m_driverController.b().whileTrue(         //POS2 B
    //     Commands.sequence(
    //       mElevator.GotoPosSlow(-22).until(mElevator.atSetpoint()),
    //       Commands.parallel(
    //         mElevator.GotoPosSlow(-22),
    //         mArm.GotoPos(-42)
    //       )
    //     ));

    //     m_driverController.a().whileTrue(         //POS3 A
    //       Commands.sequence(
    //         mElevator.GotoPosSlow(-47).until(mElevator.atSetpoint()),
    //         Commands.parallel(
    //           mElevator.GotoPosSlow(-47),
    //           mArm.GotoPos(-41)
    //         )
    //     ));

    //     m_driverController.x().whileTrue(       //POS 4 X
    //       Commands.sequence(
    //         mElevator.GotoPosSlow(-95).until(mElevator.atSetpoint()),
    //         Commands.parallel(
    //           mElevator.GotoPosSlow(-95),
    //           mArm.GotoPos(20)
    //         )
    //     ));
    RobotModeTriggers.teleop().and(() -> !intaking).whileTrue(mFeed.intakeBack(() -> 0.5).withInterruptBehavior(InterruptionBehavior.kCancelIncoming).until(mFeed.back).andThen(Commands.run(() -> intaking = true)));
    mFeed.back.and(() -> intaking).whileTrue(mFeed.stopBack().withInterruptBehavior(InterruptionBehavior.kCancelIncoming).until(mElevator.atSetpoint().and(() -> mElevator.goingForTransfer)).unless(mElevator.atSetpoint().and(() -> mElevator.goingForTransfer)));
    mFeed.back.and(() -> intaking).whileTrue(mElevator.GotoPos(6.3, true).until(mElevator.atSetpoint()));

    mElevator.atSetpoint().and(() -> intaking).and(() -> mElevator.goingForTransfer)
      .whileTrue(Commands.parallel(mFeed.intakeBoth(), mElevator.GotoPos(6.3, true)).withInterruptBehavior(InterruptionBehavior.kCancelIncoming).until(mFeed.front));

    mFeed.front.and(() -> intaking).whileTrue(mFeed.intakeFront(() -> 0.5).alongWith(mElevator.GotoPos(6.3, true)).withInterruptBehavior(InterruptionBehavior.kCancelIncoming).until(mFeed.front.negate()).unless(mFeed.front.negate()).andThen(mFeed.stopFront()).until(() -> !mElevator.goingForTransfer));

    m_driverController.rightTrigger().whileTrue(Commands.runOnce(() -> {
      mElevator.goingForTransfer = false;
      intaking = false;
    }).andThen(
mFeed.outtakeFront(() -> m_driverController.getRightTriggerAxis())));

    m_driverController.y().whileTrue(mElevator.GotoPos(35, false));
    m_driverController.b().whileTrue(mElevator.GotoPos(7, false));

    mFeed.setDefaultCommand(mFeed.stop());
    mElevator.setDefaultCommand(mElevator.Stop());
    
    // mArm.setDefaultCommand(mArm.Stop());
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
