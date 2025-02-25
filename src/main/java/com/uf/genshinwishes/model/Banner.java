package com.uf.genshinwishes.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Banner {
    @Id
    private Long id;

    @Column
    private String version;

    @ManyToMany
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinTable(
        name = "events__items",
        joinColumns = @JoinColumn(name = "event_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items;

    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @Column(nullable = false)
    private Integer gachaType;

    @Column
    private Boolean isStartLocale;
    @Column
    private Boolean isEndLocale;

    @ManyToOne
    @JoinTable(
        name = "upload_file_morph",
        joinColumns = @JoinColumn(name = "related_id"),
        inverseJoinColumns = @JoinColumn(name = "upload_file_id"))
    private Image image;
}
