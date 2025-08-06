# Spring Boot REST API Exercise: Book Web App

Welcome to the Book Web App exercise! In this activity, you'll learn how to create a RESTful API endpoint using Spring Boot and provide some sample data to be returned as JSON. Follow the instructions below to complete the exercise.

---

## Objectives
- Create a REST endpoint using Spring Boot
- Return JSON data from your endpoint
- Practice using annotations like `@RestController`, `@GetMapping`, and `@RequestParam`
- Provide hard-coded sample data (no database required)

---

## Instructions

### 1. Set Up Your Controller
- Create a new Java class in the `controller` package (e.g., `BookController`).
- Annotate your class with `@RestController`.

### 2. Define a Model Class
- Create a simple `Book` model class in the `model` package with fields like `author`, `title`, and `summary`.
- Add appropriate getters, setters, and constructors.

### 3. Create a GET Endpoint
- In your controller, create a method that handles HTTP GET requests (e.g., `/books`).
- Use the `@GetMapping` annotation to map the endpoint.
- The method should return a list of `Book` objects as JSON.

### 4. Provide Sample Data
- Inside your controller, create a method that returns a hard-coded list of `Book` objects (no database needed).
- Use this method to supply data to your GET endpoint.

### 5. (Optional) Add Filtering
- Add an optional URL parameter (e.g., `author`) to filter books by author name.
- Use the `@RequestParam` annotation for this parameter.

---

## Example (Partial, Not Full Solution)

```java
@RestController
public class BookController {
    @GetMapping("/books")
    public List<Book> getBooks(/* parameters here */) {
        // Create and return a list of Book objects
        // Optionally filter by author if parameter is provided
    }
}
```

```java
public class Book {
    private String author;
    private String title;
    private String summary;
    // Getters, setters, constructors
}
```

---

## Testing Your Endpoint
- Run your Spring Boot application.
- Open your browser or use a tool like Postman to visit your `/books` endpoint.
- The base URL will depend on your application's configuration (e.g., `http://localhost:PORT/books`).
- You should see a JSON array of books.
- Try adding a query parameter, e.g., `?author=Jane` to filter by author.

---

## Tips
- Use `List<Book>` as your return type for automatic JSON conversion.
- Use `@RequestParam(value = "author", required = false)` for optional parameters.
- Hard-code at least 3-5 sample books.

---

## Challenge
- Add another endpoint (e.g., `/books/author`) that returns books by exact author name.
- Try adding a POST endpoint to accept new books (bonus).

---
