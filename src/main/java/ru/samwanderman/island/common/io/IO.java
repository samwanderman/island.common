/**
 * @author Potapov Sergei (sam-wanderman@yandex.ru)
 */
package ru.samwanderman.island.common.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import ru.samwanderman.island.common.core.object.Level;
import ru.samwanderman.island.common.core.object.TilePoint;
import ru.samwanderman.wheel.io.Resources;
import ru.samwanderman.wheel.view.figure.Point2D;

/**
 * Level save/load
 */
public class IO {
	private static final String TILES_PATH = "./resources/tiles/";
	/**
	 * Load tile info
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static final <T> T loadTile(final String path, final Class<T> tileClass) 
			throws IOException {
		return Resources.loadObject("./tiles/" + path + ".json", tileClass);
	}
	
	/**
	 * Load all tiles for given type from standart folders
	 * 
	 * @return
	 * @throws IOException
	 */
	public static final <T> List<T> loadTiles(final String path, final Class<T> tileClass) 
			throws IOException {
		final File file = new File(TILES_PATH + path);
		
		if (!file.isDirectory()) {
			throw new IOException();	
		}

		final String[] files = file.list();
		final List<T> tiles = new ArrayList<>();
		for (final String f: files) {
			final T tile = Resources.loadObject(new File(TILES_PATH + path + "/" + f), tileClass);
			tiles.add(tile);
		}
		
		return tiles;
	}

	public static final Level loadLevel(final String path) 
			throws IOException {
		final JsonNode json = Resources.loadJSON(path, true);		
		final Level level = new Level();		
		level.setId(json.get("id").asText());
		level.setName(json.get("name").asText());
		level.setDescription(json.get("description").asText());
		level.setWidth(json.get("width").asInt(0));
		level.setHeight(json.get("height").asInt(0));
		
		final List<TilePoint> landscapeTiles = new ArrayList<>();
		final List<TilePoint> objectTiles = new ArrayList<>();
		final List<TilePoint> unitTiles = new ArrayList<>();
		level.setLandscapeTiles(landscapeTiles);
		level.setObjectTiles(objectTiles);
		level.setUnitTiles(unitTiles);
		
		JsonNode tilesNode = json.get("landscapeTiles");
		Iterator<JsonNode> iterator = null;
		if (tilesNode != null) {
			iterator = tilesNode.elements();
			while (iterator.hasNext()) {
				final JsonNode node = iterator.next();
				final Point2D point = new Point2D(node.get("point").get("x").asInt(0), node.get("point").get("y").asInt(0));
				final TilePoint tile = new TilePoint(node.get("tile").asText(), point);
				landscapeTiles.add(tile);
			}
		}
		
		tilesNode = json.get("objectTiles");
		if (tilesNode != null) {
			iterator = tilesNode.elements();
			while (iterator.hasNext()) {
				final JsonNode node = iterator.next();
				final Point2D point = new Point2D(node.get("point").get("x").asInt(0), node.get("point").get("y").asInt(0));
				final TilePoint tile = new TilePoint(node.get("tile").asText(), point);
				objectTiles.add(tile);
			}
		}
		
		tilesNode = json.get("unitTiles");
		if (tilesNode != null) {
			iterator = tilesNode.elements();
			while (iterator.hasNext()) {
				final JsonNode node = iterator.next();
				final Point2D point = new Point2D(node.get("point").get("x").asInt(0), node.get("point").get("y").asInt(0));
				final TilePoint tile = new TilePoint(node.get("tile").asText(), point);
				unitTiles.add(tile);
			}
		}
		
		return level;
	}
	
	public static final boolean saveLevel(final String path, final Level level) 
			throws IOException {
		Resources.saveObject(path, level, true);
		return true;
	}
}
