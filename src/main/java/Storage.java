import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles storage and retrieval of data from mainly csv files.
 *
 * This class provides basic CRUD operations on a file stored in the ./data/ directory.
 * The default file format is .csv for storage files.
 */
public class Storage {

    /** Directory where all data files are stored. */
    private static final String DATA_DIRECTORY = "./data/";

    /** Default file format used for storage files. */
    private static final String CSV_FORMAT = ".csv";

    /** Full path to the file being managed by this Storage instance. */
    private final String fileName;

    /**
     * Creates a storage object using the default CSV file format.
     *
     * @param fileName Base name of the file (without extension).
     */
    public Storage(String fileName) {
        this.fileName = DATA_DIRECTORY + fileName + CSV_FORMAT;
    }

    /**
     * Creates a storage object using a custom file format.
     *
     * @param fileName   Base name of the file.
     * @param fileFormat File extension.
     */
    public Storage(String fileName, String fileFormat) {
        this.fileName = DATA_DIRECTORY + fileName + fileFormat;
    }

    /**
     * Appends a new row to the end of the file.
     *
     * @param strings Array of values to be written as a row.
     * @throws IOException If an I/O error occurs while writing.
     */
    public void insert(String[] strings) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(String.join(",", strings));
            writer.newLine();
        }
    }

    /**
     * Deletes the row at the specified index from the file.
     *
     * @param index Index of the row to delete.
     * @throws IOException If an I/O error occurs while reading or writing.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
    public void delete(int index) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException("Invalid row index");
        }
        lines.remove(index);
        Files.write(Paths.get(fileName), lines);
    }

    /**
     * Removes all rows from the file.
     *
     * @throws IOException If an I/O error occurs while clearing the file.
     */
    public void clear() throws IOException {
        Files.write(Paths.get(fileName), new ArrayList<>());
    }

    /**
     * Replaces the row at the specified index with new data.
     *
     * @param index   Index of the row to edit.
     * @param strings New row data as an array of values.
     * @throws IOException If an I/O error occurs while reading or writing.
     * @throws IndexOutOfBoundsException If the index is invalid.
     */
    public void edit(int index, String[] strings) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException("Invalid row index");
        }
        lines.set(index, String.join(",", strings));
        Files.write(Paths.get(fileName), lines);
    }

    /**
     * Reads all rows from the file.
     *
     * @return Array of lines read from the file.
     * @throws IOException If an I/O error occurs while reading.
     */
    public String[] read() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        return lines.toArray(new String[lines.size()]);
    }
}