package ar.edu.utn.frc.tup.lciii.dtos.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryDTO {

    private String code;
    private String name;
    @JsonIgnore
    private String region;
    @JsonIgnore
    private Map<String, String> languages;

    public CountryDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
