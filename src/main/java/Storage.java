import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Storage {
    private static final String DATA_DIRECTORY = "./data/";
    private static final String CSV_FORMAT = ".csv";

    private final String fileName;

    public Storage(String fileName) {
        this.fileName = DATA_DIRECTORY + fileName + CSV_FORMAT;
    }

    public Storage(String fileName, String fileFormat) {
        this.fileName = DATA_DIRECTORY + fileName + fileFormat;
    }

    //Insert a new row
    public void insert(String[] strings) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.join(",", strings));
            writer.newLine();
        }
    }

    //Delete row at index
    public void delete(int index) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException("Invalid row index");
        }
        lines.remove(index);
        Files.write(Paths.get(fileName), lines);
    }

    public void clear() throws IOException {
        Files.write(Paths.get(fileName), new ArrayList<>());
    }

    //Edit row at index
    public void edit(int index, String[] strings) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException("Invalid row index");
        }
        lines.set(index, String.join(",", strings));
        Files.write(Paths.get(fileName), lines);
    }

    public String[] read() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        return lines.toArray(new String[lines.size()]);
    }
}
