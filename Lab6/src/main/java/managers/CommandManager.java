package managers;

import exceptions.CommandExecuteException;
import exceptions.UnknownCommandException;

import java.util.HashMap;
import java.util.LinkedHashMap;
import managers.commands.*;

/**
 * Класс, отвечающий за связь между командами и {@link CollectionManager}.
 *
 * @see CollectionManager
 * @author Alvas
 * @since 1.0
 */
public class CommandManager {
  private final HashMap<String, Command> commandList;

  /**
   * Конструктор менеджера команд.
   *
   * <p>Создает коллекцию {@link LinkedHashMap} для хранения всех команд и помещает туда все
   * существующие.
   *
   * @param collectionManager менеджер коллекции {@link CollectionManager}.
   * @param fileManager файловый менеджер {@link FileManager}.
   * @see CollectionManager
   * @see FileManager
   * @see LinkedHashMap
   * @author Alvas
   * @since 2.0
   */
  public CommandManager(
      CollectionManager collectionManager, FileManager fileManager, Console console) {
    commandList = new LinkedHashMap<>();
    commandList.put("help", new HelpCommand(collectionManager, console));
    commandList.put("info", new InfoCommand(collectionManager));
    commandList.put("show", new ShowCommand(collectionManager));
    commandList.put(
        "add",
        new AddCommand(collectionManager, console.getScriptManager(), console.getScannerManager()));
    commandList.put(
        "update",
        new UpdateCommand(
            collectionManager, console.getScriptManager(), console.getScannerManager()));
    commandList.put("remove_by_id", new RemoveByIdCommand(collectionManager));
    commandList.put("clear", new ClearCommand(collectionManager));
    commandList.put("save", new SaveCommand(fileManager, collectionManager));
    commandList.put(
        "execute_script", new ExecuteScriptCommand(collectionManager, fileManager, console));
    commandList.put("exit", new ExitCommand(console));
    commandList.put("remove_head", new RemoveHeadCommand(collectionManager));
    commandList.put(
        "remove_lower",
        new RemoveLowerCommand(
            collectionManager, console.getScriptManager(), console.getScannerManager()));
    commandList.put("max_by_creation_date", new MaxByCreationDateCommand(collectionManager));
    commandList.put("filter_by_type", new FilterByTypeCommand(collectionManager));
    commandList.put(
        "add_if_max",
        new AddIfMaxCommand(
            collectionManager, console.getScriptManager(), console.getScannerManager()));
    commandList.put("average_of_price", new AverageOfPriceCommand(collectionManager));
  }

  /**
   * Начинает исполнять заданную команду {@link Command}.
   *
   * @param line команда с аргументами.
   * @see Command
   * @throws UnknownCommandException если заданной команды не существует.
   * @author Alvas
   * @since 1.0
   */
  public void startExecuting(String line) throws UnknownCommandException {
    String commandName = line.strip().split(" ")[0];
    if (!commandList.containsKey(commandName)) {
      throw new UnknownCommandException(commandName);
    }
    Command command = commandList.get(commandName);
    try {
      command.execute(line.strip().trim().split("\\s+"));
    } catch (CommandExecuteException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Возвращает все существующие команды в виде {@link LinkedHashMap}.
   *
   * @return Все существующие команды.
   * @see LinkedHashMap
   * @author Alvas
   * @since 1.0
   */
  public HashMap<String, Command> getCommandList() {
    return commandList;
  }
}
