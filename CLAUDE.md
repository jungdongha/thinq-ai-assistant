# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build
./gradlew build

# Run (port 8081)
./gradlew bootRun

# Test
./gradlew test

# Compile only (check for errors)
./gradlew compileJava
```

## Environment Variables

Required in `.env` or system environment:

```
GEMINI_API_KEY=
CLAUDE_API_KEY=
GROQ_API_KEY=
THINQ_PAT_TOKEN=
THINQ_API_KEY=
THINQ_BASE_URL=https://api-kic.lgthinq.com
THINQ_CLIENT_ID=
JASYPT_ENCRYPTOR_PASSWORD=
JASYPT_ALGORITHM=
```

## Tech Stack

- **Spring Boot 4.0.5** + **Spring AI 2.0.0-M3**
- **Java 17**, H2 in-memory database, SpringDoc OpenAPI
- AI models: `claude-sonnet-4-6` (Anthropic), `gemini-1.5-flash` (Google), `meta-llama/llama-4-scout-17b-16e-instruct` (Groq via OpenAI-compatible API)

## Architecture

### Domain Structure

```
com.example.demo/
├── domain/ai/          # Chat API (controller, service, dto, exception)
├── domain/thinq/       # LG ThinQ IoT tools + response DTOs + exception
└── global/
    ├── advisor/        # CustomLoggingAdvisor (Spring AI advisor)
    ├── config/         # AiConfig, RestClientConfig, WebConfig
    ├── properties/     # AiProperties (groq), ThinQProperties
    └── common/         # ApiResponse wrapper, BaseException, ErrorCode, GlobalExceptionHandler
```

### AI Layer

**Chat flow**: `AiController` → `AiService` → selected `ChatClient` → ThinQ tools

**Multi-model selection**: `POST /api/ai/chat` accepts an optional `model` field (`"claude"`, `"gemini"`, `"groq"`). `AiService` resolves this to a bean name by appending `"ChatClient"` (e.g. `"groqChatClient"`). Default when `model` is null: `groqChatClient`.

- `AiConfig`: defines `claudeChatClient`, `geminiChatClient`, `groqChatClient` beans, and `ChatMemory` bean
  - `groqChatClient` is built manually via `OpenAiApi` pointing to `https://api.groq.com/openai` (OpenAI-compatible); the `spring.ai.openai.api-key=dummy` disables Spring Boot's OpenAI auto-configuration
  - `ChatMemory`: `MessageWindowChatMemory` with `InMemoryChatMemoryRepository`, window of 20 messages
- `AiService`: injects all `ChatClient` beans as `Map<String, ChatClient>`, and all `ThinQTool` beans as `List<ThinQTool>`; builds prompt with `CustomLoggingAdvisor`, `MessageChatMemoryAdvisor`, and tools
- System prompt loaded from `src/main/resources/prompts/system.st`; `{currentDate}` placeholder replaced at runtime

**Spring AI 2.0.0-M3 API patterns** (breaking changes from 1.x):
- `MessageChatMemoryAdvisor` requires builder: `MessageChatMemoryAdvisor.builder(chatMemory).conversationId(...).build()`
- `ChatMemory` bean uses `MessageWindowChatMemory.builder().chatMemoryRepository(...).maxMessages(20).build()`
- Tool annotations: `@Tool` and `@ToolParam` from `org.springframework.ai.tool.annotation`
- Advisor API classes (`CallAroundAdvisor`, `AdvisedRequest`, etc.) — verify package paths against the jar if import errors occur

### ThinQ Tools

`ThinQTool` is a **marker interface**. All tool classes implement it and are `@Component` beans, which allows `AiService` to inject them all via `List<ThinQTool>`. Tools use Spring AI's `@Tool` annotation on methods.

Tools call the LG ThinQ REST API via `thinQRestClent` (`RestClient` bean in `RestClientConfig`). The client sets default headers: `x-api-key`, `x-client-id`, `x-service-phase: OP`, `Authorization: Bearer <PAT>`, and adds a per-request `x-message-id` (base64-encoded UUID).

Available tool classes: `ThinQDeviceTools`, `ThinQRouteTools`, `ThinQEnergyTools`, `ThinQEventTools`, `ThinQPushTools`

### API

- `POST /api/ai/chat` — body: `{ "message": "...", "conversationId": "..." (optional), "model": "claude|gemini|groq" (optional, default: groq) }`
- Response: `ApiResponse<ChatResponse>` containing `content` and `conversationId`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
