package her.m35;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles storage and retrieval of data from mainly csv files.
 * This class provides basic CRUD operations on a file stored in the ./data/ directory.
 * The default file format is .csv for storage files.
 */
public class Storage {

    /** Name of the file being managed by this Storage instance. */
    private final String fileName;

    /** Full path of the file being managed by this Storage instance. */
    private final Path filePath;

    /**
     * Creates a storage object using the default CSV file format.
     *
     * @param fileName Base name of the file (without extension).
     */
    public Storage(String fileName) throws IOException {
        Files.createDirectories(Paths.get("data"));
        this.fileName = "data/" + fileName;
        File file = new File(this.fileName);
        file.createNewFile();
        this.filePath = Paths.get(this.fileName);
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
        List<String> lines = Files.readAllLines(filePath);
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException("Invalid row index");
        }
        lines.remove(index);
        Files.write(filePath, lines);
    }

    /**
     * Removes all rows from the file.
     *
     * @throws IOException If an I/O error occurs while clearing the file.
     */
    public void clear() throws IOException {
        Files.write(filePath, new ArrayList<>());
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
        List<String> lines = Files.readAllLines(filePath);
        if (index < 0 || index >= lines.size()) {
            throw new IndexOutOfBoundsException("Invalid row index");
        }
        lines.set(index, String.join(",", strings));
        Files.write(filePath, lines);
    }

    /**
     * Executes an edit function and returns the provided error message if method throws an exception.
     *
     * @param index Index of the row to edit.
     * @param strings New row data as an array of values.
     * @param errorMessage Error message to return if exception thrown.
     * @return Empty string only if no exception thrown, else the provided error message is returned.
     */
    public String edit(int index, String[] strings, String errorMessage) {
        try {
            edit(index, strings);
            return "";
        } catch (IOException e) {
            return errorMessage + "\nCause: " + e.getMessage();
        }
    }

    /**
     * Reads all rows from the file.
     *
     * @return Array of lines read from the file.
     * @throws IOException If an I/O error occurs while reading.
     */
    public String[] read() throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        return lines.toArray(new String[0]);
    }
}
