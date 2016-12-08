package ru.samwanderman.island.common.ai;

import ru.samwanderman.island.common.animation.ChangePositionAnimation;
import ru.samwanderman.island.common.core.object.UnitTile;
import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.island.common.view.tile.GuiObjectTile;
import ru.samwanderman.island.common.view.tile.GuiUnitTile;
import ru.samwanderman.wheel.ai.IAI;
import ru.samwanderman.wheel.animation.IAnimation;
import ru.samwanderman.wheel.view.figure.Point2D;

public final class UnitAI implements IAI {
	private final GuiUnitTile unit;
	
	public UnitAI(final GuiUnitTile unit) {
		this.unit = unit;
	}
	
	@Override
	public final void sync() {
		final IAnimation anim = unit.getCurrentAnimation();
		final boolean isMoving = ((anim != null) && (anim.getName().equals(ChangePositionAnimation.NAME)) && anim.isRunning());
		final boolean isAttacking = ((anim != null) && (anim.getName().equals("attack")) && anim.isRunning());
		final GuiObjectTile tile = checkAttack(); 
		final boolean canAttack = (tile != null) && (unit.getHealth() > 0);
		
		if (canAttack && !isMoving && !isAttacking) {
			unit.playAnimation("attack");
			tile.addHealth(-((UnitTile) unit.getTile()).getAttack());
		} else if (isMoving) {
			return;
		} else if (isAttacking && canAttack) {
			tile.addHealth(-((UnitTile) unit.getTile()).getAttack());
			return;
		} else {
			unit.stopCurrentAnimation();
		}
	}
	
	/**
	 * Check if unit can attack
	 * 
	 * @return
	 */
	private final GuiObjectTile checkAttack() {
		final Point2D point = unit.getPoint();
		final GuiLevel level = unit.getGuiLevel();
		GuiObjectTile tile = null;
		
		if (point.getX() > 0) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() - 1, point.getY()));
			if ((tile != null) && AI.isCommandsHostile(unit.getGameCommand(), tile.getGameCommand())) {
				return tile;
			}
		}
		
		if ((point.getX() > 0) && (point.getY() > 0)) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() - 1, point.getY() - 1));
			if ((tile != null) && AI.isCommandsHostile(unit.getGameCommand(), tile.getGameCommand())) {
				return tile;
			}
		}

		if ((point.getX() > 0) && (point.getY() < level.getLevel().getHeight() - 1)) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() - 1, point.getY() + 1));
			if ((tile != null) && AI.isCommandsHostile(unit.getGameCommand(), tile.getGameCommand())) {
				return tile;
			}
		}

		if (point.getY() < level.getLevel().getHeight() - 1) {
			tile = level.getObjectAtPoint(new Point2D(point.getX(), point.getY() + 1));
			if ((tile != null) && AI.isCommandsHostile(unit.getGameCommand(), tile.getGameCommand())) {
				return tile;
			}
		}
		
		if (point.getY() > 0) {
			tile = level.getObjectAtPoint(new Point2D(point.getX(), point.getY() - 1));
			if ((tile != null) && AI.isCommandsHostile(unit.getGameCommand(), tile.getGameCommand())) {
				return tile;
			}
		}

		if (point.getX() < level.getLevel().getWidth() - 1) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() + 1, point.getY()));
			if ((tile != null) && AI.isCommandsHostile(unit.getGameCommand(), tile.getGameCommand())) {
				return tile;
			}
		}
		
		if ((point.getX() < level.getLevel().getWidth() - 1) && (point.getY() > 0)) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() + 1, point.getY() - 1));
			if ((tile != null) && AI.isCommandsHostile(unit.getGameCommand(), tile.getGameCommand())) {
				return tile;
			}
		}

		if ((point.getX() < level.getLevel().getWidth() - 1) && (point.getY() < level.getLevel().getHeight() - 1)) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() + 1, point.getY() + 1));
			if ((tile != null) && AI.isCommandsHostile(unit.getGameCommand(), tile.getGameCommand())) {
				return tile;
			}
		}		
		
		return null;
	}
}
