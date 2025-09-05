package com.imtf.activate.admin.domain;

public interface Effect {
    enum CapabilityEffect implements Effect {
        CREATE
    }

    enum CustomerEffect implements Effect {
        CREATE
    }

    enum LicenseEffect implements Effect {
        CREATE, SIGN
    }

    enum InternalLicenseEffect implements Effect {
        SIGN
    }

    enum LoadEffect implements Effect {
        CSV_DOWNLOAD, CSV_UPLOAD
    }
}

