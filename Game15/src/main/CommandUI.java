package main;

import java.io.FileNotFoundException;
import java.util.*;

public class CommandUI{    
    private boolean autoSave    = Globals.AUTOSAVE_DEFAULT;
    private boolean autoGrid    = true;
    private boolean autoExit    = false;
    private boolean easySlide   = true;
    
    private boolean wantsToExit = false;
    private String defaultSaveFile = "";
    
    public void start(Grid grid)
    {
        Scanner inputScanner = new Scanner(System.in);

        while(!wantsToExit)
        {
            System.out.print("slide»");
            handleCommand(inputScanner.nextLine(), grid);
        }
    }
    
    public void handleCommand(String inputtedLine, Grid grid)
    {
        Scanner tokenScanner = new Scanner(inputtedLine);
        
        if (easySlide && tokenScanner.hasNextInt())
        {
            try
            {
                swapTile(tokenScanner.nextInt(), grid);
            }
            catch (NoSuchElementException e)
            {
                System.err.println("Вибачте, але ви не ввели номер плитки. Будь ласка спробуйте ще раз.");
            }
        }
        else
        {
            String input = "";
            
            try
            {
                input = tokenScanner.next();
            }
            catch (NoSuchElementException e) {}
            
            
            switch (input)
            {
                case "help":
                {
                    displayHelp();
                    break;
                }
                case "demo":
                {
                    runDemo();
                    break;
                }
                case "newgame":
                {
                    if (Globals.CHEAT_MODE)
                        System.out.println("Але чому???");
                    
                    grid = GridHelper.generateRandomGrid();
                    
                    System.out.println();
                    CommandUI.printGrid(grid);
                    System.out.println();
                    break;
                }
                case "slide":
                {
                    try
                    {
                        swapTile(tokenScanner.nextInt(), grid);
                    }
                    catch (NoSuchElementException e)
                    {
                        System.err.println("Вибачте, але ви не ввели номер плитки. Будь ласка спробуйте ще раз.");
                    }
                    
                    break;
                }
                case "print":
                {
                    System.out.println();
                    
                    try
                    {
                        input = tokenScanner.next();
                        
                        if (input.equals("grid"))
                            printGrid(grid);
                        else if (input.equals("goal"))
                            printGrid(new Grid (Grid.GOAL_GRID));
                        else
                        {
                            System.err.println("Вибач, але \"" + input + "\" не є дійсною плиткою.");
                            System.err.println("Спробуйте \"grid\" або \"goal.\"");
                        }
                    }
                    catch (NoSuchElementException e)
                    {
                        System.err.println("Вибачте, але ви не ввели номер плитки. ");
                        System.err.println("Спробуйте \"grid\" або \"goal.\"");
                    }
                        
                    System.out.println();
                    break;
                }
                case "save":
                {
                    try
                    {
                        saveGame(tokenScanner.next(), grid);
                    }
                    catch (NoSuchElementException e)
                    {
                        System.err.println("Вибачте, але ви не ввели збережений файл. ");
                        System.err.println("Спробуйте \"help\" для інформації.");
                    }
                    
                    break;
                }
                case "load":
                {
                    try
                    {
                        loadGame(tokenScanner.next(), grid);
                        CommandUI.printGrid(grid);
                        System.out.println();
                    }
                    catch (NoSuchElementException e)
                    {
                        System.err.println("Вибачте, але ви не ввели збережений файл. ");
                        System.err.println("Спробуйте \"help\" для інформації.");
                    }
                    break;
                }
                case "options":
                {
                    displayOptions();
                    break;
                }
                case "enable":
                case "disable":
                {
                    try
                    {
                        handleOptions(tokenScanner.next(), input.equals("enable"));
                    }
                    catch (NoSuchElementException e)
                    {
                        System.err.println("Вибачте, але ви не ввели option. ");
                        System.err.println("Спробуйте ввести \"options\" для списку.");
                    }
                    
                    break;
                }
                case "about":
                {
                    displayAbout();
                    break;
                }
                case "exit":
                {
                    wantsToExit = true;
                    break;
                }
                case "debug":
                {
                    System.err.println("not implemented");
                    break;
                }
                case "":
                    break;
                default:
                {
                    System.err.println("Вибач, але \"" + input + "\" не є дійсною командою. ");
                    System.err.println("Спробуйте ввести \"help\" для списку.");
                    break;
                }
            }
        }
    }
    
    private void swapTile(int tile, Grid grid)
    {
        if (tile > 0 && tile < 16 && GridHelper.validMove(tile, grid))
        {
            GridHelper.swapTile(tile, grid);

            wantsToExit = autoExit && GridHelper.hasWon(grid);

            
            if (autoSave && !(defaultSaveFile.equals("")))
            {
                try
                {
                    GridHelper.save(defaultSaveFile, grid);
                }
                catch (FileNotFoundException e)
                {
                    System.err.println("Warning: автоматичне збереження не вдалося. Спробуйте зберегти нове місце за допомогою \"save\", або змінити дозволи файлів.") ;
                }
            }
            
        }
        else
            System.err.println("Вибач, але \"" + tile + "\" не є дійсною плиткою. Будь ласка спробуйте ще раз.");

        if (autoGrid)
        {
            System.out.println();
            printGrid(grid);
            System.out.println();
        }

        if (GridHelper.hasWon(grid))
        {
            System.out.println("YOU WIN!!!");
            System.out.println();
            
            if (Globals.CHEAT_MODE)
            {
                System.out.println("Але ти грав не чесно! (CHEAT_MODE = true)");
            }
        }
    }
    
