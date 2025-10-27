# AGENTS.md - GitBrowser Repository Guide

This file helps AI assistants (like Jules) and developers understand the structure, conventions, and automation in the GitBrowser repository.

---

## 📋 Project Overview

**Repository:** https://github.com/likhonalhabibi/GitBrowser  
**Project Name:** GitBrowse Sentinel  
**Type:** Android Browser Application  
**Language:** 100% Kotlin  
**Architecture:** MVVM (Model-View-ViewModel)  
**UI Framework:** Jetpack Compose

### Mission
A lightweight (<5MB), privacy-focused Android browser with advanced anti-fingerprinting technology and secure multi-account GitHub integration, fully automated via GitHub Actions.

---

## 🏗️ Repository Structure

```
GitBrowser/
├── .github/
│   └── workflows/              # GitHub Actions automation
│       ├── pull-request-check.yml
│       ├── continuous-integration.yml
│       ├── release-on-tag.yml
│       ├── nightly-build.yml
│       ├── dependency-update.yml
│       └── security-audit.yml
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/gitbrowser/sentinel/
│   │   │   │   ├── ui/              # Jetpack Compose screens & components
│   │   │   │   ├── viewmodel/       # MVVM ViewModels
│   │   │   │   ├── data/            # Data layer (repositories, DAOs)
│   │   │   │   ├── domain/          # Business logic & use cases
│   │   │   │   ├── github/          # GitHub API & multi-account manager
│   │   │   │   ├── privacy/         # Anti-fingerprinting engine
│   │   │   │   ├── security/        # Keystore & encryption utilities
│   │   │   │   └── browser/         # WebView engine & tab management
│   │   │   └── res/                 # Android resources
│   │   ├── test/                    # JUnit unit tests
│   │   └── androidTest/             # Espresso UI tests
│   └── build.gradle.kts             # App-level build configuration
├── buildSrc/                        # Custom Gradle build logic
├── docs/                            # Documentation
├── AGENTS.md                        # This file (AI assistant guide)
├── blueprint.md                     # Project blueprint & roadmap
├── README.md                        # User-facing documentation
├── CONTRIBUTING.md                  # Contribution guidelines
├── LICENSE                          # GPL-3.0 License
└── build.gradle.kts                 # Project-level build configuration
```

---

## 🤖 AI Assistant Guidelines

### When Working on This Repository

1. **Follow the Blueprint**: Always reference `blueprint.md` for project vision, features, and roadmap.

2. **Respect Core Principles**:
   - **Privacy First**: Never add tracking, analytics, or user data collection
   - **Lightweight**: Keep APK size under 5MB, optimize for performance
   - **Security**: Use Android Keystore for sensitive data, never plain text
   - **Automation**: Every feature must have CI/CD integration

3. **Code Style**:
   - Use `ktlint` formatting (enforced in CI)
   - Follow Kotlin coding conventions
   - Prefer immutability and functional programming
   - Use meaningful variable names (no single letters except standard iterators)

4. **Architecture Patterns**:
   - **MVVM**: ViewModels handle business logic, Views are pure UI
   - **Unidirectional Data Flow**: State flows from ViewModel to UI
   - **Dependency Injection**: Use Hilt for DI
   - **Repository Pattern**: Separate data sources from business logic

5. **Testing Requirements**:
   - Minimum 70% code coverage
   - Unit tests for all ViewModels and business logic
   - UI tests for critical user flows
   - Mock GitHub API calls in tests

---

## 🔧 Key Components & Agents

### 1. **Privacy Engine** (`app/src/main/java/.../privacy/`)

**Purpose**: Implements anti-fingerprinting and tracking protection.

**Key Classes**:
- `FingerprintProtectionManager`: Coordinates all anti-fingerprinting measures
- `CanvasRandomizer`: Randomizes canvas fingerprinting outputs
- `FontEnumerationBlocker`: Limits font access to prevent tracking
- `WebGLProtector`: Spoofs/blocks WebGL parameters
- `TrackerBlocker`: Ad and tracker blocking engine

**Input**: WebView requests, canvas operations, API calls  
**Output**: Modified/blocked requests to prevent fingerprinting

**Usage Example**:
```kotlin
val protectionManager = FingerprintProtectionManager(context)
protectionManager.applyToWebView(webView)
```

**When to Modify**:
- Adding new fingerprinting protection techniques
- Updating tracker blocklists
- Enhancing hardware API restrictions

---

### 2. **GitHub Multi-Account Manager** (`app/src/main/java/.../github/`)

**Purpose**: Securely manages multiple GitHub accounts and handles authentication.

**Key Classes**:
- `GitHubAccountManager`: Core account management logic
- `TokenEncryptionService`: Encrypts/decrypts tokens using Android Keystore
- `AccountSwitcher`: Handles switching between active accounts
- `GitHubApiClient`: Wrapper for GitHub REST API calls

**Input**: User credentials, GitHub OAuth tokens  
**Output**: Authenticated API calls, session management

