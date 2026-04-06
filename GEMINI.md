# GEMINI.md - Project Context & Instructions

This file provides comprehensive context for the Gemini CLI and other AI assistants working on the **ThinQ AI Assistant** project.

## Project Overview
A Spring Boot application that integrates **LG ThinQ IoT devices** with advanced LLMs (Gemini, Claude, Groq) using **Spring AI**. It allows users to control and monitor their home appliances through a natural language interface.

- **Primary Mission**: Act as a "ThinQ Smart Home AI Assistant" that provides insights and control over LG appliances.
- **Key Capabilities**: Device status monitoring, energy usage analysis, and appliance control via tool-calling.

## Tech Stack
- **Framework**: Spring Boot 4.0.5
- **AI Library**: Spring AI 2.0.0-M3 (Note: This is a milestone version with breaking changes from 1.x)
- **Java Version**: 17
- **Database**: H2 (In-memory) for development
- **API Documentation**: SpringDoc OpenAPI (Swagger) at `http://localhost:8081/swagger-ui.html`
- **Build Tool**: Gradle (Kotlin DSL)

## Architecture & Implementation Details

### Domain Structure (`com.example.demo.domain`)
- **`ai/`**: Contains the core chat logic.
    - `AiController`: REST endpoint (`POST /api/ai/chat`).
    - `AiService`: Orchestrates the `ChatClient` interaction, model selection, and tool attachment.
- **`thinq/`**: Integration with LG ThinQ API.
    - `tool/`: Each class (e.g., `ThinQDeviceTools`) uses `@Tool` annotations to expose methods to the LLM.
    - `dto/`: Response and request objects for the ThinQ API.

### AI Integration Strategy
- **`ChatClient` API**: Uses the fluent `ChatClient` builder pattern from Spring AI.
- **Advisors**:
    - `MessageChatMemoryAdvisor`: Manages conversation history (stored in `InMemoryChatMemoryRepository`).
    - `SimpleLoggerAdvisor`: Provides logging for prompt/response cycles.
- **System Prompt**: Loaded from `src/main/resources/prompts/system.st`. It defines the assistant's persona, mandatory KR country defaults, and strict `deviceId` handling rules.
- **Models**: Supports `geminiChatClient`, `claudeChatClient`, and `groqChatClient` (OpenAI-compatible).

### Tool Calling Workflow
1. LLM must call `getDevices` first to retrieve valid `deviceId` and `alias`.
2. LLM identifies the specific device based on user intent.
3. LLM calls specialized tools (`getDeviceState`, `getEnergyUsage`) using the verified `deviceId`.

## Environment Setup
Requires a `.env` file in the root directory (automatically loaded by `bootRun`):
```env
GEMINI_API_KEY=your_key
CLAUDE_API_KEY=your_key
THINQ_PAT_TOKEN=your_token
THINQ_API_KEY=your_key
THINQ_BASE_URL=https://api-kic.lgthinq.com
```

## Building and Running
- **Build**: `./gradlew build`
- **Run**: `./gradlew bootRun` (Server starts on port **8081**)
- **Test**: `./gradlew test`
- **Clean**: `./gradlew clean`

## Development Conventions
- **Standard Spring Boot**: Follow constructor injection and domain-driven packaging.
- **Error Handling**: Use `BaseException` and `ErrorCode` patterns found in `global/common/exception`.
- **API Consistency**: All responses should be wrapped in `ApiResponse<T>`.
- **Spring AI 2.0.0-M3 Specifics**:
    - Use `MessageChatMemoryAdvisor.builder(chatMemory)...build()`.
    - Avoid deprecated 1.x advisor patterns.
    - Reference `CallAroundAdvisor` and `AdvisedRequest` for custom advisors.

## Key Files
- `src/main/java/com/example/demo/domain/ai/service/AiService.java`: Main AI orchestration.
- `src/main/java/com/example/demo/global/config/AiConfig.java`: AI Bean configurations.
- `src/main/java/com/example/demo/domain/thinq/tool/ThinQDeviceTools.java`: Hardware integration tools.
- `src/main/resources/prompts/system.st`: AI behavior and safety rules.
