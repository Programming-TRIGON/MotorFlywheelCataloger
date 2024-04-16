package frc.robot.flywheel;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public class FlywheelConstants {
    static final double TEST_TIMEOUT_SECONDS = 10;
    static final SysIdRoutine.Config FLYWHEEL_SYSID_CONFIG = new SysIdRoutine.Config(
            Units.Volts.of(0.25).per(Units.Second),
            Units.Volts.of(7),
            Units.Second.of(TEST_TIMEOUT_SECONDS)
    );
    static final double VOLTAGE_TOLERANCE = 0.1;
}