**Security Conventions**:
- NEVER store tokens in plain text
- Always use `EncryptedSharedPreferences` or `Android Keystore`
- Implement token refresh logic
- Clear sensitive data on logout

**Usage Example**:
```kotlin
val accountManager = GitHubAccountManager(context)
accountManager.addAccount(username, encryptedToken)
accountManager.switchToAccount(accountId)
```

**When to Modify**:
- Adding new GitHub API integrations
- Implementing OAuth flow
- Adding account sync features

---

### 3. **Browser Engine** (`app/src/main/java/.../browser/`)

**Purpose**: Core browsing functionality using Android System WebView.

**Key Classes**:
- `BrowserEngine`: Main WebView wrapper
- `TabManager`: Manages multiple browser tabs
- `NavigationController`: Handles back/forward navigation
- `BookmarkManager`: Stores and retrieves bookmarks

**Input**: URLs, user navigation commands  
**Output**: Rendered web pages, tab state

**Performance Requirements**:
- Cold start < 2 seconds
- Tab switching < 100ms
- Memory usage < 50MB per tab

**Usage Example**:
```kotlin
val browser = BrowserEngine(context)
browser.loadUrl("https://github.com")
browser.applyPrivacySettings()
```

**When to Modify**:
- Adding new browser features
- Optimizing performance
- Implementing cookie management

---

### 4. **Security Module** (`app/src/main/java/.../security/`)

**Purpose**: Handles all encryption and secure storage operations.

**Key Classes**:
- `KeystoreManager`: Manages Android Keystore operations
- `EncryptionHelper`: AES encryption utilities
- `SecurePreferences`: Encrypted SharedPreferences wrapper
- `CertificatePinner`: Certificate pinning for GitHub API

**Security Standards**:
- Use AES-256 for encryption
- Use RSA-2048 for key exchange
- Implement certificate pinning for all external APIs
- Never log sensitive data

**Usage Example**:
```kotlin
val keystore = KeystoreManager(context)
val encryptedData = keystore.encrypt(sensitiveData)
SecurePreferences.putString("token", encryptedData)
```

**When to Modify**:
- Adding new secure storage needs
- Implementing biometric authentication
- Updating encryption algorithms

---

### 5. **GitHub Actions Workflows** (`.github/workflows/`)

**Purpose**: Automate testing, building, and releasing.

#### Workflow Agents:

**a) Pull Request Check Agent**
- **Trigger**: Every PR to `main` or `develop`
- **Actions**: Lint, test, build, check APK size
- **Output**: Pass/fail status, coverage reports
- **Secrets Used**: None

**b) Release Agent**
- **Trigger**: Git tag push (pattern: `v*.*.*`)
- **Actions**: Build signed APK, create release, upload artifacts
- **Output**: GitHub Release with signed APK
- **Secrets Required**:
  - `KEYSTORE_FILE` (base64 encoded)
  - `KEYSTORE_PASSWORD`
  - `KEY_ALIAS`
  - `KEY_PASSWORD`

**c) Security Audit Agent**
- **Trigger**: Daily + every push to `main`
- **Actions**: OWASP checks, dependency scanning, secret detection
- **Output**: Security report, GitHub issues if vulnerabilities found
- **Secrets Used**: None

**d) Dependency Update Agent**
- **Trigger**: Weekly (Mondays)
- **Actions**: Check for dependency updates, create PR
- **Output**: Automated PR with dependency updates
- **Secrets Used**: `GITHUB_TOKEN` (auto-provided)

---

## 📝 Coding Conventions

### Kotlin Style Guide

```kotlin
// ✅ Good: Clear, descriptive names
class GitHubAccountManager(
    private val context: Context,
    private val encryptionService: EncryptionService
) {
    suspend fun authenticateAccount(username: String, token: String): Result<Account> {
        // Implementation
    }
}

// ❌ Bad: Unclear, abbreviated names
class GHAccMgr(ctx: Context, encSvc: EncService) {
    fun auth(u: String, t: String): Res<Acc> {
        // Implementation
    }
}
```

### File Naming
- **Activities**: `*Activity.kt` (e.g., `MainActivity.kt`)
- **Fragments**: `*Fragment.kt` (e.g., `BrowserFragment.kt`)
- **ViewModels**: `*ViewModel.kt` (e.g., `BrowserViewModel.kt`)
- **Composables**: `*Screen.kt` or `*Component.kt`
- **Repositories**: `*Repository.kt`
- **Use Cases**: `*UseCase.kt`

### Package Organization
```
com.gitbrowser.sentinel/
├── ui/
│   ├── browser/
│   ├── github/
│   └── settings/
├── viewmodel/
├── data/
│   ├── local/
│   ├── remote/
│   └── repository/
├── domain/
│   ├── model/
│   └── usecase/
└── di/  # Dependency injection modules
```

---

## 🧪 Testing Conventions

### Unit Tests (`src/test/`)

