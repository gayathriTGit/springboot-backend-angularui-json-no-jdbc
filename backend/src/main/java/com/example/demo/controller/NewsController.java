package com.example.demo.controller;

import com.example.demo.model.News;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * NewsController - RESTful Web Service Controller
 *
 * This class demonstrates Spring Boot REST API development with:
 * - @RestController for JSON responses
 * - @GetMapping for HTTP GET endpoints
 * - @RequestParam for URL parameter handling
 * - AtomicLong for thread-safe request counting
 * - LinkedHashMap for structured JSON responses
 *
 * @author Your Name
 * @version 1.0
 */
@RestController  // Combines @Controller + @ResponseBody - automatically converts return values to JSON
public class NewsController {

    /**
     * AtomicLong for thread-safe request counting
     * - Thread-safe: Multiple users can access simultaneously without data corruption
     * - Atomic operations: increment and get operations are atomic (indivisible)
     * - Used to track total number of API requests across all endpoints
     */
    private final AtomicLong requestCounter = new AtomicLong();

    /**
     * Template for welcome messages using String.format()
     * %s = String placeholder that gets replaced with actual values
     */
    private static final String WELCOME_TEMPLATE = "Welcome to News Web App, %s! Your request was processed at %s";

    /**
     * Basic endpoint that returns the application name
     *
     * URL: GET /name
     * Returns: HTML string (not JSON because return type is String)
     * Example: http://localhost:9090/name
     */
    @GetMapping("/name")  // Maps HTTP GET requests to /name URL
    public String appName() {
        return "<h3>News Web App</h3>";  // Returns HTML directly
    }

    /**
     * Main news endpoint with optional filtering and limiting
     *
     * URL: GET /news
     * Optional Parameters:
     * - author: Filter news by author name (partial match, case-insensitive)
     * - limit: Maximum number of articles to return (default: 10)
     *
     * Examples:
     * - /news                           → All news (up to 10)
     * - /news?limit=3                   → First 3 news articles
     * - /news?author=john               → News by authors containing "john"
     * - /news?author=smith&limit=2      → First 2 news by authors containing "smith"
     *
     * @param authorFilter Optional author name filter (can be null)
     * @param limit Maximum number of articles (default value = 10)
     * @return LinkedHashMap that Spring Boot converts to JSON automatically
     */
    @GetMapping("/news")
    public LinkedHashMap<String, Object> getNews(
            @RequestParam(value = "author", required = false) String authorFilter,  // Optional parameter
            @RequestParam(value = "limit", defaultValue = "10") int limit) {        // Parameter with default value

        // LinkedHashMap maintains insertion order for consistent JSON field ordering
        // (unlike HashMap which has random field order in JSON output)
        LinkedHashMap<String, Object> newsMap = new LinkedHashMap<>();

        // Get all news articles (in real applications, this would come from a database)
        List<News> newsList = getAllNewsList();

        // FILTERING LOGIC: Filter by author if parameter is provided
        if (authorFilter != null && !authorFilter.isEmpty()) {
            List<News> filteredNews = new ArrayList<>();
            for (News news : newsList) {
                if (news.getAuthor().toLowerCase().contains(authorFilter.toLowerCase())) {
                    filteredNews.add(news);
                }
            }
            newsList = filteredNews;
        }

        // LIMITING LOGIC: Limit the number of articles if specified
        if (limit > 0 && limit < newsList.size()) {
            newsList = newsList.subList(0, limit);  // Get first 'limit' articles
        }

        // BUILD JSON RESPONSE: Following newsapi.org structure
        newsMap.put("status", "ok");                           // API status
        newsMap.put("total", newsList.size());                 // Number of articles returned
        newsMap.put("requestId", requestCounter.incrementAndGet());  // Unique request ID (thread-safe increment)
        newsMap.put("timestamp", new Date());                  // Current server time
        newsMap.put("articles", newsList);                     // Array of News objects (automatically converts to JSON)

        return newsMap;  // Spring Boot automatically converts to JSON using Jackson library
    }