    private void handleOptions(String option, boolean optionSetting)
    {
        switch (option)
        {
            case "autoSave":
            {
                this.autoSave = optionSetting;
                break;
            }
            case "autoGrid":
            {
                this.autoGrid = optionSetting;
                break;
            }
            case "autoExit":
            {
                this.autoExit = optionSetting;
                break;
            }
            case "easySlide":
            {
                this.easySlide = optionSetting;
                break;
            }
            default:
            {
                System.err.println("Вибач, але \"" + option + "\" не є дійсною настройкою. ");
                System.err.println("Спробуйте ввести \"options\" для списку.");
            }
        }
    }
    
    public static void printGrid(Grid grid)
    {
        System.out.println("-----------------");

        for (int i = 0; i < 4; ++i)
        {
            System.out.print("|");
            for (int j = 0; j < 4; ++j)
            {
                if (grid.gridArray[i][j] == 0)
                {
                    System.out.print("  0");
                }
                else
                {
                    if (grid.gridArray[i][j] <= 9)
                        System.out.print(" ");

                    System.out.print(" " + grid.gridArray[i][j]);
                }

                System.out.print("|");
            }

            System.out.println();

            if (i <= 2)
                System.out.println("-----------------");
        }

        System.out.println("-----------------");
    }
    
    private void saveGame(String saveFile, Grid grid)
    {
        System.out.println("Збереження гри в " + saveFile + "...");

        try
        {
            GridHelper.save(saveFile, grid);

            System.out.println("Збереженно!");
            System.out.println();

            defaultSaveFile = saveFile;
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Щось пішло не так. ");
            System.err.println("Спробуйте інше ім’я / місце розташування або змінити дозволи, щоб дозволити збереження.");
        }
    }
    
    private void loadGame(String saveFile, Grid grid)
    {
        System.out.println("Завантаження гри " + saveFile + "...");

        try
        {
            GridHelper.load(saveFile, grid);

            System.out.println("Завантаження успішнe!");
            System.out.println();

            defaultSaveFile = saveFile;
        }
        catch (FileNotFoundException | IllegalArgumentException | NoSuchElementException e)
        {
            System.err.println("Щось пішло не так під час завантаження. ");
            System.err.println("Спробуйте інше ім’я/місце розташування або змінити дозволи, щоб дозволити читання.");
        }
    }
    
    private static void runDemo()
    {
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("GameOfFifteen-Java");

        System.out.println("Ласкаво просимо, в GameOfFifteen-Java. Завдання полягає в тому, щоб зробити сітку так:");
        System.out.println();
        
        printGrid(new Grid(Grid.GOAL_GRID));
        System.out.println();

        System.out.println("Перемістіть плитки < > ^ \"(тут має бути знак вниз :) )\", ввівши число від 1 до 15 і натисніть клавішу Enter.");
        System.out.println("Ви можете зберегти або завантажити гру, ввівши \"save\" або \"load\" і ім'я файлу.");
        System.out.println("Почніть спочатку, набравши текст \"newgame.\"");
        System.out.println("Щоб дізнатися про більш класні) команди, введіть \"help.\"");
        System.out.println();

        System.out.println("Насолоджуйтесь грою!!!");
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println();
    }
    
    private static void displayHelp()
    {
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Commands:");

        System.out.println("help\t\tВідображає список дійсних команд.");
        System.out.println("demo\t\tПроведе вас через гру.");
        System.out.println();

        System.out.println("newgame\t\tЗакінчується поточна гра та починається нова.");
        System.out.println("slide\tnum\tРозсуває плитку за вказаним номером.");
        //System.out.println("print\tstr\tДрукує поточну \"grid\" або \"goal\" плитку.");
        System.out.println();

        System.out.println("save\tstr\tЗберігає гру у вказаний файл.");
        System.out.println("load\tstr\tЗавантажує гру з вказаного файлу.");
        System.out.println();

        System.out.println("options\t\tВідображає список дійсних опцій");
        System.out.println("enable\tstr\tВмикає вказаний параметр");
        System.out.println("disable\tstr\tВимикає вказаний параметр");
        System.out.println();

        System.out.println("about\t\tПро GameOfFifteen");
        System.out.println("exit\t\tВихід з GameOfFifteen");
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println();
    }
    
    private static void displayOptions()
    {
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Options:");
        System.out.println();

        System.out.println("autoSave bool\tЯкщо включено, автоматично зберігає гру до останнього файлу збереження. Увімкнено за замовчуванням.");
        System.out.println("autoGrid bool\tЯкщо ввімкнено, автороздруковує сітку після певних команд. Увімкнено за замовчуванням.");
        System.out.println("autoExit bool\tЯкщо увімкнено, після виграшу автоматично виходить з гри. Вимкнено за замовчуванням.");
        System.out.println("easySlide bool\tДозволяє спочатку просто вводити число, а не \"slide \". Увімкнено за замовчуванням.");
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println();
    }
    
    private static void displayAbout()
    {
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("GameOfFifteen-Java");
        System.out.println("Budulych Stanislav 2019");
        System.out.println();

        System.out.println();
        System.out.println("GameOfFifteen-Java - це весела, кросплатформна, слайд-плитка, що перенесена з оригінального проекту GameOfFifteen (Пятнашка).");
        System.out.println();


        System.out.println();
        System.out.println("Додаткова інформація на:");
        System.out.println();

        System.out.println("GameOfFifteen Version " + Globals.Game_VERSION_STRING);
        if (Globals.CHEAT_MODE)
            System.out.println("CHEATING BUILD");
        System.out.println();

        System.out.println("Running on: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
        System.out.println("Java Runtime: " + System.getProperty("java.vendor") + " " + System.getProperty("java.version"));
        System.out.println("Java VM: " + System.getProperty("java.vm.vendor") + " " + System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version"));
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println();
    }
}