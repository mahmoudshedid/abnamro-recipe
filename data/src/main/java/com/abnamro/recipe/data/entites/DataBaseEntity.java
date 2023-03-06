package com.abnamro.recipe.data.entites;

import com.abnamro.recipe.domain.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public abstract class DataBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_by", nullable = false)
    private String updatedBy;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public DataBaseEntity(BaseEntity baseEntity) {
        this.id = baseEntity.getId();
        this.name = baseEntity.getName();
        this.description = baseEntity.getDescription();
        this.createdBy = baseEntity.getCreatedBy();
        this.createdAt = baseEntity.getCreatedAt();
        this.updatedBy = baseEntity.getUpdatedBy();
        this.updatedAt = baseEntity.getUpdatedAt();
    }
}
