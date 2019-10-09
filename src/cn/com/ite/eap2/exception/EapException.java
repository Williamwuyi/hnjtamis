package cn.com.ite.eap2.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>Title cn.com.ite.eap2.EapException</p>
 * <p>Description EAP异常</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 19, 2014 9:26:21 AM
 * @version 2.0
 * 
 * @modified records:
 */
public class EapException extends Exception{
	private static final long serialVersionUID = -1624858763414911638L;
	/**
	 * 异常根
	 */
	protected Throwable rootCause=null;
	/**
	 * 异常数组
	 */
	@SuppressWarnings("unchecked")
	private List exceptions=new ArrayList();
	/**
	 * 窗口类型
	 */
    private String windowType;
    /**
     * 返回地址
     */
    private String forwardPath;
    /**
     * 消息键
     */
    private String messageKey=null;
    /**
     * 消息参数
     */
    private Object[] messageArgs=null;

    public Object[] getMessageArgs() {
		return messageArgs;
	}

	public void setMessageArgs(Object[] messageArgs) {
		this.messageArgs = messageArgs;
	}

	public String getMessageKey() {
		return messageKey;
	}
	
	public String getMessage()
    {
		if(rootCause instanceof java.sql.SQLException){
			java.sql.SQLException sthis=(java.sql.SQLException)rootCause;
			return ErrorContext.getErrorInfo(sthis.getErrorCode());
		}
        return super.getMessage();
    }

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}

	public EapException(String strMessage)
    {
        super(strMessage);
    }

    public EapException(Throwable e)
    {
        super(e);
        rootCause = e;
    }

    public EapException(Throwable e, String message)
    {
        super(message);
        rootCause = e;
    }

    public EapException(Throwable e, String message, String forwardPath)
    {
        super(message);
        rootCause = e;
        this.forwardPath = forwardPath;
    }

    public EapException()
    {
    }

    public String getWindowType()
    {
        return windowType;
    }

    public void setWindowType(String windowType)
    {
        this.windowType = windowType;
    }

    public String getForwardPath()
    {
        return forwardPath;
    }

    public void setForwardPath(String forwardPath)
    {
        this.forwardPath = forwardPath;
    }

    public String getTypeDiscription()
    {
        return "\u51FA\u73B0\u7CFB\u7EDF\u6CA1\u6709\u660E\u786E\u5B9A\u4E49\u7684\u5F02\u5E38\uFF0C";
    }

	public Throwable getRootCause() {
		return rootCause;
	}

	public void setRootCause(Throwable rootCause) {
		this.rootCause = rootCause;
	}

	@SuppressWarnings("unchecked")
	public List getExceptions() {
		return exceptions;
	}
	@SuppressWarnings("unchecked")
	public void addException(EapException ex){
		exceptions.add(ex);
	}
}
