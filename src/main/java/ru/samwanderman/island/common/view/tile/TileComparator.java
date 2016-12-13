/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.view.tile;

import java.util.Comparator;

import ru.samwanderman.island.common.view.GuiLevel;
import ru.samwanderman.wheel.view.figure.Point2D;

/**
 * GuiTile comparator
 */
public final class TileComparator<T extends GuiTile<GuiLevel, ?>> implements Comparator<T> {
	public final int compare(final T o1, final T o2) {
		final Point2D p1 = o1.getPoint(), p2 = o2.getPoint();

		// Layer sorting
		if (o1.getZ() > o2.getZ()) {
			return 1;
		}
		
		if (o1.getZ() < o2.getZ()) {
			return -1;
		}

		// If same layer - tile pos sorting
		if (((p1.getX() >= p2.getX()) && (p1.getY() > p2.getY())) || ((p1.getX() > p2.getX()) && (p1.getY() >= p2.getY()))) {
			return 1;
		}
		
		if (((p1.getX() <= p2.getX()) && (p1.getY() < p2.getY())) || ((p1.getX() < p2.getX()) && (p1.getY() <= p2.getY()))) {
			return -1;
		}
		
		return 0;
	}
}
