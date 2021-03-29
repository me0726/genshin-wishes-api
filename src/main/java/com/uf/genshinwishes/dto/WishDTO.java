package com.uf.genshinwishes.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishDTO {
    private Long index;
    private Integer gachaType;
    private Long itemId;
    private Long bannerId;
    private LocalDateTime time;
}
