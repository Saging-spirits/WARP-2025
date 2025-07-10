// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of

// the WPILib BSD license file in the root directory of this project.

 
package frc.robot.subsystems;

 

import java.util.function.DoubleSupplier;

 

import com.revrobotics.spark.SparkMax;

import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.ADIS16448_IMU;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import edu.wpi.first.wpilibj2.command.Command;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.OperatorConstants;



public class DriveBase extends SubsystemBase {

  private SparkMax LeftSide=new SparkMax(2,MotorType.kBrushed);

  private SparkMax RightSide=new SparkMax(3,MotorType.kBrushed);

  DutyCycleEncoder LeftEncoder=new DutyCycleEncoder(1);

  DutyCycleEncoder RightEncoder=new DutyCycleEncoder(1);

  ADIS16448_IMU gyro=new ADIS16448_IMU();
  
  @Override
  public void periodic (){
System.out.println();
  }
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

 public Command DriveTank(DoubleSupplier LeftY, DoubleSupplier RightY) {
  return run(()->{
    double LeftPower=0;
    double RightPower=0;
  
    if (LeftY.getAsDouble()>OperatorConstants.Deadzone){
      LeftPower=OperatorConstants.MaxSpeed*LeftY.getAsDouble();
    } else if (LeftY.getAsDouble()<-OperatorConstants.Deadzone){
      LeftPower=OperatorConstants.MaxSpeed*LeftY.getAsDouble();
    }
    if (RightY.getAsDouble()>OperatorConstants.Deadzone){
      RightPower=-OperatorConstants.MaxSpeed*RightY.getAsDouble();
    } else if (RightY.getAsDouble()<-OperatorConstants.Deadzone){
      RightPower=-OperatorConstants.MaxSpeed*RightY.getAsDouble();
    }
    LeftPower=Math.pow(LeftPower, 3);
    RightPower=Math.pow(RightPower, 3);
    LeftSide.setVoltage(LeftPower);
    RightSide.setVoltage(RightPower);

    System.out.println("LeftSpeed"+LeftY.getAsDouble());
    System.out.println("RightSpeed"+RightY.getAsDouble());
  });
 }

  public Command DriveGeneral(DoubleSupplier Xjoy, DoubleSupplier Yjoy) {

    return run(()->{

if (Xjoy.getAsDouble()*Xjoy.getAsDouble()+Yjoy.getAsDouble()*Yjoy.getAsDouble()<0.2) {

//Middle circle of 0 volts for stick drift correction

  LeftSide.setVoltage(0);

  RightSide.setVoltage(0);

} else {

  if (Yjoy.getAsDouble()==0) {

//When Y value is 0 (to avoid divide by 0 errors)

    LeftSide.setVoltage(8*Xjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()));

    RightSide.setVoltage(-8*Xjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()));

  } else {

    if (Xjoy.getAsDouble()==0) {

//When X value is 0 (to avoid divide by 0 errors)

      LeftSide.setVoltage(-8*Yjoy.getAsDouble()/Math.abs(Yjoy.getAsDouble()));

      RightSide.setVoltage(-8*Yjoy.getAsDouble()/Math.abs(Yjoy.getAsDouble()));

    } else {

      if (Xjoy.getAsDouble()*Yjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()*Yjoy.getAsDouble())==1) {

// Quaderants top left and bottom right. (left motor changes voltage)

        LeftSide.setVoltage(-8*Xjoy.getAsDouble()*Math.cos(2*Math.atan(Xjoy.getAsDouble()/Yjoy.getAsDouble()))/Math.abs(Xjoy.getAsDouble()));

        RightSide.setVoltage(-8*Xjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()));

      } else {

// Quaderants top right and bottom left. (right motor changes voltage)

        LeftSide.setVoltage(8*Xjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()));

        RightSide.setVoltage(8*Xjoy.getAsDouble()*Math.cos(2*Math.atan(Xjoy.getAsDouble()/Yjoy.getAsDouble()))/Math.abs(Xjoy.getAsDouble()));

}

}

}

}

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

  public void simulationPeriodic() {

    // This method will be called once per scheduler run during simulation

  }

}


//New code built from here