package com.uf.genshinwishes.dto.mihoyo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MihoyoInfoRetDTO extends MihoyoRetDTO {
    private MihoyoUserDTO data;
}
