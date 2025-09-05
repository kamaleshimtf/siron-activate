package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.*;
import com.imtf.activate.admin.persistence.internal.CsvLoader;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LoaderService {

    private static final String RELATIVE_TEMP_DIR = "siron-activate-temp";

    @ConfigProperty(name = "siron.activate.csv.importer.file")
    String importerPath;

    @ConfigProperty(name = "siron.activate.csv.exporter.file")
    String exporterPath;

    private final CsvLoader csvLoader;

    private final CapabilityService capabilityService;

    private final CustomerService customerService;

    private final LicenseService licenseService;

    private final AuditService auditService;


    @Inject
    public LoaderService(
            CsvLoader csvLoader,
            CapabilityService capabilityService,
            CustomerService customerService,
            LicenseService licenseService,
            AuditService auditService) {
        this.csvLoader = csvLoader;
        this.capabilityService = capabilityService;
        this.customerService = customerService;
        this.licenseService = licenseService;
        this.auditService = auditService;
    }

    public void load() {
        // TODO handle properly load
        Model model = csvLoader.load(importerPath);

        for (Capability capability : model.capabilities()) {
            if (!capabilityService.getAll().stream().map(Capability::name).toList().contains(capability.name())) {
                capabilityService.create(capability);
            }
        }

        for (Customer customer : model.customers()) {
            if (!customerService.getAll().stream().map(Customer::name).toList().contains(customer.name())) {
                customerService.create(customer);
            }
        }

        //TODO handle properly duplicates
        model.licenses().forEach(licenseService::create);
    }

    public Path download() {
        try {

            List<License> licenses = licenseService.getAll();

            String filename = UUID.randomUUID().toString();
            Path tempFile = Path.of(Files.createTempDirectory(RELATIVE_TEMP_DIR).toString(), filename);

            csvLoader.unload(tempFile.toString(), new Model(licenses));
            auditService.log(new Action(ActionScope.LOADER, Effect.LoadEffect.CSV_DOWNLOAD));

            return tempFile;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void unload() {
        List<License> licenses = licenseService.getAll();
        csvLoader.unload(exporterPath, new Model(licenses));
    }
}
