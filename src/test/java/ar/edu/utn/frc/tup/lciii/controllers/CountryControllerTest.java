package ar.edu.utn.frc.tup.lciii.controllers;


import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CountryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService countryService;

    private static final String COUNTRY_CODE_1 = "ARG";
    private static final String COUNTRY_NAME_1 = "Argentina";
    private static final String COUNTRY_CODE_2 = "BRA";
    private static final String COUNTRY_NAME_2 = "Brazil";
    private static final String CONTINENT = "South America";
    private static final String LANGUAGE = "Spanish";

    @Test
    void testGetAllCountries() throws Exception {
        CountryDTO country1 = new CountryDTO(COUNTRY_CODE_1, COUNTRY_NAME_1, CONTINENT, null);
        CountryDTO country2 = new CountryDTO(COUNTRY_CODE_2, COUNTRY_NAME_2, CONTINENT, null);
        List<CountryDTO> countries = Arrays.asList(country1, country2);

        when(countryService.getCountrie(null)).thenReturn(countries);

        mockMvc.perform(get("/api/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].code", is(COUNTRY_CODE_1)))
                .andExpect(jsonPath("$[1].code", is(COUNTRY_CODE_2)));
    }

    @Test
    void testGetCountriesByContinent() throws Exception {
        CountryDTO country = new CountryDTO(COUNTRY_CODE_1, COUNTRY_NAME_1, CONTINENT, null);
        List<CountryDTO> countries = Arrays.asList(country);

        when(countryService.getCountriesByContinent(CONTINENT)).thenReturn(countries);

        mockMvc.perform(get("/api/countries/" + CONTINENT + "/continent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].code", is(COUNTRY_CODE_1)));
    }

    @Test
    void testGetCountriesByLanguage() throws Exception {
        CountryDTO country = new CountryDTO(COUNTRY_CODE_1, COUNTRY_NAME_1, CONTINENT, Map.of("es", LANGUAGE));
        List<CountryDTO> countries = Arrays.asList(country);

        when(countryService.getCountriesByLanguage(LANGUAGE)).thenReturn(countries);

        mockMvc.perform(get("/api/countries/" + LANGUAGE + "/language"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].code", is(COUNTRY_CODE_1)));
    }

    @Test
    void testGetCountryWithMostBorders() throws Exception {
        CountryDTO country = new CountryDTO("CHN", "China", "Asia", null);

        when(countryService.getCountryWithMostBorders()).thenReturn(country);

        mockMvc.perform(get("/api/countries/most-borders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code", is("CHN")));
    }
}