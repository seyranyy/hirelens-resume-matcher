# HireLens Resume Matcher

HireLens Resume Matcher is a full-stack application that compares a candidate's resume with a job posting and generates a match analysis.

The project includes:

- a Spring Boot backend for resume upload, job posting creation, and match analysis
- a React + TypeScript frontend for submitting resumes and viewing results
- an AI-powered analysis flow
- a fallback keyword analysis flow when the AI response fails

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
```

---

## How It Works

The analysis flow works like this:

1. The user uploads a resume file
2. The backend extracts raw text from the uploaded resume
3. The user enters a job title, company, and job description
4. The frontend creates a job posting
5. The backend tries to run AI-based analysis
6. If the AI call fails or returns an invalid response, the system uses fallback keyword analysis
7. The frontend displays:
   - score
   - matched skills
   - missing skills
   - summary

---

## Backend API Flow

Main backend flow:

- `POST /api/resumes`  
  Uploads a resume file and extracts text

- `POST /api/job-postings`  
  Creates a job posting from manual input

- `POST /api/match-analyses`  
  Compares the uploaded resume and job posting

- `GET /api/resumes/{id}`  
  Returns uploaded resume data

- `GET /api/job-postings/{id}`  
  Returns job posting data

- `GET /api/match-analyses/{id}`  
  Returns analysis result

- `GET /api/match-analyses`  
  Returns all analysis results

---

## AI and Fallback Logic

HireLens first tries to generate the analysis with AI.

If the AI response:
- fails
- times out
- returns incomplete JSON
- cannot be parsed correctly

the system automatically switches to fallback keyword analysis.

This makes the project more reliable and ensures the user still receives a result even if the AI layer is unstable.

---

## Resume Parsing

Uploaded resumes are parsed on the backend.

Current support:
- PDF
- TXT

The extracted text is saved and used during analysis.

---

## Running the Project Locally

### 1. Clone the repository

```bash
git clone https://github.com/seyranyy/hirelens-resume-matcher.git
cd hirelens-resume-matcher
```

### 2. Run the backend

```bash
cd hirelens-be
./mvnw spring-boot:run
```

Backend runs on:

```text
http://localhost:8080
```

### 3. Run the frontend

Open a new terminal:

```bash
cd hirelens-fe
npm install
npm run dev
```

Frontend runs on Vite's local server, typically:

```text
http://localhost:5173
```

---

## Environment Setup

To use AI analysis, set your API key before running the backend.

Example:

```bash
export OPENROUTER_API_KEY="your_api_key_here"
```

If the key is missing or the AI call fails, fallback analysis will still work.

---

## Database

The backend uses PostgreSQL.

Example local configuration:

- database: `hirelens_db`
- username: `seyran`

Make sure your PostgreSQL server is running before starting the backend.

---

## Current Limitations

- AI response quality may vary depending on provider stability
- Fallback analysis is keyword-based, so it is simpler than AI analysis
- Resume parsing currently focuses on raw text extraction
- Job posting URL mode is not fully implemented yet
- Manual job posting entry is currently the main workflow

---

## Future Improvements

- Job posting URL support
- Better resume text extraction and normalization
- Saved analysis history page
- More detailed AI summaries
- Better skill grouping and normalization
- Authentication and user-specific data
- Improved dashboard and analytics UI

---

## Sample Use Case

Example workflow:

- Upload a candidate resume
- Enter a job title such as `Business Analyst`
- Enter company and job description
- Click **Analyze Resume**
- Review:
  - match score
  - matched skills
  - missing skills
  - summary

---

## Why This Project Matters

This project is more than a basic CRUD application.

It demonstrates:

- full-stack development
- frontend-backend integration
- file upload handling
- PDF text extraction
- AI integration
- fallback system design
- DTO-based API structure
- practical product thinking

---

## Author

Built by **Dilan Seyran Yuceli**

GitHub: [seyranyy](https://github.com/seyranyy)
