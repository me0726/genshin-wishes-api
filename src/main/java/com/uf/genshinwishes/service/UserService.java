package com.uf.genshinwishes.service;

import com.uf.genshinwishes.config.security.oauth2.GsOauth2User;
import com.uf.genshinwishes.config.security.oidc.GsOidcUser;
import com.uf.genshinwishes.dto.mihoyo.MihoyoUserDTO;
import com.uf.genshinwishes.exception.ApiError;
import com.uf.genshinwishes.exception.ErrorType;
import com.uf.genshinwishes.model.enums.Region;
import com.uf.genshinwishes.model.User;
import com.uf.genshinwishes.repository.UserRepository;
import com.uf.genshinwishes.service.mihoyo.MihoyoImRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private MihoyoImRestClient mihoyoImRestClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WishService wishService;

    public Optional<User> findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public GsOauth2User loadByOauth2User(OAuth2User oAuth2User) {
        return new GsOauth2User(oAuth2User, loadOrCreateUser(oAuth2User));
    }

    public GsOidcUser loadByOidcUser(OidcUser oidcUser) {
        return new GsOidcUser(oidcUser, loadOrCreateUser(oidcUser));
    }


    private User loadOrCreateUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            OAuth2Error auth2Error = new OAuth2Error("No necessary permissions: email");
            throw new OAuth2AuthenticationException(auth2Error, auth2Error.toString());
        }
        return userRepository.findByEmail(email)
            .orElseGet(() -> userRepository.save(User.builder().email(email).build()));
    }

    @Transactional
    public void verifyUserIsUnlinkedAndLinkToMihoyo(User user, String authkey, String gameBiz) throws ApiError {
        if (user.getMihoyoUid() != null) {
            return;
        }

        linkToMihoyo(user, authkey, gameBiz);
    }

    private void linkToMihoyo(User user, String authkey, String gameBiz) {
        MihoyoUserDTO mihoyoUser = mihoyoImRestClient.getUserInfo(Optional.empty(), authkey, gameBiz);
        user.setRegion(switch (mihoyoUser.getUserId().charAt(0)) {
                case '1' -> Region.CHINA.getPrefix();
                case '6' -> Region.AMERICA.getPrefix();
                case '7' -> Region.EUROPE.getPrefix();
                default -> Region.ASIA.getPrefix();
            }).setMihoyoUid(mihoyoUser.getUserId())
            .setNickname(mihoyoUser.getNickname());
        userRepository.save(user);
    }


    @Transactional
    public void linkNewMihoyoAccountAndDeleteOldWishes(User user, String authkey, String gameBiz) throws ApiError {
        this.linkToMihoyo(user, authkey, gameBiz);

        wishService.deleteAllUserWishes(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void updateLang(User user, String lang) {
        if (lang == null || "".equals(Locale.forLanguageTag(lang).toString()))
            throw new ApiError(ErrorType.INVALID_LANG);
        user.setLang(lang);
        userRepository.save(user);
    }

    public void updateWholeClock(User user, Boolean wholeClock) {
        user.setWholeClock(wholeClock);
        userRepository.save(user);
    }

    public String initProfileId(User user) {
        String profileId = BigInteger.valueOf(Long.parseLong(Instant.now().toEpochMilli() + "" + user.getId())).toString(36);
        user.setProfileId(profileId);
        userRepository.save(user);
        return profileId;
    }

    public Optional<User> findUserByProfileId(String profileId) {
        return userRepository.findByProfileId(profileId);
    }


    public Long getUsersCount() {
        return this.updateUsersCount();
    }


    public Long updateUsersCount() {
        return userRepository.countByNicknameIsNotNull();
    }

    public void share(User user, boolean share) {
        user.setSharing(share);

        userRepository.save(user);
    }

    public Long count() {
        return this.userRepository.count();
    }

    public static int getRegionOffset(Region region) {
        return switch (region) {
            case AMERICA -> 5;
            case EUROPE -> -1;
            case ASIA -> -8;
            default -> throw new IllegalStateException("Unexpected value: " + region);
        };
    }
}
