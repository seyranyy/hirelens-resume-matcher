# HireLens Resume Matcher

HireLens Resume Matcher is a full-stack application that compares a candidate's resume with a job posting and generates a match analysis.

The project includes:

- a **Spring Boot backend** for resume upload, job posting creation, and match analysis
- a **React + TypeScript frontend** for submitting resumes and viewing results
- an **AI-powered analysis flow**
- a **fallback keyword analysis** flow when the AI response fails

---

## Features

- Upload resume files
- Create job postings manually
- Compare a resume against a job posting
- Generate:
  - match score
  - matched skills
  - missing skills
  - analysis summary
- Use AI for analysis
- Fall back to keyword-based matching if AI fails
- View results in a modern frontend UI

---

## Tech Stack

### Backend
- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Apache PDFBox

### Frontend
- React
- TypeScript
- Vite
- Fetch API
- CSS

### AI / Analysis
- OpenRouter API
- Prompt-based resume/job comparison
- Keyword-based fallback analysis

---

## Project Structure

```text
hirelens-resume-matcher/
├── hirelens-be/   # Spring Boot backend
└── hirelens-fe/   # React frontend
