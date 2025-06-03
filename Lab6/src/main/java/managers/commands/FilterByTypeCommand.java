package managers.commands;

import data.Ticket;
import data.TicketType;
import exceptions.CommandExecuteException;
import java.util.List;
import managers.CollectionManager;

/**
 * Класс, отвечающий за команду "filter_by_type".
 *
 * <p>Описание команды: "Вывести элементы {@link Ticket} с заданным значением {@code type}".
 *
 * <p>Принимает на вход один обязательный аргумент - тип билета (тип {@link TicketType}). Регистр не
 * имеет значения.
 *
 * @see Command
 * @see Ticket
 * @see TicketType
 * @author Alvas
 * @since 1.0
 */
public class FilterByTypeCommand implements Command {
  private final CollectionManager collectionManager;

  /**
   * Конструктор команды.
   *
   * @param collectionManager менеджер коллекции.
   * @see CollectionManager
   * @author Alvas
   * @since 1.0
   */
  public FilterByTypeCommand(CollectionManager collectionManager) {
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
    if (args.length != 2) {
      throw new CommandExecuteException("Команда принимает один обязательный аргумент.");
    }

    try {
      TicketType type = TicketType.valueOf(args[1].toUpperCase());
      List<Ticket> filteredTickets = collectionManager.getFilteredByType(type);
      if (filteredTickets.isEmpty()) {
        System.out.println("Элементов, соответствующих данному типу, не найдено.");
      } else {
        System.out.println("ЭЛЕМЕНТЫ С ТИПОМ БИЛЕТА " + type.name().toUpperCase() + ":");
        filteredTickets.forEach(System.out::println);
      }
    } catch (IllegalArgumentException e) {
      System.out.println("Такого типа билета не существует.");
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
    return "filter_by_type";
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
    return "вывести элементы с заданным значением type";
  }
}
