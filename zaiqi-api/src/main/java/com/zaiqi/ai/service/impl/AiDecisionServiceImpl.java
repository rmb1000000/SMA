package com.zaiqi.ai.service.impl;

import com.alibaba.fastjson2.JSON;
import com.zaiqi.ai.client.DeepSeekClient;
import com.zaiqi.ai.service.AiDecisionService;
import com.zaiqi.user.entity.User;
import com.zaiqi.user.entity.UserProfile;
import com.zaiqi.user.mapper.UserMapper;
import com.zaiqi.user.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiDecisionServiceImpl implements AiDecisionService {

    private final DeepSeekClient deepSeekClient;
    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;

    @Override
    public Map<String, Object> getMatchAnalysis(Long userId, Long targetUserId) {
        UserProfile userProfile = userProfileMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserProfile>()
                        .eq(UserProfile::getUserId, userId));
        UserProfile targetProfile = userProfileMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<UserProfile>()
                        .eq(UserProfile::getUserId, targetUserId));

        StringBuilder sb = new StringBuilder();
        if (userProfile != null) {
            sb.append("用户A - 价值观:").append(userProfile.getDimValues())
              .append(" 生活方式:").append(userProfile.getDimLifestyle())
              .append(" 经济观念:").append(userProfile.getDimEconomy())
              .append(" 家庭观念:").append(userProfile.getDimFamily())
              .append(" 感性态度:").append(userProfile.getDimEmotion()).append("\n");
        }
        if (targetProfile != null) {
            sb.append("用户B - 价值观:").append(targetProfile.getDimValues())
              .append(" 生活方式:").append(targetProfile.getDimLifestyle())
              .append(" 经济观念:").append(targetProfile.getDimEconomy())
              .append(" 家庭观念:").append(targetProfile.getDimFamily())
              .append(" 感性态度:").append(targetProfile.getDimEmotion());
        }

        String prompt = "你是一个专业的再婚匹配分析顾问。基于5维数据分析匹配程度，返回JSON格式。";
        String result = deepSeekClient.chat(prompt, sb.toString());
        try { return JSON.parseObject(result); }
        catch (Exception e) { return mockMatchData(); }
    }

    @Override
    public Map<String, Object> getFullReport(Long userId, Long targetUserId) {
        String prompt = "你是一个再婚决策顾问。生成完整决策报告（5维分析/风险/沟通/判断/计划）。JSON格式。";
        String msg = "用户A:" + userId + " 用户B:" + targetUserId;
        String result = deepSeekClient.chat(prompt, msg);
        try { return JSON.parseObject(result); }
        catch (Exception e) { return mockReportData(); }
    }

    @Override
    public Map<String, Object> getReadinessAssessment(Long userId, Map<String, Object> answers) {
        String prompt = "你是一个再婚readiness评估顾问。返回JSON含总体评分/优势/顾虑/建议。";
        String result = deepSeekClient.chat(prompt, JSON.toJSONString(answers));
        try { return JSON.parseObject(result); }
        catch (Exception e) {
            Map<String, Object> m = new HashMap<>();
            m.put("overall", "基本准备好"); m.put("score", 72);
            m.put("strengths", List.of("经济状况稳定", "家庭支持良好"));
            m.put("concerns", List.of("情感处理需要更多时间"));
            m.put("suggestions", List.of("建议给自己更多时间"));
            return m;
        }
    }

    @Override
    public Map<String, Object> getChatSuggestion(Long userId, Long sessionId) {
        String result = deepSeekClient.chat("你是一个再婚对话顾问。提供回复建议/话题/风险提示。JSON。",
                "用户:" + userId + " 会话:" + sessionId);
        try { return JSON.parseObject(result); }
        catch (Exception e) {
            Map<String, Object> m = new HashMap<>();
            m.put("suggestion", "可以聊聊对家庭分工的看法");
            m.put("topics", List.of("家庭分工", "子女教育", "经济管理"));
            m.put("riskAlert", null);
            return m;
        }
    }

    @Override
    public Map<String, Object> getReadinessQuestions() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("title", "再婚 Readiness 评估问卷");
        result.put("questions", List.of(
            Map.of("id","q1","question","您上一段婚姻结束多久了？","options",List.of("不到半年","半年到一年","一到三年","三年以上")),
            Map.of("id","q2","question","您对上一段婚姻的反思程度？","options",List.of("已经完全放下","大部分释怀","偶尔还会想起","仍然有负面情绪")),
            Map.of("id","q3","question","您的子女对您再婚的态度？","options",List.of("非常支持","可以接受","无所谓","不太接受")),
            Map.of("id","q4","question","您的经济状况是否稳定？","options",List.of("非常稳定","基本稳定","一般","不太稳定")),
            Map.of("id","q5","question","您对再婚最看重什么？","options",List.of("情感陪伴","经济互助","子女成长环境","社会认同")),
            Map.of("id","q6","question","如果再次遇到矛盾，您倾向于？","options",List.of("坦诚沟通","给彼此空间","寻求专业帮助","回避冲突"))
        ));
        return result;
    }

    private Map<String, Object> mockMatchData() {
        Map<String, Object> d = new HashMap<>();
        d.put("matchScore", 78);
        d.put("dimensions", List.of(
            Map.of("name","价值观","score",82,"comment","高度匹配"),
            Map.of("name","生活方式","score",65,"comment","存在差异"),
            Map.of("name","经济观念","score",73,"comment","良好"),
            Map.of("name","家庭观念","score",91,"comment","非常匹配"),
            Map.of("name","感性态度","score",80,"comment","匹配")));
        return d;
    }

    private Map<String, Object> mockReportData() {
        Map<String, Object> r = new HashMap<>(mockMatchData());
        r.put("risks", List.of("对方子女抚养权可能存在争议"));
        r.put("communication", "双方沟通模式较为理性");
        r.put("verdict", "有一定潜力");
        r.put("actionPlan", List.of("了解对方对子女教育的看法", "讨论婚后居住安排"));
        return r;
    }
}
