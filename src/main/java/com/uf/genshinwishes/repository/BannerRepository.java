package com.uf.genshinwishes.repository;

import com.uf.genshinwishes.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByGachaTypeOrderByStartDesc(Integer gachaType);

    Banner findFirstByGachaTypeOrderByEndDesc(Integer gachaType);
}
