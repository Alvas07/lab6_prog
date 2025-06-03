package managers.commands;

import data.Ticket;
import exceptions.CommandExecuteException;
import exceptions.EmptyCollectionException;
import managers.CollectionManager;

/**
 * Класс, отвечающий за команду "max_by_creation_date".
 *
 * <p>Описание команды: "Вывести максимальный по полю {@code creationDate} элемент {@link Ticket}
 * коллекции".
 *
 * <p>Не принимает входных аргументов.
 *
 * @see Command
 * @see Ticket
 * @author Alvas
 * @since 1.0
 */
public class MaxByCreationDateCommand implements Command {
  private final CollectionManager collectionManager;

  /**
   * Конструктор команды.
   *
   * @param collectionManager менеджер коллекции.
   * @see CollectionManager
   * @author Alvas
   * @since 1.0
   */
  public MaxByCreationDateCommand(CollectionManager collectionManager) {
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

    try {
      System.out.println("МАКСИМАЛЬНЫЙ ЭЛЕМЕНТ ПО ДАТЕ СОЗДАНИЯ:");
      System.out.println(collectionManager.getMaxByDate());
    } catch (EmptyCollectionException e) {
      System.out.println(e.getMessage());
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
    return "max_by_creation_date";
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
    return "вывести максимальный по creationDate элемент коллекции";
  }
}
