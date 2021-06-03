package com.uf.genshinwishes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Serializable {

    @Id
    private Long id;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String itemType;

    @Column(nullable = false)
    private Integer rankType;


    @ManyToOne
    @JoinTable(
        name = "upload_file_morph",
        joinColumns = @JoinColumn(name = "related_id"),
        inverseJoinColumns = @JoinColumn(name = "upload_file_id"))
    private Image image;


    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;
}
