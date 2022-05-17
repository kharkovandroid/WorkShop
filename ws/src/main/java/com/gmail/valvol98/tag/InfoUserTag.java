package com.gmail.valvol98.tag;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.StringWriter;
import java.util.Locale;
import javax.servlet.jsp.JspException;
import java.util.GregorianCalendar;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;

public class InfoUserTag extends SimpleTagSupport {

    private String userName;
    private String userRole;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void doTag() throws IOException {
        JspWriter out = getJspContext().getOut();
        out.println(userName + "<br>" + userRole + "<br>");
    }
}
