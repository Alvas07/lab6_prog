package managers.commands;

import data.Ticket;
import exceptions.CommandExecuteException;
import exceptions.ObjectCreationException;
import exceptions.RemoveException;
import managers.CollectionManager;
import managers.ScannerManager;
import managers.ScriptManager;
import utils.generators.TicketGenerator;

/**
 * Класс, отвечающий за команду "remove_lower".
 *
 * <p>Описание команды: "Удалить из коллекции все элементы {@link Ticket}, меньшие заданного".
 *
 * <p>Не принимает входных аргументов. Вызывает {@link TicketGenerator}, запрашивающий входные
 * данные для создания элемента, с которым происходит сравнение.
 *
 * @see Command
 * @see Ticket
 * @see TicketGenerator
 * @author Alvas
 * @since 1.0
 */
public class RemoveLowerCommand implements Command {
  private final CollectionManager collectionManager;
  private final ScriptManager scriptManager;
  private final ScannerManager scannerManager;

  /**
   * Конструктор команды.
   *
   * @param collectionManager менеджер коллекции.
   * @param scriptManager менеджер выполнения скриптов.
   * @param scannerManager менеджер сканеров.
   * @see CollectionManager
   * @see ScriptManager
   * @see ScannerManager
   * @author Alvas
   * @since 2.0
   */
  public RemoveLowerCommand(
      CollectionManager collectionManager,
      ScriptManager scriptManager,
      ScannerManager scannerManager) {
    this.collectionManager = collectionManager;
    this.scriptManager = scriptManager;
    this.scannerManager = scannerManager;
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

    int size = collectionManager.getCollectionSize();
    try {
      Ticket ticket =
          new TicketGenerator(collectionManager.getIdManager(), scriptManager, scannerManager)
              .create();
      collectionManager.removeLower(ticket);
      System.out.println(
          "Удалено "
              + (size - collectionManager.getCollectionSize())
              + " элементов, меньших заданного.");
    } catch (RemoveException | ObjectCreationException e) {
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
    return "remove_lower";
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
    return "удалить из коллекции все элементы, меньшие заданного";
  }
}
