package com.uf.genshinwishes.service.mihoyo;

import com.uf.genshinwishes.dto.mihoyo.MihoyoInfoRetDTO;
import com.uf.genshinwishes.dto.mihoyo.MihoyoUserDTO;
import com.uf.genshinwishes.exception.ApiError;
import com.uf.genshinwishes.exception.ErrorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MihoyoImRestClientTest {

    private final String MIHOYO_ENDPOINT = "http://mihoyo-im-endpoint";

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MihoyoGameBizSettingsSelector selector;

    @InjectMocks
    private MihoyoImRestClient mihoyoImRestClient = new MihoyoImRestClient();


    @BeforeEach
    public void beforeEach() {
        Mockito.when(selector.getImEndpoint(ArgumentMatchers.eq("hk4e_global")))
            .thenReturn(Optional.of(MIHOYO_ENDPOINT));
    }

    @Test
    void givenMihoyoReturnsMinusOneCode_thenThrowApiError() throws URISyntaxException {
        URI uri = new URI(MIHOYO_ENDPOINT + "/common/im/userClient/initUserChat?authkey=authkey&authkey_ver=1&game_biz=hk4e_global&sign_type=2");
        MihoyoInfoRetDTO retDTO = new MihoyoInfoRetDTO();
        retDTO.setRetcode(-1);

        Mockito
            .when(restTemplate.postForEntity(
                uri,
                "{\"device\":\"Mozilla\",\"language\":\"en\",\"system_info\":\"Mozilla/5.0\"}",
                MihoyoInfoRetDTO.class))
            .thenReturn(new ResponseEntity<>(retDTO, HttpStatus.OK));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> mihoyoImRestClient.getUserInfo(Optional.empty(), "authkey", "hk4e_global"));


        Mockito.verify(restTemplate, Mockito.times(1)).postForEntity(
            Mockito.eq(uri),
            Mockito.eq("{\"device\":\"Mozilla\",\"language\":\"en\",\"system_info\":\"Mozilla/5.0\"}"),
            Mockito.eq(MihoyoInfoRetDTO.class));

        assertThat(exception).isExactlyInstanceOf(ApiError.class);
        assertThat(((ApiError) exception).getErrorType()).isEqualTo(ErrorType.AUTHKEY_INVALID);
    }

    @Test
    void givenMihoyoReturnsNullData_thenThrowApiError() throws URISyntaxException {
        URI uri = new URI(MIHOYO_ENDPOINT + "/common/im/userClient/initUserChat?authkey=authkey&authkey_ver=1&game_biz=hk4e_global&sign_type=2");
        MihoyoInfoRetDTO retDTO = new MihoyoInfoRetDTO();
        retDTO.setRetcode(1);

        Mockito
            .when(restTemplate.postForEntity(
                uri,
                "{\"device\":\"Mozilla\",\"language\":\"en\",\"system_info\":\"Mozilla/5.0\"}",
                MihoyoInfoRetDTO.class))
            .thenReturn(new ResponseEntity<>(retDTO, HttpStatus.OK));

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> mihoyoImRestClient.getUserInfo(Optional.empty(), "authkey", "hk4e_global"));

        Mockito.verify(restTemplate, Mockito.times(1)).postForEntity(
            Mockito.eq(uri),
            Mockito.eq("{\"device\":\"Mozilla\",\"language\":\"en\",\"system_info\":\"Mozilla/5.0\"}"),
            Mockito.eq(MihoyoInfoRetDTO.class));

        assertThat(exception).isExactlyInstanceOf(ApiError.class);
        assertThat(((ApiError) exception).getErrorType()).isEqualTo(ErrorType.AUTHKEY_INVALID);
    }

    @Test
    void givenMihoyoReturnsCorrectData_thenReturnMihoyoUserDtoWithUidAndUsername() throws URISyntaxException {
        URI uri = new URI(MIHOYO_ENDPOINT + "/common/im/userClient/initUserChat?authkey=authkey&authkey_ver=1&game_biz=hk4e_global&sign_type=2");
        MihoyoUserDTO userDTO = new MihoyoUserDTO();
        userDTO.setUserId("some user id");
        userDTO.setNickname("some username");
        MihoyoInfoRetDTO retDTO = new MihoyoInfoRetDTO();
        retDTO.setRetcode(1);
        retDTO.setData(userDTO);

        Mockito
            .when(restTemplate.postForEntity(
                uri,
                "{\"device\":\"Mozilla\",\"language\":\"en\",\"system_info\":\"Mozilla/5.0\"}",
                MihoyoInfoRetDTO.class))
            .thenReturn(new ResponseEntity<>(retDTO, HttpStatus.OK));

        MihoyoUserDTO userDto = mihoyoImRestClient.getUserInfo(Optional.empty(), "authkey", "hk4e_global");


        Mockito.verify(restTemplate, Mockito.times(1)).postForEntity(
            Mockito.eq(uri),
            Mockito.eq("{\"device\":\"Mozilla\",\"language\":\"en\",\"system_info\":\"Mozilla/5.0\"}"),
            Mockito.eq(MihoyoInfoRetDTO.class));

        assertThat(userDto).isEqualTo(userDTO);
    }

    @Test
    public void testRegionOfCN() {
        Mockito.reset(selector);
        Mockito.when(selector.getImEndpoint(ArgumentMatchers.eq("hk4e_cn")))
            .thenReturn(Optional.of("https://api-takumi.mihoyo.com"));
        mihoyoImRestClient.setRestTemplate(new RestTemplate());
        MihoyoUserDTO dto = mihoyoImRestClient.getUserInfo(Optional.empty(), "UGCvVmi4BsxNR%2bX%2fPXvdJubb2%2bETkLZT3rgWp0ezJ%2bHYMRp5iG33Fymb3TUpA5CHCD1SqJlRjWvBs366DX%2fpH4FIo5Jn%2bTovRnAcIHyJIR9PhAVEqtHUKf2xjz52%2bTrgC4wE19hW9TGDdQWWyQMEa5Vi9OZkFd30TV5xjiNHBYWdTqw%2bQVyzTDvaH7AtnADeVyt%2fbpnw1H5vBI2lEIMhfrAj84A1Gm%2f4UMk7ZceLlesPi9wkQ4dY5YInEcJFQR1BuEVBlsG6o6YITobqxzmikROTr7BAYOvH2xVTenQ4IOPrS52ckNyCemwJFnfD5UQiieJ7TvAmfmbeMWqSE3jI8ELo4WHBn6bo9aDougMrT4MuDCMXmfBH%2bnWhLkAFZQjiMdUuL62Kd9xCOi7WuYb3A0GVxKgKi%2bQIA7ZqghtOsWfxIb9pP7RKhVcwaI0nOUmhLiGk5sBXuqyGkAoCF4NdXT8S2vayh7dPMDOQwChVutJLvui2DAOzFLffOP64RCPnMlzKBDYyHgcUaPd2jv%2bndo8flumtCcmKfmwWORKPFonOz93ojaRSL%2bLpN9o5TwSebaP5OY7z2hcnPj7nEqx6wcO1mQsJSw%2bpi3HVWZ9yVNGrSdwGAZypncTFmByBfm9Vpu%2bh%2bX27HH8G01%2fmDUxFIX3x2fz0LLJFsdwQIHkQ%2f80m0k1qa5CYyh%2fubi4NbUXUCcJzsTFKcUL3vCcii3TKzUO7r5%2bd036efTxncaO%2fifB99RGKILuQ64txwzRwUUIV9PYS4bWyRZX4InduovJAEqUhw90RI7fxkJKp1ivJRSDkwpnzJ8RWVmGgTfHJurnkTbi3l2HevFltbFQrdJoXu%2bA3uHNaZxCZVS%2fTvtWU%2fSnAxKroo7p4GVOE3p3i36ST%2fsR9mdIJJ0ElZHKLXnDYSOn4ifeUkQRgdTCaz7Ke5g65LSemke%2bSlFLBg2SgLLenPaRpCq85TrPaW1eudbnym1sQh6s8BRteCk7K4C7lKDKvL3yEFameptODrqhB70gK", "hk4e_cn");
        System.out.println(dto);
    }
}
