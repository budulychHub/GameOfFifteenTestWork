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
        
        System.out.println("Введіть \"help\" для списку команд.");
        System.out.println("Якщо ви вперше граєте, наберіть \"demo.\"");
        
        System.out.println();
        CommandUI.printGrid(gameGrid);
        System.out.println();
        System.out.println();
        
        terminalUI.start(gameGrid);
        
        
        System.out.println("Дякуємо за гру GameOfFifteen. Goodbye!");
        }
        catch (Exception e)
        {
            System.err.println("Сталася фатальна помилка, скоро розробник її виправить. :)");
            System.err.println();
            
            throw e;
        }
    }
}
