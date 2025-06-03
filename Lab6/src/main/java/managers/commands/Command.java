package managers.commands;

import exceptions.CommandExecuteException;

/**
 * Базовый интерфейс для реализации команд.
 *
 * @author Alvas
 * @since 1.0
 */
public interface Command {
  /**
   * Базовый метод для исполнения команды с заданными параметрами.
   *
   * @param args аргументы команды.
   * @throws CommandExecuteException если указано неверное количество аргументов команды.
   * @author Alvas
   * @since 1.0
   */
  void execute(String[] args) throws CommandExecuteException;

  /**
   * Базовый метод для получения названия команды.
   *
   * @return Название команды.
   */
  String getName();

  /**
   * Базовый метод для получения описания команды.
   *
   * @return Описание команды.
   */
  String getDescription();
}
