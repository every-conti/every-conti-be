package my.everyconti.every_conti.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) return "No stack trace available.";

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);

        return sw.toString();
    }
}