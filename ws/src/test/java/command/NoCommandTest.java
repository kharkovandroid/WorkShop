package command;

import com.gmail.valvol98.command.NoCommand;
import com.gmail.valvol98.data.UnChangeData;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class NoCommandTest {

    @Test
    public void testExecuteNoCommand() throws ServletException, IOException {
        HttpServletRequest req = mock(HttpServletRequest.class);

        HttpSession session = mock(HttpSession.class);
        when(req.getSession())
                .thenReturn(session);

        NoCommand noCommand = new NoCommand();
        String address = noCommand.execute(req, null);

        assertEquals(UnChangeData.PAGE_ERROR, address);
    }
}
