package client;

import common.exceptions.CommandExecuteException;
import common.exceptions.UnknownCommandException;
import common.managers.*;
import common.network.ObjectEncoder;
import common.network.Request;
import common.network.Response;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class UDPClient implements ClientControl {
  private final int BUFFER_SIZE = 65535;
  private final int TIMEOUT_MS = 10000;
  private final InetSocketAddress serverAddress;
  private boolean isRunning = true;
  private final CommandManager commandManager;
  private final ScriptManager scriptManager;
  private boolean limitFlag = true;

  public UDPClient(
      String host, int port, CommandManager commandManager, ScriptManager scriptManager)
      throws IOException {
    this.serverAddress = new InetSocketAddress(host, port);
    this.commandManager = commandManager;
    this.scriptManager = scriptManager;
  }

  public void runClient() {
    try (DatagramChannel channel = DatagramChannel.open();
        Selector selector = Selector.open()) {
      channel.configureBlocking(false);
      channel.register(selector, SelectionKey.OP_READ);
      System.out.println("[CLIENT] Установлено подключение к серверу: " + serverAddress);
      spinLoop(channel, selector);
    } catch (IOException e) {
      System.err.println("[CLIENT] Ошибка при подключении к серверу.");
    }
  }

  private void sendRequest(Request request, DatagramChannel channel, Selector selector)
      throws IOException {
    try {
      ByteBuffer sendBuffer = ObjectEncoder.encodeObject(request);
      channel.send(sendBuffer, serverAddress);

      if (selector.select(100) > 0) {
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {
          SelectionKey key = keys.next();
          keys.remove();

          if (key.isReadable()) {
            ByteBuffer receiveBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
              SocketAddress responseAddress = channel.receive(receiveBuffer);
              if (responseAddress != null) {
                receiveBuffer.flip();
                ByteArrayInputStream bais =
                    new ByteArrayInputStream(receiveBuffer.array(), 0, receiveBuffer.limit());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Response response = (Response) ois.readObject();

                System.out.println("[CLIENT] Ответ: " + response.getMessage());

                if (response.getTickets() != null && !response.getTickets().isEmpty()) {
                  response.getTickets().forEach(System.out::println);
                }

                if (response.isStopFlag()) {
                  stopClient();
                }
                limitFlag = false;
                break;
              }
            }
          }
        }
      }

      if (limitFlag) {
        System.out.println("[CLIENT] Превышено время ожидания ответа от сервера.");
      }
    } catch (ClassNotFoundException e) {
      System.out.println("[CLIENT] Ошибка при передаче команды: " + e.getMessage());
    }
  }

  private void spinLoop(DatagramChannel channel, Selector selector) {
    Scanner scanner = new Scanner(System.in);

    try {
      while (isRunning) {
        System.out.print("> ");
        String commandLine = scanner.nextLine().trim();
        String[] parts = commandLine.split("\\s+");
        try {
          Request request = null;
          if (parts[0].equals("execute_script")) {
            if (parts.length != 2) {
              System.out.println("[CLIENT] Команда принимает один обязательный аргумент.");
            } else {
              executeScript(parts[1], channel, selector);
            }
          } else {
            request = commandManager.convertInputToCommandRequest(commandLine);
          }

          if (request != null) {
            sendRequest(request, channel, selector);
          }
        } catch (IOException e) {
          stopClient();
        } catch (Exception e) {
          System.err.println("[CLIENT] Непредвиденная ошибка: " + e.getMessage());
        }
      }
    } catch (NoSuchElementException e) {
      System.out.println("[CLIENT] Завершение работы клиента...");
      stopClient();
    }
  }

  private void executeScript(String fileName, DatagramChannel channel, Selector selector) {
    FileManager fileManager = new FileManager(fileName);
    ScannerManager scannerManager = scriptManager.getScannerManager();
    boolean recursionFlag = false;
    if (!fileManager.canRead()) {
      System.err.println("[CLIENT] Невозможно прочитать информацию из файла скрипта.");
    }

    try {
      scriptManager.activateFileMode();
      scriptManager.addPath(fileName);
      Scanner currentScanner;

      while (!scriptManager.getAllScanners().isEmpty()) {
        currentScanner = scriptManager.getLastScanner();
        if (currentScanner.hasNextLine()) {
          scannerManager.setScanner(currentScanner);
        } else {
          scriptManager.removePath();
          scannerManager.setScanner(scriptManager.getLastScanner());
          currentScanner = scriptManager.getLastScanner();
        }

        String input = currentScanner.nextLine();
        String[] commandParts = input.trim().split("\\s+");

        if (commandParts[0].equalsIgnoreCase("execute_script")
            && scriptManager.isRecursive(commandParts[1])) {
          System.err.println(
              "[CLIENT] Обнаружена рекурсия! Отмена скрипта! Повторно вызывается файл "
                  + new File(commandParts[1]).getAbsolutePath());
          recursionFlag = true;
          continue;
        }

        System.out.println("[CLIENT] Выполнение команды " + commandParts[0] + ":");
        try {
          Request request = null;
          request = commandManager.convertInputToCommandRequest(input);

          if (request != null) {
            sendRequest(request, channel, selector);
          }
        } catch (UnknownCommandException | CommandExecuteException | IOException e) {
          System.out.println("[CLIENT] Непредвиденная ошибка: " + e.getMessage());
        } catch (NoSuchElementException e) {
          currentScanner = new Scanner(System.in);
          scannerManager.setScanner(currentScanner);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("[CLIENT] Не удалось найти файл: " + e.getMessage());
    } catch (NoSuchElementException ignored) {
    } finally {
      scriptManager.deactivateFileMode();
      if (!recursionFlag) {
        System.out.println("[CLIENT] Скрипт " + fileName + " выполнен!");
      }
    }
  }

  @Override
  public void stopClient() {
    isRunning = false;
  }
}
