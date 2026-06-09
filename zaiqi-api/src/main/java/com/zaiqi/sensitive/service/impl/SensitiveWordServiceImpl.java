package com.zaiqi.sensitive.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaiqi.common.PageResult;
import com.zaiqi.sensitive.entity.SensitiveWord;
import com.zaiqi.sensitive.mapper.SensitiveWordMapper;
import com.zaiqi.sensitive.service.SensitiveWordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SensitiveWordServiceImpl implements SensitiveWordService {
    private final SensitiveWordMapper mapper;

    @Override
    public PageResult<SensitiveWord> getWordList(int page, int size, String scope) {
        Page<SensitiveWord> p = new Page<>(page, size);
        LambdaQueryWrapper<SensitiveWord> w = new LambdaQueryWrapper<SensitiveWord>().eq(SensitiveWord::getStatus, 1);
        if (scope != null && !scope.isEmpty())
            w.and(q -> q.eq(SensitiveWord::getScope, scope).or().eq(SensitiveWord::getScope, "all"));
        return PageResult.of(mapper.selectPage(p, w));
    }

    @Override
    public void addWord(String word, Integer level, String scope) {
        SensitiveWord sw = new SensitiveWord();
        sw.setWord(word); sw.setLevel(level != null ? level : 1);
        sw.setScope(scope != null ? scope : "all"); sw.setStatus(1);
        mapper.insert(sw);
    }

    @Override
    public void deleteWord(Long id) { mapper.deleteById(id); }

    @Override
    public boolean checkContent(String content, String scope) {
        List<SensitiveWord> words = mapper.selectList(
                new LambdaQueryWrapper<SensitiveWord>().eq(SensitiveWord::getStatus, 1));
        return words.stream().anyMatch(sw -> content.contains(sw.getWord()));
    }
}
