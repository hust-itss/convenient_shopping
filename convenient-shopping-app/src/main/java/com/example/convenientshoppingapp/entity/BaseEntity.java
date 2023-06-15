package com.example.convenientshoppingapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;


@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @CreationTimestamp
    @Column(name = "create_at")
    protected Timestamp createAt;
    @LastModifiedDate
    @UpdateTimestamp
    @Column(name = "update_at")
    protected Timestamp updateAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
