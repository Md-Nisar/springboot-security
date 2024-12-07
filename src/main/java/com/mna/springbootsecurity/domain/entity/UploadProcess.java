package com.mna.springbootsecurity.domain.entity;

import com.mna.springbootsecurity.base.enums.UploadProcessState;
import com.mna.springbootsecurity.base.enums.UploadProcessType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CORE_UPLOAD_PROCESS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UploadProcessState status;

    @Column(nullable = false)
    private UploadProcessType type;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 500)
    private String errorMessage;

}

