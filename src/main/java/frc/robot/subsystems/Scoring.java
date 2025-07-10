package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Scoring extends SubsystemBase{
      private PWM scoringPwm=new PWM(8);

      public Command intakeCommand() {
  return run(()->{
    scoringPwm.setSpeed(0.45);
  });
      }


      public Command outputCommand() {
        return run(()->{
          scoringPwm.setSpeed(-0.75);
        });
            }

     public Command intakestopCommand() {
     return run(()->{
          scoringPwm.setSpeed(0);
        });
            }
}

