package com.zaiqi.sensitive.service;

import com.zaiqi.common.PageResult;
import com.zaiqi.sensitive.entity.SensitiveWord;

public interface SensitiveWordService {
    PageResult<SensitiveWord> getWordList(int page, int size, String scope);
    void addWord(String word, Integer level, String scope);
    void deleteWord(Long id);
    boolean checkContent(String content, String scope);
}
