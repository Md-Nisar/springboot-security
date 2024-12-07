package com.mna.springbootsecurity.domain.dao;

import com.mna.springbootsecurity.base.enums.UploadProcessState;
import com.mna.springbootsecurity.base.enums.UploadProcessType;
import com.mna.springbootsecurity.domain.entity.UploadProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UploadProcessDao extends JpaRepository<UploadProcess, Long> {

    Optional<UploadProcess> findByTypeAndStatus(UploadProcessType type, UploadProcessState status);

}
