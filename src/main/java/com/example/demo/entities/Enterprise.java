package com.example.demo.entities;

import com.example.demo.enums.SizeEnterprise;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity()
@Table(name = "enterprises")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Enterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String enterpriseName;

    private String description;

    @Column(nullable = false)
    private String cnpj;

    @Column(nullable = false)
    private String contact;

    private String socialReason;
    private String locality;
    private SizeEnterprise sizeEnterprise;
    private Long numberOfEmployees;
    private String sector;
    private String siteUrl;
    private boolean isApproved = false;
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
