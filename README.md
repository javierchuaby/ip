# Mr Moon - Your Personal Task Manager

**A powerful and intuitive Java-based task management application with dual CLI/GUI interface support**


Mr Moon is a comprehensive task management application that bridges the gap between command-line efficiency and graphical user experience. Built with Java 17 and JavaFX, it provides seamless task organization with persistent storage and intelligent date parsing.



## ✨ Key Features

### **Task Management**
- **Todo Tasks**: Simple task tracking with descriptions
- **Deadline Tasks**: Time-sensitive tasks with due dates  
- **Event Tasks**: Scheduled activities with start and end times
- **Smart Updates**: Modify task descriptions and dates dynamically

### **Dual Interface Support**
- **CLI Mode**: Terminal-based interface for power users
- **GUI Mode**: JavaFX-powered graphical interface for intuitive interaction
- **Seamless Switching**: Choose your preferred interaction method

### **Advanced Functionality**
- **Flexible Date Parsing**: Natural language date input support
- **Persistent Storage**: Automatic file-based task preservation
- **Search Capabilities**: Find tasks by keywords instantly
- **Bulk Operations**: Clear all tasks with confirmation prompts



## 🚀 Quick Start

### **Prerequisites**
- **Java 17** or higher
- **Gradle** (included via wrapper)
- **JavaFX Runtime** (for GUI mode)

### **Installation**

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ip
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run the application**

   **GUI Mode (Recommended)**
   ```bash
   ./gradlew run
   ```

   **CLI Mode**
   ```bash
   java -cp build/classes/java/main duke.MrMoon
   ```

### **Platform-Specific Commands**

**Windows:**
```batch
gradlew.bat run
```

**macOS/Linux:**
```bash
./gradlew run
```


## 📖 Usage Guide

### **Core Commands**

| Command | Description | Example |
|---------|-------------|---------|
| `todo <description>` | Create a simple task | `todo read book` |
| `deadline <description> /by <date>` | Create deadline task | `deadline submit assignment /by 2023-10-15 2359` |
| `event <description> /from <date> /to <date>` | Create event task | `event project meeting /from 2023-10-15 1400 /to 1600` |
| `list` | Display all tasks | `list` |
| `mark <index>` | Mark task as completed | `mark 1` |
| `unmark <index>` | Mark task as incomplete | `unmark 1` |
| `delete <index>` | Remove task | `delete 1` |
| `update <index>` | Modify existing task | `update 1` |
| `find <keyword>` | Search tasks | `find meeting` |
| `clear` | Remove all tasks | `clear` |
| `bye` | Exit application | `bye` |

### **Flexible Date Formats**

Mr. Moon supports multiple date input formats :

- **ISO Format**: `2023-10-15`
- **Standard Format**: `15/10/2023`
- **Natural Language**: `Oct 15 2023`
- **With Time**: `2023-10-15 1400`
- **12-Hour Format**: `15/10/2023 2:00pm`
- **Relative Dates**: `today`, `tomorrow`, `next Monday`

### **Task Update Process**

When using the `update` command:
1. **Select Update Type**: Choose description (`1` or `rename`) or date (`2` or `edit date`)
2. **Enter New Values**: Follow guided prompts for input
3. **Confirmation**: Review updated task details



## 🛠️ Development

### **Project Structure**

```
src/
├── main/
│   ├── java/duke/
│   │   ├── MrMoon.java          # CLI entry point
│   │   ├── command/             # Command implementations  
│   │   ├── gui/                 # JavaFX components
│   │   │   ├── Launcher.java    # GUI launcher
│   │   │   └── Main.java        # JavaFX application
│   │   ├── parser/              # Input parsing logic
│   │   ├── storage/             # File persistence layer
│   │   ├── task/                # Task type definitions
│   │   ├── ui/                  # User interface handlers
│   │   └── util/                # Utility classes
│   └── resources/               # GUI assets (FXML, CSS)
└── test/                        # Unit test suite
```

### **Build Commands**

**Compile and Build:**
```bash
./gradlew build
```

**Run Tests:**
```bash
./gradlew test
```

**Create JAR:**
```bash
./gradlew shadowJar
java -jar build/libs/duke.jar
```

**Code Quality:**
```bash
./gradlew spotlessApply    # Format code
./gradlew checkstyleMain   # Style validation
```



## 📊 Sample Session

```
Hello! I'm Mr. Moon
What can I do for you?

>> todo read book
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.

>> deadline submit report /by 2023-10-15 2359
Got it. I've added this task:
  [D][ ] submit report (by: Oct 15 2023 11:59 PM)
Now you have 2 tasks in the list.

>> list
Here are the tasks in your list:
1.[T][ ] read book
2.[D][ ] submit report (by: Oct 15 2023 11:59 PM)

>> mark 1
Nice! I've marked this task as done:
  [T][X] read book

>> bye
Bye. Hope to see you again soon!
```


## 💾 Data Storage

### **Storage Configuration**
- **Default Location**: `data/duke.txt`
- **Custom Location**: Specify as command-line argument
- **Format**: Plain text serialization
- **Auto-Creation**: Directories and files created automatically

### **Custom Data File**
```bash
java -cp build/classes/java/main duke.MrMoon path/to/custom/datafile.txt
```


## 🔧 Troubleshooting

### **Common Issues**

**Java Version Error:**
```bash
# Verify Java installation
java -version
# Ensure Java 17+ is installed
```

**Data File Problems:**
- Delete `data/duke.txt` to reset storage
- Verify file system permissions
- Check directory structure integrity


## 🧪 Testing

### **Running Tests**
```bash
./gradlew test                    # All tests
./gradlew test --tests "*.TaskTest"  # Specific test class
```

### **Test Coverage**
- Unit tests for core functionality
- Integration tests for file operations
- GUI component testing with JavaFX


## 🤝 Contributing

### **Development Guidelines**
1. **Code Style**: Follow established Java conventions
2. **Testing**: Ensure all tests pass before submission
3. **Documentation**: Update README for new features
4. **Build Verification**: Run `./gradlew build` successfully

### **Contribution Process**
1. Fork the repository
2. Create feature branch (`git checkout -b feature/new-feature`)
3. Commit changes (`git commit -m 'Add new feature'`)
4. Push branch (`git push origin feature/new-feature`)
5. Open Pull Request


## 📄 License

This project is developed as an educational application for learning Java and software engineering principles.


## 🆘 Support

### **Getting Help**
- Use `help` command within the application
- Review troubleshooting section above
- Check sample usage examples
- Examine project structure documentation

**Built with Java 17, JavaFX, and Gradle** 🚀
