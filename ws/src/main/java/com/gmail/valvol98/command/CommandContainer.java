package com.gmail.valvol98.command;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Holder for all commands.
 *
 * @author V.Volovshchykov
 *
 */
public class CommandContainer {

    private static final Logger log = Logger.getLogger(CommandContainer.class);

    private static final Map<String, Command> commands;

    static {
        commands = new HashMap<>();

        commands.put("login", new LoginCommand());
        commands.put("managerStartPage", new ManagerCommand());
        commands.put("userStartPage", new UserCommand());
        commands.put("foremanStartPage", new ForemanCommand());
        commands.put("logOut", new LogOutCommand());
        commands.put("addOrder", new AddOrderCommand());
        commands.put("addComment", new AddCommentCommand());
        commands.put("changeOrderStatusByForeman", new ChangeOrderStatusByForemanCommand());
        commands.put("addSumToAccount", new AddSumToAccountCommand());
        commands.put("addCostUnSubmittedOrder", new AddCostUnSubmittedOrderCommand());
        commands.put("addForemanUnSubmittedOrder", new AddForemanUnSubmittedOrder());
        commands.put("changeOrderStatusByManager", new ChangeOrderStatusByManagerCommand());
        commands.put("changeLanguage", new ChangeLanguageCommand());
        commands.put("nextAccountPage", new NextAccountPageCommand());
        commands.put("noCommand", new NoCommand());
    }

    /**
     * Returns command object with the given name.
     *
     * @param commandName - Name of the command.
     * @return Command object.
     */
    public static Command getCommand(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            //log.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }
        return commands.get(commandName);
    }
}
