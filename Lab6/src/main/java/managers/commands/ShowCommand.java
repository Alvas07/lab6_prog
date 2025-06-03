package managers.commands;

import data.Ticket;
import exceptions.CommandExecuteException;
import managers.CollectionManager;

/**
 * Класс, отвечающий за команду "show".
 *
 * <p>Описание команды: "Вывести все элементы {@link Ticket} коллекции".
 *
 * <p>Не принимает входных аргументов.
 *
 * @see Command
 * @see Ticket
 * @author Alvas
 * @since 1.0
 */
public class ShowCommand implements Command {
  private final CollectionManager collectionManager;

  /**
   * Конструктор команды.
   *
   * @param collectionManager менеджер коллекции.
   * @see CollectionManager
   * @author Alvas
   * @since 1.0
   */
  public ShowCommand(CollectionManager collectionManager) {
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

    if (collectionManager.getCollectionSize() == 0) {
      System.out.println("Коллекция пуста.");
    } else {
      System.out.println("ЭЛЕМЕНТЫ КОЛЛЕКЦИИ:");
      collectionManager.getCollection().forEach(System.out::println);
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
    return "show";
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
    return "вывести все элементы коллекции";
  }
}
