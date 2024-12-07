package com.mna.springbootsecurity.service.core;

import com.mna.springbootsecurity.base.enums.UploadProcessType;
import com.mna.springbootsecurity.domain.entity.UploadProcess;

public interface UploadProcessService {

    UploadProcess createUploadProcess(String fileName, UploadProcessType type);

    UploadProcess updateUploadProcess(UploadProcess uploadProcess);

    boolean isUploadProcessRunning(UploadProcessType type);
}