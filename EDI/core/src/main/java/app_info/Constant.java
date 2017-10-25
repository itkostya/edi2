package app_info;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Year;
import java.time.YearMonth;

/*
* Constants and static method's used in application
*
* Created by kostya on 10/20/2016.
*/
public enum Constant {

    @SuppressWarnings("unused")
    INSTANCE;

    public static final int SCENARIO_NUMBER = 100;
    public static final int MINUTES_WITHDRAW_AVAILABLE = 2;
    public static final String BASIC_FILE_PATH = System.getProperty("catalina.home") + File.separator + "files" + File.separator;
    public static final String PATH_YEAR_MONTH = Year.now().getValue() + File.separator + YearMonth.now().getMonthValue() + File.separator;
    public static final int FILE_SIZE_THRESHOLD = 1024 * 1024;          // 1 MB
    public static final int MAX_FILE_SIZE       = 10   * 1024 * 1024;   // 25 MB
    public static final int MAX_REQUEST_SIZE    = 100  * 1024 * 1024;   // 100 MB

    static {

        try {
            Files.createDirectories(Paths.get(BASIC_FILE_PATH+PATH_YEAR_MONTH));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
