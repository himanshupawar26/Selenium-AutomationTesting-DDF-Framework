package com.himanshupawar.ui.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Reads a header-first CSV classpath resource into TestNG DataProvider rows.
 * Replaces the old framework's Apache POI / Excel reader: the test data is
 * now plain text, readable and diffable in a PR instead of a binary .xlsx
 * blob, with no change in what's being demonstrated — each row is still an
 * independent, named test case fed into a {@code @DataProvider}.
 */
public final class CsvDataReader {

    private CsvDataReader() {
    }

    /**
     * @param classpathResource e.g. "testdata/login-data.csv"
     * @return one {@code Object[]} per data row, each wrapping a single
     *         {@code Map<String, String>} keyed by the CSV header row
     */
    public static Object[][] readAsMaps(String classpathResource) {
        try (InputStream in = CsvDataReader.class.getClassLoader().getResourceAsStream(classpathResource)) {
            if (in == null) {
                throw new IllegalArgumentException("Test data file not found on classpath: " + classpathResource);
            }
            try (CSVParser parser = CSVParser.parse(
                    new InputStreamReader(in, StandardCharsets.UTF_8),
                    CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
                List<CSVRecord> records = parser.getRecords();
                Object[][] rows = new Object[records.size()][1];
                for (int i = 0; i < records.size(); i++) {
                    CSVRecord record = records.get(i);
                    Map<String, String> row = new LinkedHashMap<>();
                    record.toMap().forEach(row::put);
                    rows[i][0] = row;
                }
                return rows;
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read test data file: " + classpathResource, e);
        }
    }
}
