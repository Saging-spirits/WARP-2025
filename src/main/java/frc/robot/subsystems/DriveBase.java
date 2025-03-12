// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveBase extends SubsystemBase {
  private SparkMax LeftSide=new SparkMax(0,MotorType.kBrushless);
  private SparkMax RightSide=new SparkMax(0,MotorType.kBrushless);
  /** Creates a new ExampleSubsystem. */
  public DriveBase() {}

  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command DriveForward() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return run(
        () -> {
        LeftSide.setVoltage(12);
        RightSide.setVoltage(12);
        });
  }

  public Command Stop() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
        LeftSide.setVoltage(0);
        RightSide.setVoltage(0);
        });
  }

  public Command DriveBackward() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return run(
        () -> {
        LeftSide.setVoltage(-12);
        RightSide.setVoltage(-12);
        });
  }

  public Command TurnLeft() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return run(
        () -> {
        LeftSide.setVoltage(-12);
        RightSide.setVoltage(12);
        });
  }

  public Command TurnRight() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return run(
        () -> {
        LeftSide.setVoltage(12);
        RightSide.setVoltage(-12);
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
