package managers.commands;

import exceptions.CommandExecuteException;
import managers.CollectionManager;

/**
 * Класс, отвечающий за команду "info".
 *
 * <p>Описание команды: "Вывести информацию о коллекции".
 *
 * <p>Не принимает входных аргументов.
 *
 * @see Command
 * @author Alvas
 * @since 1.0
 */
public class InfoCommand implements Command {
  private final CollectionManager collectionManager;

  /**
   * Конструктор команды.
   *
   * @param collectionManager менеджер коллекции.
   * @see CollectionManager
   * @author Alvas
   * @since 1.0
   */
  public InfoCommand(CollectionManager collectionManager) {
    this.collectionManager = collectionManager;
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
    if (args.length != 1) {
      throw new CommandExecuteException("Команда не принимает аргументы.");
    }

    System.out.println("ИНФОРМАЦИЯ О КОЛЛЕКЦИИ");
    System.out.println(
        "Тип коллекции: " + collectionManager.getCollection().getClass().getSimpleName());
    System.out.println("Количество элементов: " + collectionManager.getCollectionSize());
    System.out.println("Дата инициализации: " + collectionManager.getInitializationTime());
    System.out.println("Дата последнего изменения: " + collectionManager.getLastUpdateTime());
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
    return "info";
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
    return "вывести информацию о коллекции";
  }
}
