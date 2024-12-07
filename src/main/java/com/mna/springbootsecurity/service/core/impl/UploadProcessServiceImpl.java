package com.mna.springbootsecurity.service.core.impl;

import com.mna.springbootsecurity.base.enums.UploadProcessState;
import com.mna.springbootsecurity.base.enums.UploadProcessType;
import com.mna.springbootsecurity.domain.dao.UploadProcessDao;
import com.mna.springbootsecurity.domain.entity.UploadProcess;
import com.mna.springbootsecurity.service.core.UploadProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadProcessServiceImpl implements UploadProcessService {

    private final UploadProcessDao uploadProcessDao;

    @Override
    public UploadProcess createUploadProcess(String fileName, UploadProcessType type) {
        UploadProcess uploadProcess = new UploadProcess();
        uploadProcess.setFileName(fileName);
        uploadProcess.setStatus(UploadProcessState.STARTED);
        uploadProcess.setType(type);
        return uploadProcessDao.save(uploadProcess);
    }

    @Override
    public UploadProcess updateUploadProcess(UploadProcess uploadProcess) {
        return uploadProcessDao.save(uploadProcess);
    }

    public boolean isUploadProcessRunning(UploadProcessType type) {
        Optional<UploadProcess> ongoingProcess = uploadProcessDao.findByTypeAndStatus(type, UploadProcessState.STARTED);
        return ongoingProcess.isPresent();
    }
}
