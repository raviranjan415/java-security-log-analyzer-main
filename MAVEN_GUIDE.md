# Maven Build Setup for Security Log Analyzer

## Project Structure

```
java-security-log-analyzer/
├── pom.xml                          # Maven configuration
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/soc/analyzer/
│   │   │       ├── SecurityLogAnalyzerGUI.java    # Main GUI application
│   │   │       ├── LogAnalyzer.java                # Log analysis tool
│   │   │       └── LoginChecker.java               # Interactive login checker
│   │   └── resources/                              # Application resources
│   └── test/
│       └── java/                                   # Unit tests (future)
├── target/                         # Compiled output (auto-generated)
├── README.md                        # Project documentation
├── HOW_TO_ADD_DATA.txt             # Data format guide
├── logins.txt                      # Sample login logs
├── blocked_ips.txt                 # Auto-generated blocked IPs
└── MAVEN_GUIDE.md                  # This file

```

## Prerequisites

- **Java 17 or later** (JDK)
- **Maven 3.6.0 or later**
- Windows/Linux/Mac

### Check Installations

```bash
java -version
javac -version
mvn -version
```

## Maven Commands

### 1. **Clean Build** (Remove old artifacts)
```bash
mvn clean
```

### 2. **Compile Source Code**
```bash
mvn compile
```

### 3. **Run Tests** (When added)
```bash
mvn test
```

### 4. **Package Application** (Create JAR)
```bash
mvn package
```

### 5. **Clean + Compile + Test + Package** (Full build)
```bash
mvn clean package
```

### 6. **Install to Local Repository**
```bash
mvn install
```

## Running the Application

### Option A: Using Maven Plugin
```bash
mvn exec:java -Dexec.mainClass="com.soc.analyzer.SecurityLogAnalyzerGUI"
```

### Option B: Using Compiled JAR (after `mvn package`)
```bash
java -cp target/security-log-analyzer-2.0.0.jar com.soc.analyzer.SecurityLogAnalyzerGUI
```

### Option C: Using Fat JAR (includes all dependencies)
```bash
java -jar target/security-log-analyzer-2.0.0.jar
```

## Build Configuration (pom.xml)

### Key Settings
- **Source/Target**: Java 17
- **Group ID**: com.soc
- **Artifact ID**: security-log-analyzer
- **Version**: 2.0.0
- **Packaging**: JAR

### Dependencies
- **JUnit 4.13.2** - Unit testing
- **SLF4J 2.0.7** - Logging API
- **Logback 1.4.11** - Logging implementation
- **Commons CLI 1.5.0** - Command-line parsing
- **GSON 2.10.1** - JSON processing (optional)

### Plugins
- **Maven Compiler Plugin** - Compiles Java source
- **Maven Shade Plugin** - Creates fat JAR with dependencies
- **Maven JAR Plugin** - Packages application
- **Maven Surefire Plugin** - Runs unit tests
- **Maven Assembly Plugin** - Creates distribution archives

## Workflow Examples

### Quick Development Build
```bash
mvn clean compile
```

### Build and Run GUI
```bash
mvn clean package
java -jar target/security-log-analyzer-2.0.0.jar
```

### Run with CLI Arguments (future enhancement)
```bash
mvn exec:java -Dexec.mainClass="com.soc.analyzer.LogAnalyzer"
```

### Skip Tests (fast build)
```bash
mvn clean package -DskipTests
```

### Display Dependency Tree
```bash
mvn dependency:tree
```

### Generate Project Report
```bash
mvn site
```

## Maven Directory Structure Explanation

```
src/
├── main/           # Production code
│   ├── java/       # Java source files
│   └── resources/  # Non-Java resources (config, data files)
└── test/           # Test code
    ├── java/       # JUnit test files
    └── resources/  # Test resources
```

## Target Directory Structure (after build)

```
target/
├── classes/                    # Compiled .class files
├── security-log-analyzer-2.0.0.jar              # Standard JAR
├── security-log-analyzer-2.0.0-jar-with-dependencies.jar  # Fat JAR
├── maven-archiver/            # Build metadata
└── surefire-reports/          # Test reports (if tests run)
```

## Troubleshooting

### Issue: "Maven not found"
**Solution**: Install Maven or add to PATH
```bash
# Windows: Add Maven bin folder to System PATH
# Linux/Mac: export PATH=$PATH:/path/to/maven/bin
```

### Issue: "Java 17 not found"
**Solution**: Install JDK 17+
```bash
# Verify Java version
java -version

# Update pom.xml if using different Java version
# Change <maven.compiler.source>17</maven.compiler.source>
```

### Issue: Build fails with encoding errors
**Solution**: Add encoding to pom.xml properties:
```xml
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
```

### Issue: Cannot find logins.txt
**Solution**: Ensure logins.txt is in project root or use full path in file chooser

## IDEs Integration

### IntelliJ IDEA
- Right-click project → Maven → Reload Project
- Run → Run 'SecurityLogAnalyzerGUI'

### Eclipse
- Project → Maven → Update Project

### VS Code
- Install Extension: "Maven for Java"
- Click Maven icon on sidebar to run commands

## Publishing to Repository (Future)

```bash
# Deploy to remote repository
mvn deploy

# Build source JAR
mvn source:jar

# Build JavaDoc
mvn javadoc:jar
```

## Next Steps

1. ✅ Clean Build: `mvn clean`
2. ✅ Compile: `mvn compile`
3. ✅ Package: `mvn package`
4. ✅ Run: `java -jar target/security-log-analyzer-2.0.0.jar`
5. 📝 Add Unit Tests in `src/test/java/`
6. 📝 Add Logging Configuration in `src/main/resources/`
7. 📝 Add Maven Shade Plugin for fat JAR distribution

---

**Version**: 2.0.0  
**Java**: 17+  
**Maven**: 3.6.0+  
**Last Updated**: May 12, 2026
