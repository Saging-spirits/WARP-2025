package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * Drives four roboRIO DIO lines (3..6) as a 4-bit code to the Arduino.
 * Bit order: bit0 = DIO 3 (LSB) ... bit3 = DIO 6 (MSB).
 *
 * No scheduling required — just call setCode(...) or setPattern(...).
 */
public class LedSignaller extends SubsystemBase {

  // DIO pins (change here later if you rewire)
  public static final int BIT0_DIO = 3; // LSB
  public static final int BIT1_DIO = 4;
  public static final int BIT2_DIO = 5;
  public static final int BIT3_DIO = 6; // MSB

  // Suggested codes for the patterns you’ve built on Arduino
  public enum Pattern {
    IDLE(0b0000),
    SIDE1_FWD(0b0001),
    SIDE1_REV(0b0010),
    SIDE2_FWD(0b0100),
    SIDE2_REV(0b1000),

    FORWARDS(0b0101),
    BACKWARDS(0b1010),
    REV_FWD(0b0110),
    FWD_REV(0b1001);

    public final int code;
    Pattern(int code) { this.code = code; }
  }

  private final DigitalOutput bit0 = new DigitalOutput(BIT0_DIO);
  private final DigitalOutput bit1 = new DigitalOutput(BIT1_DIO);
  private final DigitalOutput bit2 = new DigitalOutput(BIT2_DIO);
  private final DigitalOutput bit3 = new DigitalOutput(BIT3_DIO);

  private int currentCode = Pattern.IDLE.code;

  public LedSignaller() {
    // Come up in IDLE so Arduino shows your idle effect (0000)
    setPattern(Pattern.IDLE);
  }

  /** Write a 4-bit value (0–15) to the DIO lines. Safe if called often. */
  public void setCode(int code) {
    int v = code & 0xF; // clamp to 4 bits
    bit0.set(((v >> 0) & 1) == 1);
    bit1.set(((v >> 1) & 1) == 1);
    bit2.set(((v >> 2) & 1) == 1);
    bit3.set(((v >> 3) & 1) == 1);
    currentCode = v;
  }

  /** Convenience for named patterns. */
  public void setPattern(Pattern p) {
    setCode(p.code);
  }

  public int getCurrentCode() {
    return currentCode;
  }
}

