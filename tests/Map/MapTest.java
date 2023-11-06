package Map;


import Character.Camera;
import Map.Tile.FieryTile;
import Map.Tile.Tile;
import PickUp.PickUp;
import PickUp.PickUpHeal;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;


/**
 * Parasoft Jtest UTA: Test class for Map
 *
 * @author otsob
 * @see Map
 */
public class MapTest {

    Map underTest;
    int cols;
    int rows;

    @Before
    public void init() {
        cols = 100;
        rows = 100;
        underTest = new Map(cols, rows);
    }
    /**
     * Parasoft Jtest UTA: Test for generateHealthTiles()
     *
     * @author otsob
     * @see Map#generateHealthTiles()
     */
    @Test
    public void testGenerateHealthTilesReturnsTiles() throws Throwable {

        // When
        List<Tile> result = underTest.generateHealthTiles();
        assertNotNull(result);

        // Then
        // assertNotNull(result);
        // assertEquals(0, result.size());
        // assertTrue(result.contains(null));
    }

    @Test
    public void testGenerateHealthTilesResultsContainTheRightTile() throws Throwable {
        List<Tile> result = underTest.generateHealthTiles();
        Tile testTile = underTest.getTileByLoc(0, 1);
        assertEquals(testTile, result.get(0));
    }

    @Test
    public void testGenerateHealthTilesTilesGetSetWithHealthPickUps() throws Throwable {
        List<Tile> result = underTest.generateHealthTiles();
        Tile testTile = underTest.getTileByLoc(0, 1);
        assertNotNull(testTile.getPickUp());
    }

    @Test
    public void testGenerateHealthTilesExist() throws Throwable {

        // When
        underTest.generateHealthTiles();

        boolean healthTile = false;
        for(int i = 0; i < cols * rows; i++)
        {
            PickUp temp = underTest.getTile(i).getPickUp();
            healthTile |= temp instanceof PickUpHeal;
        }

        assertEquals(healthTile, true);

    }

    /**
     * Parasoft Jtest UTA: Test for getTileCount()
     *
     * @author otsob
     * @see Map#getTileCount()
     */
    @Test(timeout = 5000)
    public void testGetTileCount() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // When
        int result = underTest.getTileCount();
        assertEquals(result, 100*100);

    }

    /**
     * Parasoft Jtest UTA: Test for getTileByLoc(int, int)
     *
     * @author otsob
     * @see Map#getTileByLoc(int, int)
     */
    @Test(timeout = 5000)
    public void testGetTileByLoc() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // When
        int x = 0; // UTA: default value
        int y = 0; // UTA: default value
        Tile result = underTest.getTileByLoc(x, y);

        assertNotNull(result);

    }

    @Test
    public void getAllTilesOneByOne() throws Throwable {
        for(int x = 0; x < cols; x++)
            for(int y = 0; y < rows; y++)
            {
                Tile result = underTest.getTileByLoc(x, y);
                assertNotNull(result);
            }
    }

//    @Test(timeout = 5000)
//    public void testGetTileByLocOutsideBounds() throws Throwable {
//        // UTA is unable to resolve the values required to create the requested test case.
//        // A test case with default values has been created instead.
//
//        // When
//        int x = 100; // UTA: default value
//        int y = 0; // UTA: default value
//        Tile result = underTest.getTileByLoc(x, y);
//
//        assertNull(result);
//
//    }

    /**
     * Parasoft Jtest UTA: Test for getTile(int)
     *
     * @author otsob
     * @see Map#getTile(int)
     */
    @Test(timeout = 5000)
    public void testGetTile() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // When
        int index = 0; // UTA: default value
        Tile result = underTest.getTile(index);
        assertNotNull(result);

    }

    @Test
    public void getAllPossibleTilesByIndex() throws Throwable {
        for(int index = 0; index < cols * rows; index++)
        {
            Tile result = underTest.getTile(index);
            assertNotNull(result);
        }
    }

    /**
     * Parasoft Jtest UTA: Test for getRows()
     *
     * @author otsob
     * @see Map#getRows()
     */
    @Test(timeout = 5000)
    public void testGetRows() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // When
        int result = underTest.getRows();
        assertEquals(result, 100);

    }

    /**
     * Parasoft Jtest UTA: Test for getCols()
     *
     * @author otsob
     * @see Map#getCols()
     */
    @Test(timeout = 5000)
    public void testGetCols() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // When
        int result = underTest.getCols();
        assertEquals(result, 100);

    }

    /**
     * Parasoft Jtest UTA: Test for drawMap(Graphics, Camera)
     *
     * @author otsob
     * @see Map#drawMap(Graphics, Camera)
     */
