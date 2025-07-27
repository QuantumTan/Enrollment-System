# 🎓 Enrollment System

![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white) 
![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat&logo=github&logoColor=white)

---

## 📌 Project Overview

The **Enrollment System** is a simple Java-based application designed to manage student enrollments in educational institutions. It allows users to register students, update their information, and retrieve details efficiently using file-based storage.

---

## ✨ Features

- **Student Registration** – Add new students to the system
- **Information Management** – Update and retrieve student details
- **Data Persistence** – Save and load student records from a local file

---

## 🧰 Tech Stack

- **Language:** Java
- **Storage:** Text File (`student.txt`)
- **Version Control:** Git & GitHub

---

## 🧱 System Design

### 📋 Class Diagram

```plaintext
+-------------------------+
|        Student          |
+-------------------------+
| - id: int               |
| - name: String          |
| - age: int              |
| - course: String        |
+-------------------------+
| + register(): void      |
| + updateInfo(): void    |
| + getInfo(): String     |
+-------------------------+
```

> **Student Class** – Represents a student with attributes such as ID, name, age, and course. It includes methods for registration, updating information, and retrieving student details.

---

## 🚀 Installation

Follow these steps to run the project locally:

### 1. Clone the Repository
```bash
git clone https://github.com/QuantumTan/Enrollment-System.git
```

### 2. Navigate to the Project Directory
```bash
cd Enrollment-System
```

### 3. Compile the Java Files
```bash
javac studentEnrollmentSystem.java
```

### 4. Run the Application
```bash
java studentEnrollmentSystem
```

---

## ▶️ Usage

- To **register a new student**, call the `register()` method in the `Student` class
- To **update student information**, use the `updateInfo()` method
- To **retrieve student details**, use the `getInfo()` method

> If GUI (Swing) is implemented, just run the main GUI class (e.g., `Dashboard.java`) instead of the console.

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository  
2. Create a new branch:
   ```bash
   git checkout -b feature/YourFeature
   ```
3. Make your changes and commit:
   ```bash
   git commit -m "Add new feature"
   ```
4. Push to your branch:
   ```bash
   git push origin feature/YourFeature
   ```
5. Open a Pull Request

---

## 👨‍💻 Author

**Jonathan Peguit**  
[GitHub Profile](https://github.com/QuantumTan)
