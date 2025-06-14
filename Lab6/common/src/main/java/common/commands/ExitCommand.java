package common.commands;

import common.exceptions.CommandExecuteException;
import common.network.Request;
import common.network.RequestBody;
import common.network.Response;

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
  @Override
  public Response execute(Request request) {
    return new Response("Завершение работы программы...", true);
  }

  @Override
  public RequestBody packageBody(String[] args) throws CommandExecuteException {
    if (args.length != 0) {
      throw new CommandExecuteException("Команда не принимает аргументы.");
    }

    return new RequestBody(args);
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
