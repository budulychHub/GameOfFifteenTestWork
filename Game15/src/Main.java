import main.*;

public class Main{
    static Grid gameGrid = null;
    
    static CommandUI terminalUI = new CommandUI();

    public static void main(String[] args) throws Exception
    {
        try
        {
            if (!Globals.CHEAT_MODE)
                gameGrid = GridHelper.generateRandomGrid();
            else
            {
                gameGrid = new Grid (Grid.GOAL_GRID);
                GridHelper.reIndex(gameGrid);
                GridHelper.swapTile(15, gameGrid);
            }
                
            
        System.out.println("GameOfFifteen-Java");
        System.out.println();
        
        System.out.println("������ \"help\" ��� ������ ������.");
        System.out.println("���� �� ������ �����, ������� \"demo.\"");
        
        System.out.println();
        CommandUI.printGrid(gameGrid);
        System.out.println();
        System.out.println();
        
        terminalUI.start(gameGrid);
        
        
        System.out.println("������ �� ��� GameOfFifteen. Goodbye!");
        }
        catch (Exception e)
        {
            System.err.println("������� �������� �������, ����� ��������� �� ���������. :)");
            System.err.println();
            
            throw e;
        }
    }
}
