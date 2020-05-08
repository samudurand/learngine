package com.learngine.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextToTranslate {
    @NotEmpty
    String text;

    @NotEmpty
    @Length(min = 2)
    String target;
}
