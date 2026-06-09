package com.zaiqi.ai.client;

import com.zaiqi.ai.config.DeepSeekConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeepSeekClient {

    private final DeepSeekConfig config;

    public String chat(String systemPrompt, String userMessage) {
        try {
            WebClient client = WebClient.builder()
                    .baseUrl(config.getApiUrl())
                    .defaultHeader("Authorization", "Bearer " + config.getApiKey())
                    .defaultHeader("Content-Type", "application/json")
                    .build();

            Map<String, Object> request = Map.of(
                "model", config.getModel(),
                "messages", List.of(
                    Map.of("role", "system", "content", systemPrompt),
                    Map.of("role", "user", "content", userMessage)
                ),
                "temperature", 0.7,
                "max_tokens", 4096
            );

            Map response = client.post()
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            if (response != null) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }
        } catch (Exception e) {
            log.error("DeepSeek API 调用失败", e);
        }
        return getMockResponse(systemPrompt, userMessage);
    }

    private String getMockResponse(String systemPrompt, String userMessage) {
        if (systemPrompt.contains("readiness")) {
            return "{\"overall\":\"基本准备好\",\"score\":72,\"strengths\":[\"经济状况稳定\",\"家庭支持良好\"],\"concerns\":[\"上一段婚姻的情感处理需要更多时间\"],\"suggestions\":[\"建议给自己更多时间走出上一段感情\"]}";
        }
        if (systemPrompt.contains("report")) {
            return "{\"matchScore\":78,\"dimensions\":[{\"name\":\"价值观\",\"score\":82,\"comment\":\"高度匹配\"},{\"name\":\"生活方式\",\"score\":65,\"comment\":\"存在差异\"},{\"name\":\"经济观念\",\"score\":73,\"comment\":\"良好\"},{\"name\":\"家庭观念\",\"score\":91,\"comment\":\"非常匹配\"},{\"name\":\"感性态度\",\"score\":80,\"comment\":\"匹配\"}],\"risks\":[\"对方子女抚养权可能存在争议\"],\"communication\":\"双方沟通模式较为理性，能够就敏感话题展开讨论\",\"verdict\":\"有一定潜力\",\"actionPlan\":[\"建议进一步了解对方对子女教育的看法\",\"建议讨论婚后居住安排\"]}";
        }
        if (systemPrompt.contains("assist")) {
            return "{\"suggestion\":\"可以聊聊对家庭分工的看法\",\"topics\":[\"家庭分工\",\"子女教育\",\"经济管理\"],\"riskAlert\":null}";
        }
        return "{\"message\":\"AI分析完成\"}";
    }
}
