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
THINQ_PAT_TOKEN=
THINQ_API_KEY=
THINQ_BASE_URL=https://api-kic.lgthinq.com
```

## Tech Stack

- **Spring Boot 4.0.5** + **Spring AI 2.0.0-M3**
- **Java 17**, H2 in-memory database, SpringDoc OpenAPI
- AI models: `claude-sonnet-4-6` (Anthropic), `gemini-1.5-flash` (Google)

## Architecture

### Domain Structure

```
com.example.demo/
├── domain/ai/          # Chat API (controller, service, dto)
├── domain/thinq/       # LG ThinQ IoT tools + response DTOs
└── global/
    ├── advisor/        # Custom Spring AI advisors (LoggingAdvisor)
    ├── config/         # AiConfig, RestClientConfig, ThinQProperties
    └── common/         # ApiResponse wrapper, BaseException, ErrorCode, BaseEntity
```

### AI Layer

**Chat flow**: `AiController` → `AiService` → `ChatClient` (Claude) → ThinQ tools

- `AiConfig`: defines `claudeChatClient` and `geminiChatClient` beans, `ChatMemory` bean
- `AiService`: builds prompt with `LoggingAdvisor`, `MessageChatMemoryAdvisor`, and ThinQ tool beans
- System prompt loaded from `src/main/resources/prompts/system.st`

**Spring AI 2.0.0-M3 API patterns** (breaking changes from 1.x):
- `MessageChatMemoryAdvisor` requires builder: `MessageChatMemoryAdvisor.builder(chatMemory).conversationId(...).chatMemoryRetrieveSize(20).build()`
- `ChatMemory` bean: `MessageWindowChatMemory.builder().chatMemoryRepository(new InMemoryChatMemoryRepository()).build()`
- Advisor API classes (`CallAroundAdvisor`, `AdvisedRequest`, etc.) — verify package paths against the jar if import errors occur

### ThinQ Tools

Each tool class is a `@Component` using Spring AI's `@Tool` annotation on methods. Tools call the LG ThinQ REST API via `RestClient` configured in `RestClientConfig` (includes PAT token auth interceptor).

Available tool classes: `ThinQDeviceTools`, `ThinQRouteTools`, `ThinQEnergyTools`, `ThinQEventTools`, `ThinQPushTools`

### API

- `POST /api/ai/chat` — body: `{ "message": "...", "conversationId": "..." (optional) }`
- Swagger UI: `http://localhost:8081/swagger-ui.html`
