package com.imtf.activate.admin.rest.v1.mocks;

import com.imtf.activate.admin.service.keys.KeyService;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;

@Mock
@ApplicationScoped
public class MockKeyService extends KeyService {


    public MockKeyService( ) {
        super(new MockKeyAccess());
    }
}
