package com.imtf.activate.admin.domain;

import java.util.List;

public record Model(List<Capability> capabilities, List<Customer> customers, List<License> licenses) {

    public Model(List<License> licenses) {
        this(List.of(), List.of(), licenses);
    }
}
