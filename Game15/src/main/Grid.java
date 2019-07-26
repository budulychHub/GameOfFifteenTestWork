package main;

public class Grid
{
    public Grid()
    {
    }
    
    public Grid(Grid grid)
    {
        System.arraycopy(grid.gridArray, 0, this.gridArray, 0, this.gridArray.length);
        System.arraycopy(grid.index, 0, this.index, 0, this.index.length);
    }
    
    public Grid(int[][] gridArray)
    {
        System.arraycopy(gridArray, 0, this.gridArray, 0, this.gridArray.length);
    }
    
    public int[][] gridArray  = new int[4][4];
    public int[][] index      = new int[16][2];
    
    final public static int[][] GOAL_GRID =
    {
        {1,  2,  3,  4},
        {5,  6,  7,  8},
        {9,  10, 11, 12},
        {13, 14, 15, 0}
    };
}