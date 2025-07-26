package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Outtake extends SubsystemBase{
      private SparkMax scoring = new SparkMax(5, MotorType.kBrushless);

      public Command intakeCommand() {
  return run(()->{
    scoring.setVoltage(5);
  });
      }


      public Command outputCommand() {
        return run(()->{
            scoring.setVoltage(-9);
        });
            }

     public Command intakestopCommand() {
     return run(()->{
        scoring.setVoltage(0);
        });
            }
}

