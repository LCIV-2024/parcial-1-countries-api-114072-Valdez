package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        private final CountryRepository countryRepository;

        private final RestTemplate restTemplate;

        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }

        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                        .name((String) nameData.get("common"))
                        .code((String) countryData.get("cca3"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .borders((List<String>) countryData.get("borders"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .build();
        }


        private CountryDTO mapToDTO(Country country) {
                CountryDTO dto = new CountryDTO();
                dto.setCode(country.getCode());
                dto.setName(country.getName());
                dto.setRegion(country.getRegion());
                dto.setLanguages(country.getLanguages());
                return dto;
        }

        public List<CountryDTO> getAllCountriesDTOs() {
                List<Country> countries = getAllCountries();

                List<CountryDTO> countryDTOs = new ArrayList<>();
                for (Country country : countries) {
                        countryDTOs.add(mapToDTO(country));
                }

                return countryDTOs;
        }

        public List<CountryDTO> getCountrie(String nameOrCode) {

                List<CountryDTO> countries = getAllCountriesDTOs();

                if (nameOrCode != null && !nameOrCode.isEmpty()) {
                        List<CountryDTO> filteredCountries = new ArrayList<>();
                        for (CountryDTO countryDTO : countries) {
                                boolean matchesName = countryDTO.getName().equalsIgnoreCase(nameOrCode);
                                boolean matchesCode = countryDTO.getCode().equalsIgnoreCase(nameOrCode);
                                if (matchesName || matchesCode) {
                                        filteredCountries.add(countryDTO);
                                }
                        }
                        return filteredCountries;
                }

                return countries;
        }

        public List<CountryDTO> getCountriesByContinent(String continent) {
                List<CountryDTO> countries = getAllCountriesDTOs();

                List<CountryDTO> filteredCountries = new ArrayList<>();
                for (CountryDTO countryDTO : countries) {
                        if (countryDTO.getRegion().equalsIgnoreCase(continent)) {
                                filteredCountries.add(countryDTO);
                        }
                }

                return filteredCountries;
        }

        public List<CountryDTO> getCountriesByLanguage(String language) {

                List<CountryDTO> countries = getAllCountriesDTOs();

                List<CountryDTO> filteredCountries = new ArrayList<>();
                if (language != null && !language.isEmpty()) {
                        for (CountryDTO countryDTO : countries) {
                                if (countryDTO.getLanguages() != null && countryDTO.getLanguages().values().stream()
                                        .anyMatch(lang -> lang.equalsIgnoreCase(language))) {
                                        filteredCountries.add(countryDTO);
                                }
                        }
                }

                return filteredCountries;
        }

        public CountryDTO getCountryWithMostBorders() {
                List<Country> countries = getAllCountries();

                Country countryWithMostBorders = null;
                int maxBorders = 0;

                for (Country country : countries) {
                        int numberOfBorders = (country.getBorders() != null) ? country.getBorders().size() : 0;
                        if (numberOfBorders > maxBorders) {
                                maxBorders = numberOfBorders;
                                countryWithMostBorders = country;
                        }
                }

                return countryWithMostBorders != null ? mapToDTO(countryWithMostBorders) : null;
        }

        @Transactional
        public List<CountryDTO> saveCountries(int amountOfCountryToSave) {
                List<Country> allCountries = getAllCountries();

                if (amountOfCountryToSave > 10) {
                        amountOfCountryToSave = 10;
                }

                Collections.shuffle(allCountries);

                List<Country> countriesToSave = allCountries.stream()
                        .limit(amountOfCountryToSave)
                        .collect(Collectors.toList());

                List<CountryEntity> entities = countriesToSave.stream()
                        .map(country -> new CountryEntity(
                                null,
                                country.getName(),
                                country.getCode(),
                                country.getPopulation(),
                                country.getArea()))
                        .collect(Collectors.toList());

                List<CountryEntity> savedEntities = countryRepository.saveAll(entities);

                return savedEntities.stream()
                        .map(entity -> new CountryDTO(entity.getCode(), entity.getName()))
                        .collect(Collectors.toList());
        }

}