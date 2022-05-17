package com.gmail.valvol98.data;

/**
 * Unchange data.
 *
 * @author V.Volovshchykov
 *
 */
public final class UnChangeData {

    public static final int NUMBER_ACCOUNTS_PER_PAGE = 2;
    public static int ROW_SINCE_FIND_LIMITED_ACCOUNTS_COLLECTION = 0;

    public static final String PAGE_INDEX = "index.jsp";
    public static final String PAGE_ERROR = "WEB-INF/jsp/error_page.jsp";
    public static final String PAGE_USER = "/WEB-INF/jsp/user.jsp";
    public static final String PAGE_FOREMAN = "/WEB-INF/jsp/foreman.jsp";
    public static final String PAGE_MANAGER = "/WEB-INF/jsp/manager.jsp";

    public static final String ROLE_MANAGER = "менеджер";
    public static final String ROLE_USER = "пользователь";
    public static final String ROLE_FOREMAN = "мастер";

    public static final String MESSAGE_ORDER_STATUS_NOT_SUMBIT_COST_BY_MANAGER = "Ожидает подтверждения менеджера";
    public static final String MESSAGE_ORDER_STATUS_NOT_SUBMIT_FOREMANE_BY_MANAGER = "Ожидает запуска в работу мастером";

    public static final String ORDER_IN_WORK = "в работе";
    public static final String ORDER_COMPLETED = "выполнено";
    public static final String ORDER_WAIT_FOR_PAY = "ожидает на оплату";
    public static final String ORDER_PAID = "оплачено";
    public static final String ORDER_REFUSE = "отменено";
    public static final String ORDER_FINAL = "final";

    public static final String COMMAND_MANAGER_START_PAGE = "controller?command=managerStartPage";
    public static final String COMMAND_USER_START_PAGE = "controller?command=userStartPage";
    public static final String COMMAND_FOREMAN_START_PAGE = "controller?command=foremanStartPage";
    //public static final String COMMAND_ERROR_PAGE = "controller?command=noCommand";
}
