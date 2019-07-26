package main;

import java.io.*;
import java.util.*;

public class GridHelper
{
    public static boolean validMove(int tileNum, Grid grid) throws IllegalArgumentException, IndexOutOfBoundsException
    {
        if (tileNum > 0 && tileNum < 16)
            return validMove(grid.index[tileNum][0], grid.index[tileNum][1], grid);
        else
            throw new IllegalArgumentException("tileNum недійсний!");
    }
    
    public static boolean validMove(int tileY, int tileX, Grid grid) throws IllegalArgumentException, IndexOutOfBoundsException
    {
        if (validGrid(grid))
        {
            if (tileY > 3 || tileY < 0 || tileX > 3 || tileX < 0)
                throw new IndexOutOfBoundsException("Координати поза межами!");
            else if (tileY == grid.index[0][0] && tileX == grid.index[0][1])
                return false;
            else if (tileY == grid.index[0][0] && ((tileX == grid.index[0][1] - 1) || (tileX == grid.index[0][1] + 1)))
                return true;
            else if (tileX == grid.index[0][1] && ((tileY == grid.index[0][0] - 1) || (tileY == grid.index[0][0] + 1)))
                return true;
            else
                return false;
        }
        else
            throw new IllegalArgumentException("Плитка недійсна!");
    }
    
    public static boolean hasWon(Grid grid) throws IllegalArgumentException
    {
        if (hasValidGridArray(grid))
            return Arrays.equals(grid.gridArray, Grid.GOAL_GRID);
        else
            throw new IllegalArgumentException("Плитка недійсна!");
    }
    
    public static boolean hasValidGridArray(Grid grid)
    {
        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < 4; ++j)
            {
                if (grid.gridArray[i][j] > 15 || grid.gridArray[i][j] < 0)
                    return false;
            }
        
        int[] numCount = new int[16];
        
        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < 4; ++j)
                    numCount[grid.gridArray[i][j]] += 1;
        
        for (int i = 0; i < 16; ++i)
        {
            if (numCount[i] != 1)
                return false;
        }
        
        return true;
    }
    
    public static boolean validGrid(Grid grid)
    {
        return hasValidGridArray(grid) && hasValidIndex(grid);
    }
    
    public static boolean hasValidIndex(Grid grid)
    {
        Grid tempGrid = new Grid(grid);
        reIndex(tempGrid);
        
        return Arrays.equals(grid.index, tempGrid.index);
    }
    
    public static void swapTile(int tileNum, Grid grid) throws IllegalArgumentException, IndexOutOfBoundsException
    {
        if ((tileNum < 16 && tileNum >=0))
        {
            int tileY = grid.index[tileNum][0];
            int tileX = grid.index[tileNum][1];
            
            if (validMove(tileY, tileX, grid))
                swapTile(tileY, tileX, grid);
            else
                throw new IllegalArgumentException("Недійсний хід");
        }
        else
            throw new IndexOutOfBoundsException("tileNum поза межами!");
    }
    
    public static void swapTile(int tileY, int tileX, Grid grid)
    {
        if (validMove(tileY, tileX, grid))
        {
            int tileNum = grid.gridArray[tileY][tileX];
            
            int oldNoTileY = grid.index[0][0];
            int oldNoTileX = grid.index[0][1];

            grid.gridArray[grid.index[0][0]][grid.index[0][1]] = tileNum;

            grid.index[tileNum][0] = oldNoTileY;
            grid.index[tileNum][1] = oldNoTileX;

            grid.gridArray[tileY][tileX] = 0;

            grid.index[0][0] = tileY;
            grid.index[0][1] = tileX;
        }
        else
            throw new IllegalArgumentException("Недійсний хід!");
    }
    
    public static Grid generateRandomGrid()
    {

        int[] linearGrid =
        {
            1,  2,  3,  4,
            5,  6,  7,  8,
            9,  10, 11, 12,
            13, 14, 15, 0
        };

        Grid tempGrid = new Grid();

        do
        {
            simpleShuffle(linearGrid);

            for (int i = 0; i < 4; ++i)
                for (int j = 0; j < 4; ++j)
                    tempGrid.gridArray[i][j] = linearGrid[(i * 4) + j];
            
            reIndex(tempGrid);
        }
        while (!solvableGrid(tempGrid));

        return tempGrid;
    }
    
    private static void simpleShuffle(int[] linearGrid)
    {

        Random generator = new Random(System.currentTimeMillis());
        
        for (int i = 15; i > 0; --i)
        {
          int index = generator.nextInt(i + 1);
          int a = linearGrid[index];
          linearGrid[index] = linearGrid[i];
          linearGrid[i] = a;
        }
    }
    
    public static boolean solvableGrid(Grid grid) throws IllegalArgumentException
    {
        if (validGrid(grid))
        {
            int[] linearGrid = new int[16];
            int numberOfInversions = 0;

            for (int i = 0; i < 4; ++i)
                for (int j = 0; j < 4; ++j)
                    linearGrid[(i * 4) + j] = grid.gridArray[i][j];

            for (int i = 0; i < 15; ++i)
            {
                for (int j = 1; j < (16 - i); ++j)
                {
                    if ((linearGrid[i] != 0) && (linearGrid[i + j] != 0))
                        if ((linearGrid[i] > linearGrid[i + j]))
                            ++numberOfInversions;
                }
            }


            boolean evenInversions = (numberOfInversions % 2) == 0;

            boolean noTileOnEvenRow = ((4 - grid.index[0][0]) % 2) == 0;

            return evenInversions != noTileOnEvenRow;
        }
        else
            throw new IllegalArgumentException("Плитка недійсна!");
    }
    
    public static void reIndex(Grid grid) throws IllegalArgumentException
    {
        if (hasValidGridArray(grid))
        {
            for(int i = 0; i < 4; ++i)
                for(int j = 0; j < 4; ++j)
                {
                    grid.index[grid.gridArray[i][j]][0] = i;
                    grid.index[grid.gridArray[i][j]][1] = j;
                }
        }
        else
            throw new IllegalArgumentException("Плитка недійсна!");
    }
    
    public static void save(String saveFile, Grid grid) throws FileNotFoundException
    {
        if (hasValidGridArray(grid))
        {
            PrintWriter saveFileWriter = new PrintWriter(saveFile);

            for(int i = 0; i < 4; ++i)
                for(int j = 0; j < 4; ++j)
                    saveFileWriter.print(grid.gridArray[i][j] + " ");
            
            saveFileWriter.close();
        }
        else
            throw new IllegalArgumentException("Плитка недійсна!");
    }
    
    public static void load(String saveFile, Grid grid) throws FileNotFoundException
    {
        Scanner saveFileScanner = new Scanner(new File(saveFile));

        for(int i = 0; i < 4; ++i)
            for(int j = 0; j < 4; ++j)
                grid.gridArray[i][j] = saveFileScanner.nextInt();

        saveFileScanner.close();

        reIndex(grid);
    }
}