    /**
     * Personalized welcome endpoint demonstrating @RequestParam usage
     *
     * URL: GET /welcome
     * Parameters:
     * - name: User's name (default: "Guest")
     * - category: News category preference (default: "general")
     *
     * Examples:
     * - /welcome                              → Welcome Guest, category: general
     * - /welcome?name=Alice                   → Welcome Alice, category: general
     * - /welcome?name=Bob&category=sports     → Welcome Bob, category: sports
     *
     * @param name User's name with default value
     * @param category News category with default value
     * @return Personalized welcome message in JSON format
     */
    @GetMapping("/welcome")
    public LinkedHashMap<String, Object> welcome(
            @RequestParam(value = "name", defaultValue = "Guest") String name,          // Default value used if parameter missing
            @RequestParam(value = "category", defaultValue = "general") String category) {

        LinkedHashMap<String, Object> response = new LinkedHashMap<>();

        // Use String.format() to insert values into template
        response.put("message", String.format(WELCOME_TEMPLATE, name, new Date()));
        response.put("category", category);
        response.put("requestId", requestCounter.incrementAndGet());  // Increment counter atomically
        response.put("timestamp", new Date());
        response.put("status", "ok");

        return response;  // Automatically converted to JSON
    }

    /**
     * Statistics endpoint showing AtomicLong usage for request tracking
     *
     * URL: GET /stats
     * Returns: Server statistics including total request count
     *
     * Demonstrates:
     * - AtomicLong.get() for reading current value
     * - Server monitoring capabilities
     * - System information endpoints
     */
    @GetMapping("/stats")
    public LinkedHashMap<String, Object> getStats() {
        LinkedHashMap<String, Object> stats = new LinkedHashMap<>();

        stats.put("totalRequests", requestCounter.get());    // Get current counter value (thread-safe read)
        stats.put("serverTime", new Date());                // Current server timestamp
        stats.put("status", "running");                     // Server status
        stats.put("applicationName", "News Web App");       // Application identifier

        return stats;
    }

    /**
     * Search news by specific author name (exact match)
     *
     * URL: GET /news/author
     * Required Parameter:
     * - name: Exact author name to search for
     *
     * Examples:
     * - /news/author?name=John Smith     → News by exactly "John Smith"
     * - /news/author?name=Sarah Johnson  → News by exactly "Sarah Johnson"
     *
     * Demonstrates:
     * - Required parameters (@RequestParam with required = true)
     * - Exact string matching vs partial matching
     * - Different response structure based on results
     *
     * @param authorName Required author name (exact match)
     * @return JSON response with filtered news or empty results
     */
    @GetMapping("/news/author")
    public LinkedHashMap<String, Object> getNewsByAuthor(
            @RequestParam(value = "name", required = true) String authorName) {  // Required parameter - API returns error if missing

        LinkedHashMap<String, Object> newsMap = new LinkedHashMap<>();

        // Get all news articles
        List<News> allNews = getAllNewsList();

        // Filter by exact author name (case-insensitive)
        List<News> filteredNews = new ArrayList<>();
        for (News news : allNews) {
            if (news.getAuthor().equalsIgnoreCase(authorName)) {
                filteredNews.add(news);
            }
        }

        // Dynamic status based on results
        newsMap.put("status", filteredNews.isEmpty() ? "no_results" : "ok");
        newsMap.put("searchedAuthor", authorName);              // Echo back what was searched
        newsMap.put("total", filteredNews.size());              // Number of results found
        newsMap.put("requestId", requestCounter.incrementAndGet());  // Unique request tracking
        newsMap.put("timestamp", new Date());
        newsMap.put("articles", filteredNews);                  // Results array (may be empty)

        return newsMap;
    }

    /**
     * POST endpoint to accept a new News article (demonstration only)
     *
     * URL: POST /news
     * Body: JSON representing a News object
     *
     * Example JSON:
     * {
     *   "author": "Jane Doe",
     *   "title": "New Article Title",
     *   "description": "Article description here."
     * }
     *
     * @param news News object from request body
     * @return Confirmation response with received news
     */
    @PostMapping("/news")
    public LinkedHashMap<String, Object> addNews(@RequestBody News news) {
        System.out.println("Calling the endpoint news!");
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        response.put("status", "received");
        response.put("receivedArticle", news);
        response.put("requestId", requestCounter.incrementAndGet());
        response.put("timestamp", new Date());
        return response;
    }

