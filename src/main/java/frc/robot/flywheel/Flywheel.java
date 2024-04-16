package frc.robot.flywheel;

import edu.wpi.first.units.Measure;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.Voltage;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog;
import edu.wpi.first.wpilibj2.command.*;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;

public abstract class Flywheel extends SubsystemBase {
    private final SysIdRoutine sysIdRoutine;
    private double sumAmpAt12V = 0;
    private double sumVelocityAt12V = 0;
    private int samplesTaken = 0;

    public Flywheel() {
        setName("Flywheel");
        sysIdRoutine = createSysIdRoutine();
        setDefaultCommand(new StartEndCommand(
                () -> setVoltage(0),
                () -> {
                },
                this
        ));
    }

    public final Command getQuasistaticCharacterizationCommand(SysIdRoutine.Direction direction) {
        return sysIdRoutine.quasistatic(direction);
    }

    public final Command getDynamicCharacterizationCommand(SysIdRoutine.Direction direction) {
        return sysIdRoutine.dynamic(direction);
    }

    public Command getCalculateValuesAt12VCommand() {
        return new SequentialCommandGroup(
                new InstantCommand(() -> {
                    setVoltage(12);
                    sumAmpAt12V = 0;
                    sumVelocityAt12V = 0;
                    samplesTaken = 0;
                }),
                new WaitCommand(3),
                new RunCommand(() -> {
                    if (Math.abs(12.0 - getVoltage()) < FlywheelConstants.VOLTAGE_TOLERANCE) {
                        samplesTaken++;
                        sumAmpAt12V += getAmperage();
                        sumVelocityAt12V += getVelocity();
                    }
                })
        ).withTimeout(FlywheelConstants.TEST_TIMEOUT_SECONDS).finallyDo(() -> {
            SmartDashboard.putNumber("Flywheel/AmperageAt12V", sumAmpAt12V / samplesTaken);
            SmartDashboard.putNumber("Flywheel/VelocityAt12V", sumVelocityAt12V / samplesTaken);
        });
    }

    private SysIdRoutine createSysIdRoutine() {
        return new SysIdRoutine(
                FlywheelConstants.FLYWHEEL_SYSID_CONFIG,
                new SysIdRoutine.Mechanism(
                        this::drive,
                        this::uploadLog,
                        this,
                        "Flywheel"
                )
        );
    }

    private void drive(Measure<Voltage> voltage) {
        setVoltage(voltage.in(Units.Volts));
    }

    private void uploadLog(SysIdRoutineLog log) {
        log.motor("Flywheel")
                .angularPosition(Units.Rotations.of(getPosition()))
                .angularVelocity(Units.RotationsPerSecond.of(getVelocity()))
                .voltage(Units.Volts.of(getVoltage()));
    }

    protected abstract void setVoltage(double voltage);

    protected abstract double getPosition();

    protected abstract double getVelocity();

    protected abstract double getVoltage();

    protected abstract double getAmperage();
}
