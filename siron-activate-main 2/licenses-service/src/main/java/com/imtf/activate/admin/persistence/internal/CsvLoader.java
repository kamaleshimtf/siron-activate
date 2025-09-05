package com.imtf.activate.admin.persistence.internal;


import com.imtf.activate.admin.domain.Capability;
import com.imtf.activate.admin.domain.Customer;
import com.imtf.activate.admin.domain.License;
import com.imtf.activate.admin.domain.Model;
import com.imtf.activate.admin.persistence.LoaderPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ApplicationScoped
public class CsvLoader implements LoaderPersistence {

    private static final Logger LOGGER = Logger.getLogger(CsvLoader.class);

    private static final String REPLACE_NULL_STRING = "-";

    @ConfigProperty(name = "siron.activate.csv.headers")
    String stringHeaders;

    private List<String> headers = List.of();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Override
    public Model load(String importPath) {
        final List<Capability> capabilities = new ArrayList<>();
        final List<Customer> customers = new ArrayList<>();
        final List<License> licenses = new ArrayList<>();

        try {
            LOGGER.info("Loading CSV file from " + importPath);

            final Reader reader = new FileReader(importPath);
            final Iterable<CSVRecord> records = configureReader().parse(reader);

            for (CSVRecord csvRecord : records) {
                csvRecord.stream().toList().forEach(field -> LOGGER.info("loading field " + field));

                String capability = restoreNull(csvRecord.get("ProductId"));
                String customerId = restoreNull(csvRecord.get("CustomerId"));
                String customerName = restoreNull(csvRecord.get("CustomerName"));
                String creationDate = csvRecord.get("CreationDate");
                String expirationDate = csvRecord.get("ExpirationDate");
                String contact = restoreNull(csvRecord.get("Contact"));

                capabilities.add(new Capability(capability, List.of()));
                customers.add(new Customer(customerId));
                licenses.add(new License(UUID.randomUUID(), capability, customerId, customerName, LocalDate.from(formatter.parse(creationDate)), LocalDate.from(formatter.parse(expirationDate)), contact));

            }

        } catch (FileNotFoundException e) {
            LOGGER.error("Error when trying to load CSV file from " + importPath, e);
            throw new RuntimeException(e);
        } catch (IOException e) {
            LOGGER.error("Error when trying to load CSV file from " + importPath, e);
            throw new RuntimeException(e);
        }

        return new Model(capabilities, customers, licenses);
    }

    @Override
    public void unload(String exportPath, Model model) {
        try (final CSVPrinter printer = new CSVPrinter(new FileWriter(exportPath), configureWriter())) {

            for (License license : model.licenses()) {
                printer.printRecord(
                        escapeNull(license.productName()),
                        escapeNull(license.customer()),
                        escapeNull(license.information()),
                        license.creationDate().format(formatter),
                        license.expirationDate().format(formatter),
                        escapeNull(license.contactEmail())
                );
            }

        } catch (IOException e) {
            LOGGER.error("Error when trying to create CSV file from " + exportPath, e);
            throw new RuntimeException(e);
        }
    }

    private CSVFormat configureReader() {
        headers = Arrays.asList(stringHeaders.split(","));
        return CSVFormat.DEFAULT.builder()
                .setHeader(headers.toArray(new String[0]))
                .setSkipHeaderRecord(true)
                .build();
    }

    private CSVFormat configureWriter() {
        headers = Arrays.asList(stringHeaders.split(","));
        return CSVFormat.DEFAULT.builder()
                .setHeader(headers.toArray(new String[0]))
                .build();
    }

    private String escapeNull(String string) {
        return Objects.requireNonNullElse(string, REPLACE_NULL_STRING);
    }

    private String restoreNull(String string) {
        if (string != null && string.equals(REPLACE_NULL_STRING)) {
            return null;
        } else {
            return string;
        }
    }
}
