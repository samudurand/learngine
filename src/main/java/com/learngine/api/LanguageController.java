package com.learngine.api;

import com.learngine.api.model.TextToTranslate;
import com.learngine.api.model.TranslationResult;
import com.learngine.source.translate.TranslateService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@Validated
public class LanguageController {

    private final TranslateService translateService;

    public LanguageController(TranslateService translateService) {
        this.translateService = translateService;
    }

    @PostMapping("/languages/translate")
    public Mono<TranslationResult> translate(@RequestBody @Valid TextToTranslate toTranslate) {
        return translateService
                .translateDescription(toTranslate.getMovieId(), toTranslate.getTarget())
                .map(TranslationResult::new);
    }
}
