package command;

import com.gmail.valvol98.command.NextAccountPageCommand;
import com.gmail.valvol98.data.UnChangeData;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NextAccountPageCommandTest {

    @Test
    public void testExecuteNextAccountPageCommand() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        int startRow = 2;
        when(req.getParameter("startRow"))
                .thenReturn(String.valueOf(startRow));

        NextAccountPageCommand nextAccountPageCommand = new NextAccountPageCommand();
        String address = nextAccountPageCommand.execute(req, null);

        assertEquals(UnChangeData.COMMAND_USER_START_PAGE, address);
    }
}
