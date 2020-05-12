package com.learngine.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextToTranslate {
    @NotNull
    Integer movieId;

    @NotEmpty
    @Length(min = 2)
    String target;
}