    /**
     * Helper method to create sample news data
     *
     * In a real application, this would:
     * - Connect to a database
     * - Use @Repository classes
     * - Implement proper data access layers
     * - Handle database exceptions
     *
     * For now, returns hard-coded sample data for demonstration
     *
     * @return List of News objects with sample data
     */
    private List<News> getAllNewsList() {
        List<News> newsList = new ArrayList<>();

        // Sample news articles - in production, this would come from database
        newsList.add(new News(
                "John Smith",
                "Tech Stocks Rise as Market Shows Optimism",
                "Technology stocks experienced significant gains today as investors show renewed confidence in the sector."
        ));

        newsList.add(new News(
                "Sarah Johnson",
                "Global Economic Outlook Improves",
                "Economists predict stronger growth for the coming quarter based on recent economic indicators."
        ));

        newsList.add(new News(
                "Mike Davis",
                "Renewable Energy Investments Surge",
                "Clean energy projects receive record funding as companies shift towards sustainable practices."
        ));

        newsList.add(new News(
                "Emily Chen",
                "Cryptocurrency Market Stabilizes",
                "Digital currencies show signs of stability after weeks of volatility in the market."
        ));

        newsList.add(new News(
                "Robert Wilson",
                "Healthcare Innovation Breakthrough",
                "Medical researchers announce promising results in new treatment methodologies."
        ));

        newsList.add(new News(
                "Lisa Brown",
                "E-commerce Growth Continues Strong",
                "Online retail sales maintain upward trend as consumer behavior shifts permanently."
        ));

        newsList.add(new News(
                "David Taylor",
                "Manufacturing Sector Shows Recovery",
                "Industrial production increases for the third consecutive month, signaling economic recovery."
        ));

        return newsList;  // Return the complete list
    }
}

/*
 * =============================================================================
 * TESTING GUIDE - How to test all endpoints:
 * =============================================================================
 *
 * 1. Basic Endpoints:
 *    GET http://localhost:9090/name
 *    GET http://localhost:9090/news
 *    GET http://localhost:9090/stats
 *
 * 2. Welcome with Parameters:
 *    GET http://localhost:9090/welcome
 *    GET http://localhost:9090/welcome?name=Alice
 *    GET http://localhost:9090/welcome?name=Bob&category=sports
 *
 * 3. News with Filtering:
 *    GET http://localhost:9090/news?limit=3
 *    GET http://localhost:9090/news?author=john
 *    GET http://localhost:9090/news?author=smith&limit=2
 *
 * 4. Author Search:
 *    GET http://localhost:9090/news/author?name=John Smith
 *    GET http://localhost:9090/news/author?name=Sarah Johnson
 *    GET http://localhost:9090/news/author?name=NonExistent
 *
 * 5. Watch Request Counter:
 *    - Call any endpoint multiple times
 *    - Check /stats to see requestId incrementing
 *
 * =============================================================================
 * KEY CONCEPTS DEMONSTRATED:
 * =============================================================================
 *
 * 1. @RestController vs @Controller:
 *    - @RestController = @Controller + @ResponseBody
 *    - Automatically converts return values to JSON
 *    - No need for view templates
 *
 * 2. @GetMapping vs @RequestMapping:
 *    - @GetMapping is shorthand for @RequestMapping(method = RequestMethod.GET)
 *    - More readable and concise
 *    - Type-safe HTTP method specification
 *
 * 3. @RequestParam Options:
 *    - value: Parameter name in URL
 *    - defaultValue: Used when parameter is missing
 *    - required: true/false (default: true unless defaultValue provided)
 *
 * 4. AtomicLong Benefits:
 *    - Thread-safe operations
 *    - No synchronized blocks needed
 *    - Better performance than synchronized counters
 *    - Atomic increment-and-get operations
 *
 * 5. JSON Conversion:
 *    - Spring Boot uses Jackson library automatically
 *    - Java objects → JSON serialization
 *    - Uses getter methods to determine JSON properties
 *    - LinkedHashMap preserves field order in JSON
 *
 * 6. Stream API Usage:
 *    - Functional programming for filtering
 *    - filter() for conditional selection
 *    - collect() for converting back to collections
 *    - Method chaining for readable code
 *
 * =============================================================================
 */