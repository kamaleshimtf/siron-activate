package com.imtf.activate.admin.persistence;

import com.imtf.activate.admin.domain.Model;

public interface LoaderPersistence {

    Model load(String importPath);

    void unload(String exportPath, Model model);
}

