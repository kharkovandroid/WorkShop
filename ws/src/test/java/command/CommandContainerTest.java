package command;

import com.gmail.valvol98.command.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CommandContainerTest {

    @Test
    public void testExecuteCommandContainerLoginCommand() {
        String commandName = "login";

        Map<String, Command> commands = new HashMap<>();
        commands.put(commandName, new LoginCommand());

        Command command = CommandContainer.getCommand(commandName);

        assertEquals(commands.get(commandName).getClass(), command.getClass());
    }

    @Test
    public void testExecuteCommandContainerNullCommand() {
        String commandName = null;

        Map<String, Command> commands = new HashMap<>();
        commands.put(commandName, new NoCommand());

        Command command = CommandContainer.getCommand(commandName);

        assertEquals(commands.get(commandName).getClass(), command.getClass());
    }
}
