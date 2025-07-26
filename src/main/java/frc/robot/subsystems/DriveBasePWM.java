// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of

// the WPILib BSD license file in the root directory of this project.

 
package frc.robot.subsystems;

 

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkMax;

import choreo.trajectory.DifferentialSample;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.LTVUnicycleController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.OperatorConstants;



public class DriveBasePWM extends SubsystemBase {

  private SparkMax LeftSide=new SparkMax(3,MotorType.kBrushed);

  private SparkMax RightSide=new SparkMax(1,MotorType.kBrushed);
  
  @Override
  public void periodic (){
  }

  public DriveBasePWM() {}

  public Command DriveTank(DoubleSupplier LeftY, DoubleSupplier RightY, BooleanSupplier Boost, BooleanSupplier Slow) {
  return run(()->{
    double LeftPower=0;
    double RightPower=0;
  
    if (LeftY.getAsDouble()>OperatorConstants.Deadzone){
      LeftPower=OperatorConstants.MaxSpeed*LeftY.getAsDouble();
    } else if (LeftY.getAsDouble()<-OperatorConstants.Deadzone){
      LeftPower=OperatorConstants.MaxSpeed*LeftY.getAsDouble();
    }
    if (RightY.getAsDouble()>OperatorConstants.Deadzone){
      RightPower=OperatorConstants.MaxSpeed*RightY.getAsDouble();
    } else if (RightY.getAsDouble()<-OperatorConstants.Deadzone){
      RightPower=OperatorConstants.MaxSpeed*RightY.getAsDouble();
    }
    double BoostSpeed = 1;
    if (Boost.getAsBoolean()==true){
      BoostSpeed = 2;
    }
    if (Slow.getAsBoolean()==true){
      BoostSpeed = 0.5;
    }
    LeftSide.setVoltage(-LeftPower*BoostSpeed);
    RightSide.setVoltage(-RightPower*BoostSpeed);

    System.out.println("LeftSpeed"+LeftY.getAsDouble());
    System.out.println("RightSpeed"+RightY.getAsDouble());
  });
 }

  public Command Stop() {

    return runOnce(

        () -> {

        LeftSide.setVoltage(0);

        RightSide.setVoltage(0);

        });

  }

 

}