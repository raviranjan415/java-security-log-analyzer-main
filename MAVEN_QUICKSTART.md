# 🚀 Maven Quick Start Guide

## Installation & Verification

### Check Java & Maven
```powershell
java -version
mvn -version
```

Expected output:
- Java 17+ (openjdk or Oracle)
- Maven 3.6.0+

---

## 📦 Build Commands (Windows PowerShell)

### 1. Fresh Build (Clean + Compile)
```powershell
mvn clean compile
```
**Time**: ~10 seconds  
**Output**: Compiled Java files in `target/classes/`

### 2. Package as JAR
```powershell
mvn package -DskipTests
```
**Time**: ~5 seconds  
**Output**: Two JAR files in `target/`:
- `security-log-analyzer-2.0.0.jar` (without dependencies)
- `original-security-log-analyzer-2.0.0.jar` (backup)

### 3. Full Build (Clean + Compile + Test + Package)
```powershell
mvn clean package
```
**Time**: ~20 seconds (with tests when added)

### 4. Install to Local Repository
```powershell
mvn install
```
**Usage**: For using this project as dependency in other Maven projects

---

## ▶️ Running the Application

### **Option 1: Run from Compiled Classes (Development)**
```powershell
mvn exec:java -Dexec.mainClass="com.soc.analyzer.SecurityLogAnalyzerGUI"
```

### **Option 2: Run from Packaged JAR (Production)**
```powershell
cd target
java -jar security-log-analyzer-2.0.0.jar
```

### **Option 3: Run Specific Class**

**Log Analyzer (CLI)**:
```powershell
mvn exec:java -Dexec.mainClass="com.soc.analyzer.LogAnalyzer"
```

**Login Checker (Interactive)**:
```powershell
mvn exec:java -Dexec.mainClass="com.soc.analyzer.LoginChecker"
```

---

## 📁 Project Structure Overview

```
java-security-log-analyzer/
├── pom.xml                          ← Maven configuration
├── src/
│   ├── main/java/com/soc/analyzer/
│   │   ├── SecurityLogAnalyzerGUI.java
│   │   ├── LogAnalyzer.java
│   │   └── LoginChecker.java
│   └── test/java/                   ← Unit tests (empty, future use)
├── target/                          ← Build output
│   ├── classes/                     ← Compiled .class files
│   └── *.jar                        ← Packaged applications
├── logins.txt                       ← Sample login logs
├── pom.xml                          ← Maven config
├── MAVEN_GUIDE.md                   ← Detailed Maven documentation
└── HOW_TO_ADD_DATA.txt              ← Data format guide
```

---

## 🔧 Maven Useful Commands

| Command | Purpose |
|---------|---------|
| `mvn clean` | Remove `target/` directory |
| `mvn compile` | Compile source code only |
| `mvn test` | Run unit tests |
| `mvn package` | Create JAR file |
| `mvn verify` | Run integration tests |
| `mvn install` | Install to local repository |
| `mvn deploy` | Upload to remote repository |
| `mvn dependency:tree` | Show dependency hierarchy |
| `mvn help:active-profiles` | Show active profiles |
| `mvn clean package -X` | Verbose mode (debugging) |

---

## ✨ Example Workflows

### Workflow 1: Development Build & Test
```powershell
mvn clean compile
mvn exec:java -Dexec.mainClass="com.soc.analyzer.SecurityLogAnalyzerGUI"
```

### Workflow 2: Production Release
```powershell
mvn clean package -DskipTests
java -jar target/security-log-analyzer-2.0.0.jar
```

### Workflow 3: Quick Recompile (Skip Clean)
```powershell
mvn compile
mvn exec:java -Dexec.mainClass="com.soc.analyzer.SecurityLogAnalyzerGUI"
```

### Workflow 4: Build & Run CLI Tool
```powershell
mvn clean compile
mvn exec:java -Dexec.mainClass="com.soc.analyzer.LogAnalyzer"
```

---

## 📊 Dependencies Installed

Maven automatically downloads and manages:

```
├── JUnit 4.13.2              (Testing)
├── SLF4J 2.0.7               (Logging API)
├── Logback 1.4.11            (Logging Implementation)
├── Commons CLI 1.5.0         (Command-line parsing)
└── GSON 2.10.1               (JSON processing)
```

View all dependencies:
```powershell
mvn dependency:tree
```

---

## 🐛 Troubleshooting

### Error: "Maven is not recognized"
**Solution**: Install Maven or add to PATH
- Download: https://maven.apache.org/download.cgi
- Extract and add `bin/` folder to system PATH

### Error: "Unsupported class file format"
**Solution**: Java version mismatch
```powershell
java -version
javac -version
```
Ensure both are Java 17+

### Error: "Failed to execute goal"
**Solution**: Clear Maven cache
```powershell
mvn clean
rm -r ~/.m2/repository  # On Windows, manually delete C:\Users\YourName\.m2\repository
```

### Error: "Cannot find logins.txt"
**Solution**: File is in project root. Use file chooser in GUI or place in `target/` directory

---

## 📝 Maven Configuration (pom.xml)

**Key Settings**:
- **Language**: Java 17
- **Package**: com.soc.analyzer
- **Version**: 2.0.0
- **Main Class**: com.soc.analyzer.SecurityLogAnalyzerGUI

To modify:
1. Edit `pom.xml`
2. Run `mvn clean compile`
3. Changes take effect immediately

---

## 📌 IDE Integration

### VS Code
1. Install "Extension Pack for Java"
2. Run command from terminal in VS Code
3. Watch build progress in Output panel

### IntelliJ IDEA
1. File → Open as Maven Project
2. Maven panel → Lifecycle → double-click `package`
3. Or run via terminal

### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Right-click Project → Run As → Maven build
3. Enter goal (e.g., `clean compile`)

---

## 🎯 Next Steps

1. ✅ **Maven installed** - Verify with `mvn -version`
2. ✅ **Project structure** - In `src/main/java/com/soc/analyzer/`
3. ✅ **pom.xml** - Configured with all dependencies
4. ✅ **Build succeeds** - Run `mvn clean package`
5. 📝 **Next**: Add unit tests in `src/test/java/`
6. 📝 **Next**: Configure logging in `src/main/resources/logback.xml`
7. 📝 **Next**: Create distribution package with `mvn assembly:single`

---

## 🔗 Useful Links

- Maven Docs: https://maven.apache.org/
- POM Reference: https://maven.apache.org/pom.html
- Central Repository: https://repo.maven.apache.org/maven2/

---

**Last Updated**: May 12, 2026  
**Maven Version**: 3.6.0+  
**Java Version**: 17+
