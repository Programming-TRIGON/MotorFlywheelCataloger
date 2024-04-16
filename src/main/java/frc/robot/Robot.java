// Copyright (c) FIRST and other WPILib contributors.

// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.flywheel.Flywheel;
import frc.robot.flywheel.TalonFXFlywheel;

public class Robot extends TimedRobot {
    @Override
    public void robotInit() {
        final Flywheel flywheel = new TalonFXFlywheel(1);
        final CommandXboxController controller = new CommandXboxController(0);

        controller.b().onTrue(flywheel.getDynamicCharacterizationCommand(SysIdRoutine.Direction.kForward));
        controller.x().onTrue(flywheel.getDynamicCharacterizationCommand(SysIdRoutine.Direction.kReverse));
        controller.y().onTrue(flywheel.getQuasistaticCharacterizationCommand(SysIdRoutine.Direction.kForward));
        controller.a().onTrue(flywheel.getQuasistaticCharacterizationCommand(SysIdRoutine.Direction.kReverse));
        controller.rightBumper().onTrue(flywheel.getCalculateValuesAt12VCommand());
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }
}
