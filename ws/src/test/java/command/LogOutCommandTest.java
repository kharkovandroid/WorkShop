package command;

import com.gmail.valvol98.command.LogOutCommand;
import com.gmail.valvol98.data.UnChangeData;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LogOutCommandTest {

    @Test
    public void testExecuteLoginOutCommand() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);

        when(req.getSession(false))
                .thenReturn(session);

        LogOutCommand command = new LogOutCommand();
        String address = command.execute(req, null);

        assertEquals(UnChangeData.PAGE_INDEX, address);
    }
}
