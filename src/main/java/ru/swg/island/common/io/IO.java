/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.swg.island.common.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import ru.swg.island.common.core.object.Level;
import ru.swg.island.common.core.object.Tile;
import ru.swg.island.common.core.object.TilePoint;
import ru.swg.island.common.view.GuiLevel;
import ru.swg.wheelframework.io.Resources;
import ru.swg.wheelframework.view.Point2D;

/**
 * Level save/load
 */
public class IO {
	/**
	 * Load tile info
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static final Tile loadTile(final String path) 
			throws IOException {
		return Resources.loadObject("./tiles/" + path + ".json", Tile.class);
	}
	
	/**
	 * Load level info
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static final Level loadLevelInfo(final String path)
			throws IOException {
		final JsonNode json = Resources.loadJSON("./levels/" + path + ".json");		
		final Level level = new Level();		
		level.setId(json.get("id").asText());
		level.setWidth(json.get("width").asInt(0));
		level.setHeight(json.get("height").asInt(0));
		
		final List<TilePoint> landscapeTiles = new ArrayList<>();
		final List<TilePoint> objectTiles = new ArrayList<>();
		level.setLandscapeTiles(landscapeTiles);
		level.setObjectTiles(objectTiles);
		
		Iterator<JsonNode> iterator = json.get("landscapeTiles").elements();
		while (iterator.hasNext()) {
			final JsonNode node = iterator.next();
			final Point2D point = new Point2D(node.get("point").get("x").asInt(0), node.get("point").get("y").asInt(0));
			final TilePoint tile = new TilePoint(node.get("tile").asText(), point);
			landscapeTiles.add(tile);
		}
		
		iterator = json.get("objectTiles").elements();
		while (iterator.hasNext()) {
			final JsonNode node = iterator.next();
			final Point2D point = new Point2D(node.get("point").get("x").asInt(0), node.get("point").get("y").asInt(0));
			final TilePoint tile = new TilePoint(node.get("tile").asText(), point);
			objectTiles.add(tile);
		}
		
		return level;
	}
	
	/**
	 * Load level
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static final GuiLevel loadLevel(final String path) 
			throws IOException {
		return new GuiLevel(loadLevelInfo(path));
	}
}
