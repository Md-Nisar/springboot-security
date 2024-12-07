package com.mna.springbootsecurity.controller.file;

import com.mna.springbootsecurity.base.vo.response.ControllerResponse;
import com.mna.springbootsecurity.file.service.FileConvertor;
import com.mna.springbootsecurity.service.core.BulkUserService;
import com.mna.springbootsecurity.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileUploadController {

    private final FileConvertor fileConvertor;
    private final BulkUserService bulkUserService;

    @GetMapping("/upload")
    public ResponseEntity<ControllerResponse<Void>> uploadFile(@RequestParam MultipartFile multipartFile) {
        try {
            File file = fileConvertor.multipartFileToFile(multipartFile);
            bulkUserService.upload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseUtil.success("Success!");
    }
}
