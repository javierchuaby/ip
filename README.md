# Mr Moon - Your Personal Task Manager

MrMoon is a sophisticated task management application built with Java and JavaFX that helps you organize your daily activities.

<br/>

## ✨ Key Features

### **Task Management**
- **Todo Tasks**: Simple tasks with descriptions
- **Deadline Tasks**: Tasks with due dates 
- **Event Tasks**: Tasks with start and end dates/times


### **Task Operations**
- **List Tasks**: View all your tasks with their completion status
- **Mark/Unmark**: Toggle task completion status
- **Delete Tasks**: Remove tasks by their index number
- **Find Tasks**: Search tasks by keywords
- **Agenda View**: View tasks scheduled for specific date (`on <date>`)
- **Clear Tasks**: Remove all tasks at once with confirmation prompt
- **Update Task**: Modify task descriptions and dates dynamically


### **Advanced Functionality**
- **Flexible Date Parsing**: Natural language date input support
- **Persistent Storage**: Automatic file-based task preservation
- **Search Capabilities**: Find tasks by keywords instantly
- **Bulk Operations**: Clear all tasks with confirmation prompts

<br/>

## 🚀 Quick Start

### **Prerequisites**
- **Java 17** or higher
- **Gradle** (included via wrapper)

### **Installation**

1. **Clone the repository**
   ```bash
   git clone git@github.com:javierchuaby/ip.git
   cd ip
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Run the application**

   **GUI Mode**
   ```bash
   ./gradlew run
   ```

<br/>

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
|`on <date>` | View tasks for specific date | `on 20 Sep 2025` |
| `find <keyword>` | Search tasks | `find meeting` |
| `clear` | Remove all tasks | `clear` |
| `bye` | Exit application | `bye` |

<br/>

## 📁 Project Structure

```
src/
├── main/
│   ├── java/duke/
│   │   ├── MrMoon.java          # CLI entry point
│   │   ├── command/             # Command implementations  
│   │   ├── gui/                 # JavaFX components
│   │   ├── parser/              # Input parsing logic
│   │   ├── storage/             # File persistence layer
│   │   ├── task/                # Task type definitions
│   │   ├── ui/                  # User interface handlers
│   │   └── util/                # Utility classes
│   └── resources/               # GUI assets (FXML, CSS)
└── test/                        # Unit test suite
```

<br/>

## 🛠️ Technical Details
### Architecture
- **Command Pattern**: Each user command is encapsulated as a Command object
- **MVC Architecture**: Separation of model (tasks), view (UI), and controller (MrMoon)
- **Parser System**: Robust input parsing with error handling
- **Storage System**: Automatic task persistence to local files

<br/>

## 🧪 **Build and Test**

**Run Tests:**
```bash
./gradlew test
```

**Create JAR:**
```bash
./gradlew shadowJar
```

**Run code quality checks:**
```bash
./gradlew checkstyleMain   # Style validation
```

<br/>

## 💾 Data Storage

### **Storage Configuration**
- **Default Location**: `.data/duke.txt`
- **Format**: Plain text serialization
- **Auto-Creation**: Directories and files created automatically

<br/>

## 🔧 Troubleshooting

### **Common Issues**

**Java Version Error:**
```bash
# Verify Java installation
java -version
# Ensure Java 17+ is installed
```

**Data File Problems:**
- Delete `.data/duke.txt` to reset storage
- Verify file system permissions
- Check directory structure integrity

<br/>

## 📄 License

This project is developed as an educational application for learning Java and software engineering principles.

<br/>
<br/>

**Built with Java 17, JavaFX, and Gradle** 🚀
