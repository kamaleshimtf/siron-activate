package com.imtf.activate.admin.persistence;

import com.imtf.activate.admin.domain.Action;

import java.util.List;

public interface AuditPersistence {

    void log(Action action);

    List<Action> getAll();
}
