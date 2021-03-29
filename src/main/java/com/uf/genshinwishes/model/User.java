package com.uf.genshinwishes.model;

import com.uf.genshinwishes.exception.ApiError;
import com.uf.genshinwishes.exception.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column()
    private String lang;

    @Column(nullable = false)
    private Date creationDate;

    @Column()
    private Date lastLoggingDate;

    @Column()
    private String mihoyoUsername;

    @Column()
    private String mihoyoUid;

    @Version
    @Column(name = "optlock", nullable = false)
    private long version = 0L;

    public int getRegionOffset() {
        if (getMihoyoUid() == null || getMihoyoUid().isEmpty()) throw new ApiError(ErrorType.NO_REGION_FROM_USER_UID);

        switch (getMihoyoUid().charAt(0)) {
            case '6':
                return 5;
            case '7':
                return -1;
            case '8':
                return -8;
            default:
                throw new ApiError(ErrorType.NO_REGION_FROM_USER_UID);
        }
    }
}
