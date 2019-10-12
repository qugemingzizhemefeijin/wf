package cg.zz.wf.core.log;

import org.slf4j.Marker;

/**
 * 
 * Log接口
 * 
 * @author chengang
 *
 */
public interface Log {

	public String getLoggerName();

	public boolean isTraceEnabled();

	public void trace(String paramString);

	public void trace(String paramString, Object paramObject);

	public void trace(String paramString, Object paramObject1, Object paramObject2);

	public void trace(String paramString, Object[] paramArrayOfObject);

	public void trace(String paramString, Throwable paramThrowable);

	public boolean isTraceEnabled(Marker paramMarker);

	public void trace(Marker paramMarker, String paramString);

	public void trace(Marker paramMarker, String paramString, Object paramObject);

	public void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);

	public void trace(Marker paramMarker, String paramString, Object[] paramArrayOfObject);

	public void trace(Marker paramMarker, String paramString, Throwable paramThrowable);

	public boolean isDebugEnabled();

	public void debug(String paramString);

	public void debug(String paramString, Object paramObject);

	public void debug(String paramString, Object paramObject1, Object paramObject2);

	public void debug(String paramString, Object[] paramArrayOfObject);

	public void debug(String paramString, Throwable paramThrowable);

	public boolean isDebugEnabled(Marker paramMarker);

	public void debug(Marker paramMarker, String paramString);

	public void debug(Marker paramMarker, String paramString, Object paramObject);

	public void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);

	public void debug(Marker paramMarker, String paramString, Object[] paramArrayOfObject);

	public void debug(Marker paramMarker, String paramString, Throwable paramThrowable);

	public boolean isInfoEnabled();

	public void info(String paramString);

	public void info(String paramString, Object paramObject);

	public void info(String paramString, Object paramObject1, Object paramObject2);

	public void info(String paramString, Object[] paramArrayOfObject);

	public void info(String paramString, Throwable paramThrowable);

	public boolean isInfoEnabled(Marker paramMarker);

	public void info(Marker paramMarker, String paramString);

	public void info(Marker paramMarker, String paramString, Object paramObject);

	public void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);

	public void info(Marker paramMarker, String paramString, Object[] paramArrayOfObject);

	public void info(Marker paramMarker, String paramString, Throwable paramThrowable);

	public boolean isWarnEnabled();

	public void warn(String paramString);

	public void warn(String paramString, Object paramObject);

	public void warn(String paramString, Object[] paramArrayOfObject);

	public void warn(String paramString, Object paramObject1, Object paramObject2);

	public void warn(String paramString, Throwable paramThrowable);

	public boolean isWarnEnabled(Marker paramMarker);

	public void warn(Marker paramMarker, String paramString);

	public void warn(Marker paramMarker, String paramString, Object paramObject);

	public void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);

	public void warn(Marker paramMarker, String paramString, Object[] paramArrayOfObject);

	public void warn(Marker paramMarker, String paramString, Throwable paramThrowable);

	public boolean isErrorEnabled();

	public void error(String paramString);

	public void error(String paramString, Object paramObject);

	public void error(String paramString, Object paramObject1, Object paramObject2);

	public void error(String paramString, Object[] paramArrayOfObject);

	public void error(String paramString, Throwable paramThrowable);

	public boolean isErrorEnabled(Marker paramMarker);

	public void error(Marker paramMarker, String paramString);

	public void error(Marker paramMarker, String paramString, Object paramObject);

	public void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2);

	public void error(Marker paramMarker, String paramString, Object[] paramArrayOfObject);

	public void error(Marker paramMarker, String paramString, Throwable paramThrowable);

}
