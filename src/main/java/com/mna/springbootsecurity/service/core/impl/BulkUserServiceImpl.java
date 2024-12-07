package com.mna.springbootsecurity.service.core.impl;

import com.mna.springbootsecurity.base.enums.UploadProcessState;
import com.mna.springbootsecurity.base.enums.UploadProcessType;
import com.mna.springbootsecurity.base.vo.excel.UserVO;
import com.mna.springbootsecurity.domain.entity.UploadProcess;
import com.mna.springbootsecurity.file.util.ExcelUtil;
import com.mna.springbootsecurity.file.util.FileUtil;
import com.mna.springbootsecurity.service.core.BulkUserService;
import com.mna.springbootsecurity.service.core.UploadProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
public class BulkUserServiceImpl implements BulkUserService {

    private final UploadProcessService uploadProcessService;

    @Override
    public void upload(File file) {
        boolean uploadProcessRunning = uploadProcessService.isUploadProcessRunning(UploadProcessType.BULK_USER_UPLOAD);
        if (uploadProcessRunning) {
            log.info("Another Bulk user upload process is already running!");
            return;
        }

        UploadProcess uploadProcess = uploadProcessService.createUploadProcess(file.getName(), UploadProcessType.BULK_USER_UPLOAD);

        Workbook workbook = null;
        try {
            workbook = ExcelUtil.createWorkbook(file);
        } catch (IOException e) {
            log.error("Error while creating excel workbook: " + e);
            uploadProcess.setType(UploadProcessType.BULK_USER_UPLOAD);
            uploadProcess.setStatus(UploadProcessState.FAILED);
            uploadProcess.setErrorMessage("Error while creating excel workbook");
            uploadProcessService.updateUploadProcess(uploadProcess);
            return;
        }

        String fileName = file.getName().toUpperCase(Locale.ROOT);
        String validationMessage = ExcelUtil.validate(fileName, workbook, 4);
        if (!validationMessage.equals("VALID")) {
            log.error(validationMessage);
            uploadProcess.setType(UploadProcessType.BULK_USER_UPLOAD);
            uploadProcess.setStatus(UploadProcessState.FAILED);
            uploadProcess.setErrorMessage(validationMessage);
            uploadProcessService.updateUploadProcess(uploadProcess);
            return;
        }

        List<UserVO> users;
        try (InputStream inputStream = FileUtil.fileToInputStream(file)) {
            users = ExcelUtil.mapExcelToVO(inputStream, UserVO.class);
        } catch (Exception e) {
            log.error("Error while converting file to VO");
            uploadProcess.setType(UploadProcessType.BULK_USER_UPLOAD);
            uploadProcess.setStatus(UploadProcessState.FAILED);
            uploadProcess.setErrorMessage("Error while converting file to VO");
            uploadProcessService.updateUploadProcess(uploadProcess);
            return;
        }

        if (users.isEmpty()) {
            log.error("Users list is empty!");
            uploadProcess.setType(UploadProcessType.BULK_USER_UPLOAD);
            uploadProcess.setStatus(UploadProcessState.FAILED);
            uploadProcess.setErrorMessage("Users list is empty!");
            uploadProcessService.updateUploadProcess(uploadProcess);
            return;
        }

        process(uploadProcess, users);
    }

    public void process(UploadProcess uploadProcess, List<UserVO> users) {
        log.info("{} {}", uploadProcess, users);
        throw new UnsupportedOperationException("Method not implmented");
    }


}
