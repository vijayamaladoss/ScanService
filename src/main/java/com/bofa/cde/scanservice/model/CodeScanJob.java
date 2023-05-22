package com.bofa.cde.scanservice.model;

import javax.persistence.*;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "**")
public class CodeScanJob {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sourceCodePath;

    private String status; // Field to track job status

    @Version
    private int version; // New field for optimistic locking

    // Getters and setters...
}
