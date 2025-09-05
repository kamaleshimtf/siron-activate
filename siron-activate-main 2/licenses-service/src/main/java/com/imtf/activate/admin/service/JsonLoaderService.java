package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.Capability;
import com.imtf.activate.admin.domain.Customer;
import com.imtf.activate.admin.domain.License;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class JsonLoaderService {

    private final CapabilityService capabilityService;

    private final CustomerService customerService;

    private final LicenseService licenseService;

    @Inject
    public JsonLoaderService(CapabilityService capabilityService, CustomerService customerService, LicenseService licenseService) {
        this.capabilityService = capabilityService;
        this.customerService = customerService;
        this.licenseService = licenseService;
    }

    public void load(List<License> licenses) {
        Set<String> capabilities = new HashSet<>();
        Set<String> customers = new HashSet<>();

        for (License license : licenses) {
            capabilities.addAll(license.capabilities());
            customers.add(license.customer());
        }

        capabilities.forEach(capability -> capabilityService.create(new Capability(capability, Collections.emptyList())));
        customers.forEach(customer -> customerService.create(new Customer(customer)));
        licenses.forEach(licenseService::create);
    }

    public List<License> download() {
        return licenseService.getAll();
    }
}
