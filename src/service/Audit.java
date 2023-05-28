package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Audit {
    private static Audit instance = null;
    private static FileWriter writer;
    private static DateTimeFormatter formatter;

    public void log(String action) throws IOException {
        writer.append(action).append(", ").append(formatter.format(LocalDateTime.now())).append("\n");
        writer.flush();
    }

    private  Audit() throws IOException {
            writer  = new FileWriter("audit.csv");
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss");
    }

    public static Audit getInstance() throws IOException {
        if (instance == null) {
            instance = new Audit();
        }
        return instance;
    }

}
