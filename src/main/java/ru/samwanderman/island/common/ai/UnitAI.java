package ru.samwanderman.island.common.ai;

import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.island.common.view.tile.GuiObjectTile;
import ru.samwanderman.island.common.view.tile.GuiUnitTile;
import ru.samwanderman.wheel.ai.IAI;
import ru.samwanderman.wheel.view.figure.Point2D;

public final class UnitAI implements IAI {
	private final GuiUnitTile unit;
	
	public UnitAI(final GuiUnitTile unit) {
		this.unit = unit;
	}
	
	@Override
	public final void sync() {
		final boolean isMove = checkMove();
		final boolean canAttack = checkAttack();
		
		if (canAttack && !isMove) {
			unit.stopCurrentAnimation();
			unit.playAnimation("attack");
			return;
		} else if (isMove) {
			return;
		} else {
			unit.stopCurrentAnimation();
		}
	}
	
	/**
	 * Check if unit is moving
	 * 
	 * @return
	 */
	private final boolean checkMove() {
		return unit.isChangePositionAnimationRunning();
	}
	
	/**
	 * Check if unit can attack
	 * 
	 * @return
	 */
	private final boolean checkAttack() {
		final Point2D point = unit.getPoint();
		final GuiLevel level = unit.getGuiLevel();
		GuiObjectTile tile = null;
		
		if (point.getX() > 0) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() - 1, point.getY()));
			if ((tile != null) && (tile.getGameCommand() != 0) && (unit.getGameCommand() != tile.getGameCommand())) {
				return true;
			}
		}
		
		if ((point.getX() > 0) && (point.getY() > 0)) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() - 1, point.getY() - 1));
			if ((tile != null) && (tile.getGameCommand() != 0)&& (unit.getGameCommand() != tile.getGameCommand())) {
				return true;
			}
		}

		if ((point.getX() > 0) && (point.getY() < level.getLevel().getHeight() - 1)) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() - 1, point.getY() + 1));
			if ((tile != null) && (tile.getGameCommand() != 0)&& (unit.getGameCommand() != tile.getGameCommand())) {
				return true;
			}
		}

		if (point.getY() < level.getLevel().getHeight() - 1) {
			tile = level.getObjectAtPoint(new Point2D(point.getX(), point.getY() + 1));
			if ((tile != null) && (tile.getGameCommand() != 0)&& (unit.getGameCommand() != tile.getGameCommand())) {
				return true;
			}
		}
		
		if (point.getY() > 0) {
			tile = level.getObjectAtPoint(new Point2D(point.getX(), point.getY() - 1));
			if ((tile != null) && (tile.getGameCommand() != 0)&& (unit.getGameCommand() != tile.getGameCommand())) {
				return true;
			}
		}

		if (point.getX() < level.getLevel().getWidth() - 1) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() + 1, point.getY()));
			if ((tile != null) && (tile.getGameCommand() != 0)&& (unit.getGameCommand() != tile.getGameCommand())) {
				return true;
			}
		}
		
		if ((point.getX() < level.getLevel().getWidth() - 1) && (point.getY() > 0)) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() + 1, point.getY() - 1));
			if ((tile != null) && (tile.getGameCommand() != 0) && (unit.getGameCommand() != tile.getGameCommand())) {
				return true;
			}
		}

		if ((point.getX() < level.getLevel().getWidth() - 1) && (point.getY() < level.getLevel().getHeight() - 1)) {
			tile = level.getObjectAtPoint(new Point2D(point.getX() + 1, point.getY() + 1));
			if ((tile != null) && (tile.getGameCommand() != 0) && (unit.getGameCommand() != tile.getGameCommand())) {
				return true;
			}
		}		
		
		return false;
	}
}
