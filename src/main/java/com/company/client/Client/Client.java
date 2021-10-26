package com.company.client.Client;

import com.company.client.validation.CommandMethods;
import com.company.client.validation.InputDevice;
import com.company.common.collection_objects.StudyGroup;

import java.io.IOException;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {
    public final CommandSender commandSender;

    public Client(InetAddress addr, int port) throws IOException {
        commandSender = new CommandSender(addr, port);
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client(InetAddress.getLocalHost(), 1221);
        client.start();
    }

    public void start() throws IOException {
        try {
            System.out.println("Готов начать работу, уважаемый пекарь");
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine().trim();


            String reply;
            while (!line.equals("exit")) {

                try {
                    Pattern pCommand = Pattern.compile("([a-zA-Zа-яА-ЯёЁ_]*)[\\s-]?");
                    Pattern pArgs = Pattern.compile("(\\s-.*)$");
                    Matcher cmd = pCommand.matcher(line);
                    cmd.find();
                    String commandName = cmd.group(0).trim();
                    Matcher args = pArgs.matcher(line);
                    boolean hasArgs = args.find();
                    InputDevice inputDevice = new InputDevice();
                    if (!hasArgs || commandName.equals("add") || commandName.equals("update") || commandName.equals("execute_script")) {

                        StudyGroup studyGroup;
                        CommandMethods device = new CommandMethods();
                        switch (commandName) {
                            case "add":
                                if (hasArgs) {
                                    studyGroup = inputDevice.addFromFile(line);
                                } else {
                                    studyGroup = inputDevice.add();
                                }
                                if (studyGroup != null) {
                                    commandSender.send(commandName + "\n", studyGroup, this.commandSender.getSocketAddress());
                                }
                                break;
                            case "update":
                                if (hasArgs) {
                                    studyGroup = inputDevice.updateFromFile(line);
                                } else {
                                    studyGroup = inputDevice.update();
                                }
                                if (studyGroup != null) {
                                    commandSender.send(commandName + "\n", studyGroup, this.commandSender.getSocketAddress());

                                }
                                break;
                            case "filter_by_semester_enum":
                                int FBS;
                                try {
                                    FBS = device.readFilterSem();
                                } catch (InputMismatchException e) {
                                    System.out.println("Вы ввели какую-то чушь. Я для вас какая-то шутка?");
                                    break;
                                }
                                commandSender.send(commandName + "\n", String.valueOf(FBS), this.commandSender.getSocketAddress());
                                break;
                            case "execute_script":
                                ScriptExecute scriptExecute = new ScriptExecute(this);
                                if (hasArgs) {
                                    scriptExecute.execute(line);
                                } else {
                                    scriptExecute.execute(null);
                                }
                                break;
                            case "remove_by_id":
                            case "remove_lower":
                            case "remove_higher":
                                int removeById = device.removeById();
                                commandSender.send(commandName + "\n", String.valueOf(removeById), this.commandSender.getSocketAddress());
                                break;

                            default:
                                commandSender.send(commandName + "\n", this.commandSender.getSocketAddress());
                                break;
                        }
                    } else {

                            String commandArgs = args.group(0);
                            commandSender.send(commandName, commandArgs, this.commandSender.getSocketAddress());
                        }
                    if (!commandName.equals("execute_script")) {
                        reply = commandSender.receive();
                        if (reply != null) {
                            System.out.println(reply);
                            //    System.out.println("Сервер временно недоступен, уважаемый пекарь( Кажется, кто-то не заплатил за интернет.");
                        }
                    }
                } catch (PortUnreachableException e) {
                    System.out.println("Сервер недоступен");
                }
                line = scanner.nextLine();
            }
                commandSender.send("save", this.commandSender.getSocketAddress());
        } catch (NoSuchElementException e) {
            System.out.println("Вы ввели что-то нехорошее, прощайте.");
        } catch (PortUnreachableException e) {
            System.out.println("Сервер стал недостпуным((");
        }
    }
}