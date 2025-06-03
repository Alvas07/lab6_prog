package managers.commands;

import data.Ticket;
import exceptions.CommandExecuteException;
import exceptions.RemoveException;
import managers.CollectionManager;

/**
 * Класс, отвечающий за команду "remove_head".
 *
 * <p>Описание команды: "Вывести первый элемент {@link Ticket} коллекции и удалить его".
 *
 * <p>Не принимает входных аргументов.
 *
 * @see Command
 * @see Ticket
 * @author Alvas
 * @since 1.0
 */
public class RemoveHeadCommand implements Command {
  private final CollectionManager collectionManager;

  /**
   * Конструктор команды.
   *
   * @param collectionManager менеджер коллекции.
   * @see CollectionManager
   * @author Alvas
   * @since 1.0
   */
  public RemoveHeadCommand(CollectionManager collectionManager) {
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
      Ticket head = collectionManager.removeHead();
      System.out.println("ПЕРВЫЙ ЭЛЕМЕНТ КОЛЛЕКЦИИ:");
      System.out.println(head);
    } catch (RemoveException e) {
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
    return "remove_head";
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
    return "вывести первый элемент коллекции и удалить его";
  }
}