//    @Test(timeout = 5000)
//    public void testDrawMap() throws Throwable {
//        // UTA is unable to resolve the values required to create the requested test case.
//        // A test case with default values has been created instead.
//
//        // When
//        Graphics g = mock(Graphics.class);
//        Camera camera = mock(Camera.class);
//        underTest.drawMap(g, camera);
//
//    }

    /**
     * Parasoft Jtest UTA: Test for copy()
     *
     * @author otsob
     * @see Map#copy()
     */
    @Test(timeout = 5000)
    public void testCopyReturnsNonNull() throws Throwable {
        // UTA is unable to resolve the values required to create the requested test case.
        // A test case with default values has been created instead.

        // When
        Map result = underTest.copy();
        assertNotNull(result);

    }

    /**
     * Parasoft Jtest UTA: Test for generateFieryLines()
     *
     * @author otsob
     * @see Map#generateFieryLines()
     */
    @Test
    public void testGenerateFieryLines() throws Throwable {

        // When
        underTest.generateFieryLines();

        boolean fieryTile = false;
        for(int i = 0; i < cols * rows; i++)
        {
            Tile temp = underTest.getTile(i);
            fieryTile |= temp instanceof FieryTile;
        }

        assertEquals(fieryTile, true);

    }


    /**
     * Parasoft Jtest UTA: Test for BFS(String, String)
     *
     * @author otsob
     * @see Map#BFS(String, String)
     */
//    @Test
//    public void testBFS() throws Throwable {
//        // When
//        String start = "start"; // UTA: default value
//        String end = "end"; // UTA: default value
//        underTest.BFS(start, end);
//
//    }

    /**
     * Parasoft Jtest UTA: Test for decryptCol(String)
     *
     * @author otsob
     * @see Map#decryptCol(String)
     */
    @Test
    public void testDecryptCol() throws Throwable {

        // When
        String coordinate = "10_20"; // UTA: default value
        int result = underTest.decryptCol(coordinate);

        // Then
         assertEquals(10, result);
    }

    /**
     * Parasoft Jtest UTA: Test for decryptRow(String)
     *
     * @author otsob
     * @see Map#decryptRow(String)
     */
    @Test
    public void testDecryptRow() throws Throwable {

        // When
        String coordinate = "10_20"; // UTA: default value
        int result = underTest.decryptRow(coordinate);

        // Then
        assertEquals(20, result);
    }

    /**
     * Parasoft Jtest UTA: Test for getNeighbors(Tile)
     *
     * @author otsob
     * @see Map#getNeighbors(Tile)
     */
    @Test
    public void testGetNeighbors() throws Throwable {
        // Given

        // When
        Tile tile = underTest.getTile(0); // UTA: default value
        ArrayList<Tile> result = underTest.getNeighbors(tile);

        // Then
         assertNotNull(result);
        // assertEquals(0, result.size());
        // assertTrue(result.contains(null));
    }

    @Test
    public void testGetNeighborsGetsCorrectNeighbours() throws Throwable {
        Tile tile = underTest.getTile(0);
        ArrayList<Tile> result = underTest.getNeighbors(tile);

        assertEquals(result.get(0), underTest.getTileByLoc(0, 1));
        assertEquals(result.get(1), underTest.getTileByLoc(1, 0));
    }

    /**
     * Parasoft Jtest UTA: Test for getAreaTiles(ArrayList, int, Tile)
     *
     * @author otsob
     * @see Map#getAreaTiles(ArrayList, int, Tile)
     */
//    @Test
////    public void testGetAreaTiles() throws Throwable {
////
////
////        // When
////        ArrayList<Tile> list = new ArrayList<Tile>(); // UTA: default value
////        int depth = 0; // UTA: default value
////        Tile currentTile = underTest.getTile(0); // UTA: default value
////        underTest.getAreaTiles(list, depth, currentTile);
////
////    }

    /**
     * Parasoft Jtest UTA: Test for paintPath(Tile[], Tile, boolean, Random)
     *
     * @author otsob
     * @see Map#paintPath(Tile[], Tile, boolean, Random)
     */
//    @Test
//    public void testPaintPath() throws Throwable {
//
//        // When
//        Tile[] prec = new Tile[0]; // UTA: default value
//        Tile currentTile = null; // UTA: default value
//        boolean isWide = false; // UTA: default value
//        Random rng = null; // UTA: default value
//        underTest.paintPath(prec, currentTile, isWide, rng);
//
//    }

    /**
     * Parasoft Jtest UTA: Test for isUnavailable(Tile)
     *
     * @author otsob
     * @see Map#isUnavailable(Tile)
     */
    @Test
    public void testIsUnavailable() throws Throwable {
        Tile tile = underTest.getTile(0); // UTA: default value
        boolean result = underTest.isUnavailable(tile);

        // Then
         assertFalse(result);
    }

    /**
     * Parasoft Jtest UTA: Test for isCloseToTile(Tile, Tile)
     *
     * @author otsob
     * @see Map#isCloseToTile(Tile, Tile)
     */
    @Test
    public void testIsCloseToTile() throws Throwable {

        // When
        Tile destination = underTest.getTile(0); // UTA: default value
        Tile currentTile = underTest.getTile(1); // UTA: default value
        boolean result = underTest.isCloseToTile(destination, currentTile);

        // Then
         assertTrue(result);
    }
}