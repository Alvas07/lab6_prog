package managers.commands;

import exceptions.CommandExecuteException;
import exceptions.FileWriteException;
import managers.CollectionManager;
import managers.FileManager;

/**
 * Класс, отвечающий за команду "save".
 *
 * <p>Описание команды: "Сохранить коллекцию в исходный файл".
 *
 * <p>Не принимает входных аргументов.
 *
 * @see Command
 * @author Alvas
 * @since 1.0
 */
public class SaveCommand implements Command {
  private final FileManager fileManager;
  private final CollectionManager collectionManager;

  /**
   * Конструктор команды.
   *
   * @param fileManager файловый менеджер.
   * @param collectionManager менеджер коллекции.
   * @see FileManager
   * @see CollectionManager
   * @author Alvas
   * @since 2.0
   */
  public SaveCommand(FileManager fileManager, CollectionManager collectionManager) {
    this.fileManager = fileManager;
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
      fileManager.saveCollectionToXml(collectionManager);
      System.out.println("Коллекция сохранена в файл " + fileManager.getFileName());
    } catch (FileWriteException e) {
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
    return "save";
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
    return "сохранить коллекцию в файл";
  }
}
