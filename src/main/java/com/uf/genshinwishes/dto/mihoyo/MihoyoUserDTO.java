package com.uf.genshinwishes.dto.mihoyo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class MihoyoUserDTO {
    @JsonProperty("user_id")
    private String userId;
    private String nickname;
}
