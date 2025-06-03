package managers.commands;

import exceptions.CommandExecuteException;
import managers.Console;

/**
 * Класс, отвечающий за команду "exit".
 *
 * <p>Описание команды: "Завершить программу".
 *
 * <p>Не принимает входных аргументов.
 *
 * @see Command
 * @author Alvas
 * @since 1.0
 */
public class ExitCommand implements Command {
  private final Console console;

  /**
   * Конструктор команды.
   *
   * @param console консоль, управляющая работой приложения.
   * @author Alvas
   * @since 2.0
   */
  public ExitCommand(Console console) {
    this.console = console;
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

    System.out.println("Завершение работы программы.");
    console.stop();
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
    return "exit";
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
    return "завершить программу";
  }
}
