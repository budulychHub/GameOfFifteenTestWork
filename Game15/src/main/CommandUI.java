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
            System.out.print("slide�");
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
                System.err.println("�������, ��� �� �� ����� ����� ������. ���� ����� ��������� �� ���.");
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
                        System.out.println("��� ����???");
                    
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
                        System.err.println("�������, ��� �� �� ����� ����� ������. ���� ����� ��������� �� ���.");
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
                            System.err.println("�����, ��� \"" + input + "\" �� � ������ �������.");
                            System.err.println("��������� \"grid\" ��� \"goal.\"");
                        }
                    }
                    catch (NoSuchElementException e)
                    {
                        System.err.println("�������, ��� �� �� ����� ����� ������. ");
                        System.err.println("��������� \"grid\" ��� \"goal.\"");
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
                        System.err.println("�������, ��� �� �� ����� ���������� ����. ");
                        System.err.println("��������� \"help\" ��� ����������.");
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
                        System.err.println("�������, ��� �� �� ����� ���������� ����. ");
                        System.err.println("��������� \"help\" ��� ����������.");
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
                        System.err.println("�������, ��� �� �� ����� option. ");
                        System.err.println("��������� ������ \"options\" ��� ������.");
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
                    System.err.println("�����, ��� \"" + input + "\" �� � ������ ��������. ");
                    System.err.println("��������� ������ \"help\" ��� ������.");
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
                    System.err.println("Warning: ����������� ���������� �� �������. ��������� �������� ���� ���� �� ��������� \"save\", ��� ������ ������� �����.") ;
                }
            }
            
        }
        else
            System.err.println("�����, ��� \"" + tile + "\" �� � ������ �������. ���� ����� ��������� �� ���.");

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
                System.out.println("��� �� ���� �� �����! (CHEAT_MODE = true)");
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
                System.err.println("�����, ��� \"" + option + "\" �� � ������ ����������. ");
                System.err.println("��������� ������ \"options\" ��� ������.");
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
        System.out.println("���������� ��� � " + saveFile + "...");

        try
        {
            GridHelper.save(saveFile, grid);

            System.out.println("����������!");
            System.out.println();

            defaultSaveFile = saveFile;
        }
        catch (FileNotFoundException e)
        {
            System.err.println("���� ���� �� ���. ");
            System.err.println("��������� ���� ��� / ���� ������������ ��� ������ �������, ��� ��������� ����������.");
        }
    }
    
    private void loadGame(String saveFile, Grid grid)
    {
        System.out.println("������������ ��� " + saveFile + "...");

        try
        {
            GridHelper.load(saveFile, grid);

            System.out.println("������������ �����e!");
            System.out.println();

            defaultSaveFile = saveFile;
        }
        catch (FileNotFoundException | IllegalArgumentException | NoSuchElementException e)
        {
            System.err.println("���� ���� �� ��� �� ��� ������������. ");
            System.err.println("��������� ���� ���/���� ������������ ��� ������ �������, ��� ��������� �������.");
        }
    }
    
    private static void runDemo()
    {
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("GameOfFifteen-Java");

        System.out.println("������� �������, � GameOfFifteen-Java. �������� ������ � ����, ��� ������� ���� ���:");
        System.out.println();
        
        printGrid(new Grid(Grid.GOAL_GRID));
        System.out.println();

        System.out.println("��������� ������ < > ^ \"(��� �� ���� ���� ���� :) )\", ����� ����� �� 1 �� 15 � �������� ������ Enter.");
        System.out.println("�� ������ �������� ��� ����������� ���, ����� \"save\" ��� \"load\" � ��'� �����.");
        System.out.println("������ ��������, �������� ����� \"newgame.\"");
        System.out.println("��� �������� ��� ���� �����) �������, ������ \"help.\"");
        System.out.println();

        System.out.println("�������������� ����!!!");
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println();
    }
    
    private static void displayHelp()
    {
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Commands:");

        System.out.println("help\t\t³������� ������ ������ ������.");
        System.out.println("demo\t\t������� ��� ����� ���.");
        System.out.println();

        System.out.println("newgame\t\t���������� ������� ��� �� ���������� ����.");
        System.out.println("slide\tnum\t������� ������ �� �������� �������.");
        //System.out.println("print\tstr\t����� ������� \"grid\" ��� \"goal\" ������.");
        System.out.println();

        System.out.println("save\tstr\t������ ��� � �������� ����.");
        System.out.println("load\tstr\t��������� ��� � ��������� �����.");
        System.out.println();

        System.out.println("options\t\t³������� ������ ������ �����");
        System.out.println("enable\tstr\t����� �������� ��������");
        System.out.println("disable\tstr\t������ �������� ��������");
        System.out.println();

        System.out.println("about\t\t��� GameOfFifteen");
        System.out.println("exit\t\t����� � GameOfFifteen");
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println();
    }
    
    private static void displayOptions()
    {
        System.out.println();
        System.out.println("----------------------------------------------------------------------------------------------------");
        System.out.println("Options:");
        System.out.println();

        System.out.println("autoSave bool\t���� ��������, ����������� ������ ��� �� ���������� ����� ����������. �������� �� �������������.");
        System.out.println("autoGrid bool\t���� ��������, �������������� ���� ���� ������ ������. �������� �� �������������.");
        System.out.println("autoExit bool\t���� ��������, ���� ������� ����������� �������� � ���. �������� �� �������������.");
        System.out.println("easySlide bool\t�������� �������� ������ ������� �����, � �� \"slide \". �������� �� �������������.");
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
        System.out.println("GameOfFifteen-Java - �� ������, ��������������, �����-������, �� ���������� � ������������ ������� GameOfFifteen (��������).");
        System.out.println();


        System.out.println();
        System.out.println("��������� ���������� ��:");
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