```kotlin
// Naming: [MethodName]_[Scenario]_[ExpectedResult]
@Test
fun authenticateAccount_validToken_returnsSuccess() {
    // Arrange
    val manager = GitHubAccountManager(mockContext, mockEncryption)
    val token = "valid_token"
    
    // Act
    val result = runBlocking { manager.authenticateAccount("user", token) }
    
    // Assert
    assertTrue(result.isSuccess)
}
```

### UI Tests (`src/androidTest/`)

```kotlin
@Test
fun switchAccount_clicksSwitcher_changesActiveAccount() {
    // Given
    composeTestRule.setContent { BrowserScreen() }
    
    // When
    composeTestRule.onNodeWithTag("account_switcher").performClick()
    composeTestRule.onNodeWithText("Account 2").performClick()
    
    // Then
    composeTestRule.onNodeWithTag("active_username").assertTextEquals("Account 2")
}
```

---

## 🔐 Security Guidelines

### Never Commit These:
- ❌ API keys or tokens
- ❌ Keystore files
- ❌ Passwords or credentials
- ❌ User data or logs

### Always Use:
- ✅ GitHub Secrets for sensitive CI/CD data
- ✅ Android Keystore for token storage
- ✅ EncryptedSharedPreferences for settings
- ✅ HTTPS for all network requests
- ✅ Certificate pinning for GitHub API

### Security Checklist:
```kotlin
// ✅ Good: Secure token storage
val encrypted = keystoreManager.encrypt(token)
securePrefs.putString("github_token", encrypted)

// ❌ Bad: Plain text storage
sharedPrefs.edit().putString("github_token", token).apply()
```

---

## 🚀 Common Development Tasks

### 1. Adding a New Feature

```bash
# Create feature branch
git checkout -b feature/new-feature-name

# Make changes, following MVVM pattern:
# 1. Add domain models in domain/model/
# 2. Create use case in domain/usecase/
# 3. Implement repository in data/repository/
# 4. Create ViewModel in viewmodel/
# 5. Build UI in ui/

# Write tests
# Run locally
./gradlew test
./gradlew connectedAndroidTest

# Commit with conventional commits
git commit -m "feat: add new feature description"

# Push and create PR
git push origin feature/new-feature-name
```

### 2. Fixing a Bug

```bash
# Create bugfix branch
git checkout -b fix/bug-description

# Fix the issue
# Add regression test
# Commit
git commit -m "fix: resolve bug description"

# CI will automatically run tests
```

### 3. Releasing a New Version

```bash
# Ensure main branch is stable
git checkout main
git pull origin main

# Create and push tag
git tag v1.2.0
git push origin v1.2.0

# GitHub Actions will automatically:
# - Run all tests
# - Build signed APK
# - Create GitHub Release
# - Upload artifacts
```

---

## 📚 Key Dependencies

### Core Libraries
- **Jetpack Compose**: UI framework
- **Hilt**: Dependency injection
- **Coroutines & Flow**: Asynchronous operations
- **OkHttp**: Network requests
- **Retrofit**: GitHub API client (if used)
- **Room**: Local database (for bookmarks)

### Security Libraries
- **Android Keystore**: Secure key storage
- **EncryptedSharedPreferences**: Encrypted settings
- **Conscrypt**: TLS/SSL provider

### Testing Libraries
- **JUnit 5**: Unit testing
- **MockK**: Mocking framework
- **Espresso**: UI testing
- **Turbine**: Flow testing

---

## 🎯 Performance Targets

| Metric | Target | CI Check |
|--------|--------|----------|
| APK Size | < 5MB | ✅ Automated |
| Cold Start Time | < 2 seconds | ⚠️ Manual |
| Memory Usage | < 50MB/tab | ⚠️ Manual |
| Test Coverage | > 70% | ✅ Automated |
| Build Time (CI) | < 5 minutes | ✅ Automated |

---

## 🐛 Debugging Tips

### Local Debugging
```bash
# Run with verbose logging
./gradlew assembleDebug --info

# Check for dependency conflicts
./gradlew dependencies

# Run specific test
./gradlew test --tests "GitHubAccountManagerTest"

# Generate coverage report
./gradlew jacocoTestReport
```

### CI Debugging
- Check workflow logs in GitHub Actions tab
- Download build artifacts for failed builds
- Review APK size reports in PR comments
- Check security scan results in Issues tab

---

## 📞 Support & Contact

- **Repository**: https://github.com/likhonalhabibi/GitBrowser
- **Issues**: https://github.com/likhonalhabibi/GitBrowser/issues
- **Discussions**: https://github.com/likhonalhabibi/GitBrowser/discussions
- **Maintainer**: @likhonalhabibi

---

## 🔄 Keep This File Updated!

When you add new:
- **Modules/Packages**: Update structure section
- **CI/CD Workflows**: Update workflow agents section
- **Security Practices**: Update security guidelines
- **Dependencies**: Update dependencies section

**Last Updated**: 2025-10-27  
**Version**: 1.0.0  
**For Jules & AI Assistants**: This file is your guide to understanding and contributing to GitBrowser effectively! 🤖✨
