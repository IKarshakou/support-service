package com.training.dto.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties({"someField2", "someField3"})
//@JsonRootName("category")
public class CategoryDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @NotBlank(message = "The 'name' field cannot be empty.")
    private String name;

    @JsonIgnore
    private String someField;

    private String someField2;
    private String someField3;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String someField4;

//    @JsonValue
    @Override
    public String toString() {
        return "CategoryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", someField='" + someField + '\'' +
                ", someField2='" + someField2 + '\'' +
                ", someField3='" + someField3 + '\'' +
                ", someField4='" + someField4 + '\'' +
                '}';
    }
}
