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

  private PWM LeftSide=new PWM(9);

  private PWM RightSide=new PWM(7);

 // DutyCycleEncoder LeftEncoder=new DutyCycleEncoder(1);

 // DutyCycleEncoder RightEncoder=new DutyCycleEncoder(1);

 // ADIS16448_IMU gyro=new ADIS16448_IMU();
  
  @Override
  public void periodic (){
System.out.println();
  }
  /** Creates a new ExampleSubsystem. */

  public DriveBasePWM() {}

 

  /**

   * Example command factory method.

   *

   * @return a command

   */
  
//       private final LTVUnicycleController controller = new LTVUnicycleController(0.02);
//     public void followTrajectory(DifferentialSample sample) {
//         // Get the current pose of the robot
//         Pose2d pose = Pose2d.kZero;

//         // Get the velocity feedforward specified by the sample
//         ChassisSpeeds ff = sample.getChassisSpeeds();

//         // Generate the next speeds for the robot
//         ChassisSpeeds speeds = controller.calculate(
//             pose,
//             sample.getPose(),
//             ff.vxMetersPerSecond,
//             ff.omegaRadiansPerSecond
//         );

//         // Apply the generated speeds
//         drive(speeds);

//         // Or, if you don't drive via ChassisSpeeds
//         DifferentialDriveWheelSpeeds wheelSpeeds = kinematics.toWheelSpeeds(speeds); // 
//         drive(wheelSpeeds.leftMetersPerSecond, wheelSpeeds.rightMetersPerSecond);
//     }

// public void drive(double LeftWheelSpeed, double RightWheelSpeed){

// }
public Command DriveForward(){
  return run(()->{
  LeftSide.setSpeed(-0.5);
    RightSide.setSpeed(0.5);
  });
}

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
      RightPower=-OperatorConstants.MaxSpeed*RightY.getAsDouble();
    } else if (RightY.getAsDouble()<-OperatorConstants.Deadzone){
      RightPower=-OperatorConstants.MaxSpeed*RightY.getAsDouble();
    }
    double BoostSpeed = 1;
    if (Boost.getAsBoolean()==true){
      BoostSpeed = 2;
    }
    if (Slow.getAsBoolean()==true){
      BoostSpeed = 0.5;
    }
    LeftSide.setSpeed(LeftPower*BoostSpeed);
    RightSide.setSpeed(RightPower*BoostSpeed);

    System.out.println("LeftSpeed"+LeftY.getAsDouble());
    System.out.println("RightSpeed"+RightY.getAsDouble());
  });
 }

  public Command DriveGeneral(DoubleSupplier Xjoy, DoubleSupplier Yjoy) {

    return run(()->{

if (Xjoy.getAsDouble()*Xjoy.getAsDouble()+Yjoy.getAsDouble()*Yjoy.getAsDouble()<0.2) {

//Middle circle of 0 volts for stick drift correction

  LeftSide.setSpeed(0);

  RightSide.setSpeed(0);

} else {

  if (Yjoy.getAsDouble()==0) {

//When Y value is 0 (to avoid divide by 0 errors)

    LeftSide.setSpeed(8*Xjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()));

    RightSide.setSpeed(-8*Xjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()));

  } else {

    if (Xjoy.getAsDouble()==0) {

//When X value is 0 (to avoid divide by 0 errors)

      LeftSide.setSpeed(-8*Yjoy.getAsDouble()/Math.abs(Yjoy.getAsDouble()));

      RightSide.setSpeed(-8*Yjoy.getAsDouble()/Math.abs(Yjoy.getAsDouble()));

    } else {

      if (Xjoy.getAsDouble()*Yjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()*Yjoy.getAsDouble())==1) {

// Quaderants top left and bottom right. (left motor changes voltage)

        LeftSide.setSpeed(-8*Xjoy.getAsDouble()*Math.cos(2*Math.atan(Xjoy.getAsDouble()/Yjoy.getAsDouble()))/Math.abs(Xjoy.getAsDouble()));

        RightSide.setSpeed(-8*Xjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()));

      } else {

// Quaderants top right and bottom left. (right motor changes voltage)

        LeftSide.setSpeed(8*Xjoy.getAsDouble()/Math.abs(Xjoy.getAsDouble()));

        RightSide.setSpeed(8*Xjoy.getAsDouble()*Math.cos(2*Math.atan(Xjoy.getAsDouble()/Yjoy.getAsDouble()))/Math.abs(Xjoy.getAsDouble()));

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

        LeftSide.setSpeed(0);

        RightSide.setSpeed(0);

        });

  }

 

  public Command DriveBackward() {

    // Inline construction of command goes here.

    // Subsystem::RunOnce implicitly requires `this` subsystem.

    return run(

        () -> {

        LeftSide.setSpeed(-12);

        RightSide.setSpeed(-12);

        });

  }

 

  public Command TurnLeft() {

    // Inline construction of command goes here.

    // Subsystem::RunOnce implicitly requires `this` subsystem.

    return run(

        () -> {

        LeftSide.setSpeed(-12);

        RightSide.setSpeed(12);

        });

  }

 

  public Command TurnRight() {

    // Inline construction of command goes here.

    // Subsystem::RunOnce implicitly requires `this` subsystem.

    return run(

        () -> {

        LeftSide.setSpeed(12);

        RightSide.setSpeed(-12);

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