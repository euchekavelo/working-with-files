package ru.comitagroup.workingwithfiles.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "files")
@Data
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String filename;

    private Long size;

    @Column(name = "content", columnDefinition="BYTEA")
    private byte[] content;
}
