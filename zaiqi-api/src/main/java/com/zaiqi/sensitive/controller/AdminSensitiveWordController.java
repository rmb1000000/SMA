package com.zaiqi.sensitive.controller;

import com.zaiqi.common.PageResult;
import com.zaiqi.common.Result;
import com.zaiqi.sensitive.entity.SensitiveWord;
import com.zaiqi.sensitive.service.SensitiveWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/sensitive-words")
@RequiredArgsConstructor
public class AdminSensitiveWordController {

    private final SensitiveWordService sensitiveWordService;

    @GetMapping
    public Result<PageResult<SensitiveWord>> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String scope) {
        return Result.success(sensitiveWordService.getWordList(page, size, scope));
    }

    @PostMapping
    public Result<Void> add(@RequestParam String word,
                             @RequestParam(required = false) Integer level,
                             @RequestParam(required = false) String scope) {
        sensitiveWordService.addWord(word, level, scope);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        sensitiveWordService.deleteWord(id);
        return Result.success();
    }
}
