# Mr Moon - User Guide 🌙

**Mr Moon** is your personal task management assistant that helps you organize todos, deadlines, and events efficiently.

## 🚀 **Quick Start**

### **Installation**
1. **Download:** Get the latest `MrMoon.jar` from the releases page
2. **Requirements:** Java 17 or higher
3. **Run:** Double-click `MrMoon.jar` or use command line

### **Running Mr Moon**
```
# GUI (Recommended)
java -jar MrMoon.jar

# Command Line
java -cp MrMoon.jar duke.MrMoon
```


**Troubleshooting:**
- If double-click doesn't work: Right-click → "Open with Java"
- "Java not found" error: Install Java 17+ from oracle.com
- Permission errors: Run `chmod +x MrMoon.jar` (Mac/Linux)


## 📋 **Command Reference** *(Your Main Navigation)*

| Command          | What You Type                          | Example                                      |
|------------------|----------------------------------------|----------------------------------------------|
| **Create Tasks** |
| `todo`           | `todo [description]`                   | `todo Buy milk`                              |
| `deadline`       | `deadline [task] /by [date]`           | `deadline Report /by 2024-12-25`             |
| `event`          | `event [task] /from [date] /to [date]` | `event Meeting /from 2024-12-20 2pm /to 4pm` |
| **Manage Tasks** |
| `list`           | `list`                                 | Shows all your tasks                         |
| `mark`           | `mark [number]`                        | `mark 2` (completes task 2)                  |
| `unmark`         | `unmark [number]`                      | `unmark 2` (uncompletes task 2)              |
| `delete`         | `delete [number]`                      | `delete 3` (removes task 3)                  |
| **Find & View**  |
| `find`           | `find [word]`                          | `find meeting`                               |
| `on`             | `on [date]`                            | `on 2024-12-25` (agenda view)                |
| **Advanced**     |
| `update`         | `update [number]`                      | Interactive task editing                     |
| `clear`          | `clear`                                | Delete all tasks                             |
| `bye`            | `bye`                                  | Exit application                             |


## 📝 **Creating Tasks**

### **Todo Tasks**
Simple tasks without dates:
```
todo buy groceries
todo call mom
```


### **Deadline Tasks**
Tasks with due dates:
```
deadline submit assignment /by 25/12/2024
deadline pay bills /by 31/12/2024 1800
```


### **Event Tasks**
Tasks with start and end times:
```
event team meeting /from 20/12/2024 1400 /to 20/12/2024 1500
event vacation /from 25/12/2024 /to 30/12/2024
```


### **Date Formats You Can Use**
- `25/12/2024` (day/month/year)
- `25-12-2024` (day-month-year)
- `25.12.2024` (day.month.year)
- `25 Dec 2024` (day month year)
- `2024-12-25` (year-month-day)

### **Time Formats**
- `1400` (24-hour format)
- Add time after date: `25/12/2024 1400`


## 🎯 **Managing Tasks**

### **View Tasks**
- `list` - See all tasks
- `on 25/12/2024` - See tasks for specific date
- `find meeting` - Search for tasks containing "meeting"

### **Update Tasks**
Use `update [task number]` for step-by-step editing:
1. Choose what to change (description or date)
2. Enter new information
3. Confirm changes

### **Mark Complete**
- `mark 3` - Mark task 3 as done ✓
- `unmark 3` - Mark task 3 as not done

### **Delete Tasks**
- `delete 3` - Remove task 3
- `clear` - Remove all tasks (asks for confirmation)


## 💾 **Your Data**

**Automatic Save:** Tasks save to `data/duke.txt` automatically

**Custom Storage:** `java -jar duke.jar [your-file-path]`

**Backup:** Copy the `data` folder to keep your tasks safe


## ❓ **Troubleshooting**

**"Invalid date format"**
→ Try: `25/12/2024` or `2024-12-25`

**"Task not found"**
→ Use `list` to see current task numbers

**App won't start**
→ Check Java 17+ installed: `java -version`

**Data not saving**
→ Make sure app has write permissions in its folder


## 💡 **Tips**

- **Task numbers change** when you delete tasks - always check `list` first
- **Use keywords** in task names to find them easier with `find`
- **Check your agenda** with `on [today's date]` each morning
- **Times are optional** - just dates work fine for most tasks


**Happy task managing with Mr Moon! 🌙**