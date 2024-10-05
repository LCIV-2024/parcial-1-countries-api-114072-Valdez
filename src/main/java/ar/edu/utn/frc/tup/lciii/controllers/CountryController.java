package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryRequestDTO;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@RequiredArgsConstructor
public class CountryController {

    @Autowired
    CountryService countryService;

    @GetMapping("")
    public ResponseEntity<List<CountryDTO>> getAllCountries(
            @RequestParam(value = "nameOrCode", required = false) String nameOrCode) {

        List<CountryDTO> countries = countryService.getCountrie(nameOrCode);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{continent}/continent")
    public ResponseEntity<List<CountryDTO>> getCountriesByContinent(@PathVariable String continent) {
        List<CountryDTO> countries = countryService.getCountriesByContinent(continent);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/{language}/language")
    public ResponseEntity<List<CountryDTO>> getCountriesByLanguage(@PathVariable String language) {
        List<CountryDTO> countries = countryService.getCountriesByLanguage(language);
        return ResponseEntity.ok(countries);
    }

    @GetMapping("/most-borders")
    public ResponseEntity<CountryDTO> getCountryWithMostBorders() {
        CountryDTO country = countryService.getCountryWithMostBorders();
        return ResponseEntity.ok(country);
    }

    @PostMapping
    public ResponseEntity<List<CountryDTO>> saveCountries(@RequestBody CountryRequestDTO request) {
        List<CountryDTO> savedCountries = countryService.saveCountries(request.getAmountOfCountryToSave());
        return ResponseEntity.ok(savedCountries);
    }
}