package com.uf.genshinwishes.dto.mapper;

import com.uf.genshinwishes.dto.BannerDTO;
import com.uf.genshinwishes.model.Banner;
import com.uf.genshinwishes.model.enums.BannerType;
import com.uf.genshinwishes.model.enums.Region;
import com.uf.genshinwishes.model.User;
import com.uf.genshinwishes.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BannerMapper {
    public BannerDTO toDto(Banner banner) {
        return this.toDto(null, banner);
    }

    public BannerDTO toDto(User user, Banner banner) {
        if (banner == null) return null;

        BannerDTO bannerDTO = new BannerDTO();

        bannerDTO.setId(banner.getId());
        bannerDTO.setVersion(banner.getVersion());
        bannerDTO.setItems(banner.getItems());
        bannerDTO.setGachaType(BannerType.from(banner.getGachaType()).orElse(null));
        bannerDTO.setImage(banner.getImage());

        if (banner.getStart() != null && banner.getEnd() != null) {
            Map<Region, LocalDateTime[]> startEndByRegion = Arrays.stream(Region.values())
                .collect(Collectors.toMap(Function.identity(), region -> {
                    LocalDateTime startTime = computeDate(region, banner.getStart(), banner.getIsStartLocale());
                    LocalDateTime endTime = computeDate(region, banner.getEnd(), banner.getIsEndLocale());

                    return new LocalDateTime[]{startTime, endTime};
                }));

            bannerDTO.setStartEndByRegion(startEndByRegion);

            if(user != null) {
                Region region = Region.getFromUser(user);

                bannerDTO.setStart(startEndByRegion.get(region)[0]);
                bannerDTO.setEnd(startEndByRegion.get(region)[1]);
            }
        }

        return bannerDTO;
    }

    private LocalDateTime computeDate(Region region, LocalDateTime date, Boolean isLocale) {
        return isLocale != null && isLocale ? date : date.minusHours(UserService.getRegionOffset(region));
    }
}
