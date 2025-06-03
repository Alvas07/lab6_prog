package managers.commands;

import exceptions.CommandExecuteException;
import exceptions.UnknownCommandException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import managers.*;

/**
 * Класс, отвечающий за команду "execute_script".
 *
 * <p>Описание команды: "Считать и исполнить скрипт из указанного файла".
 *
 * <p>Принимает на вход один обязательный аргумент - путь к файлу со скриптом (тип {@code String}).
 *
 * @see Command
 * @author Alvas
 * @since 1.0
 */
public class ExecuteScriptCommand implements Command {
  private final CollectionManager collectionManager;
  private final FileManager dataFileManager;
  private final Console console;
  private final ScriptManager scriptManager;

  /**
   * Конструктор команды.
   *
   * @param collectionManager менеджер коллекции.
   * @param dataFileManager менеджер файла с исходными данными.
   * @param console консоль, управляющая работой приложения.
   * @see CollectionManager
   * @see FileManager
   * @author Alvas
   * @since 2.0
   */
  public ExecuteScriptCommand(
      CollectionManager collectionManager, FileManager dataFileManager, Console console) {
    this.collectionManager = collectionManager;
    this.dataFileManager = dataFileManager;
    this.console = console;
    this.scriptManager = console.getScriptManager();
  }

  /**
   * Исполняет команду с заданными параметрами.
   *
   * @param args аргументы команды.
   * @throws CommandExecuteException если указано неверное количество аргументов команды.
   * @author Alvas
   * @since 1.0
   */
  @Override
  public void execute(String[] args) throws CommandExecuteException {
    if (args.length != 2) {
      throw new CommandExecuteException("Команда принимает один обязательный аргумент.");
    }

    String fileName = args[1];
    FileManager scriptFileManager = new FileManager(fileName);
    CommandManager commandManager = new CommandManager(collectionManager, dataFileManager, console);
    ScannerManager scannerManager = console.getScannerManager();
    boolean recursionFlag = false;
    if (!scriptFileManager.canRead()) {
      throw new CommandExecuteException("Невозможно прочитать информацию из файла.");
    }

    try {
      scriptManager.activateFileMode();
      scriptManager.addPath(fileName);
      Scanner currentScanner;

      while (!scriptManager.getAllScanners().isEmpty()) {
        currentScanner = scriptManager.getLastScanner();
        if (currentScanner.hasNextLine()) {
          scannerManager.setScanner(currentScanner);
        } else {
          scriptManager.removePath();
          scannerManager.setScanner(scriptManager.getLastScanner());
          currentScanner = scriptManager.getLastScanner();
        }

        String input = currentScanner.nextLine();
        String[] commandParts = input.trim().split(" ");

        if (commandParts[0].equalsIgnoreCase("execute_script")
            && scriptManager.isRecursive(commandParts[1])) {
          System.out.println(
              "Обнаружена рекурсия! Отмена скрипта! Повторно вызывается файл "
                  + new File(commandParts[1]).getAbsolutePath());
          recursionFlag = true;
          continue;
        }

        System.out.println("Выполнение команды " + commandParts[0] + ":");
        try {
          commandManager.startExecuting(input);
        } catch (UnknownCommandException e) {
          System.out.println(e.getMessage());
        } catch (NoSuchElementException e) {
          currentScanner = new Scanner(System.in);
          scannerManager.setScanner(currentScanner);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    } catch (NoSuchElementException ignored) {
    } finally {
      scriptManager.deactivateFileMode();
      if (!recursionFlag) {
        System.out.println("Скрипт " + args[1] + " выполнен!");
      }
    }
  }

  /**
   * Возвращает название команды.
   *
   * @return Название команды.
   * @author Alvas
   * @since 1.0
   */
  @Override
  public String getName() {
    return "execute_script";
  }

  /**
   * Возвращает описание команды.
   *
   * @return Описание команды.
   * @author Alvas
   * @since 1.0
   */
  @Override
  public String getDescription() {
    return "считать и исполнить скрипт из указанного файла";
  }
}
