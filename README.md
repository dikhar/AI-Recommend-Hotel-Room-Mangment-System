# 🏨 AI-Powered Room Management System

An intelligent **Room Management System** that combines traditional room management capabilities with **AI-powered recommendations using Google Gemini**. The system recommends rooms based on user preferences and provides explanations for why a particular room is suitable.

The application is designed with **fault tolerance and graceful degradation** in mind. Gemini enhances the recommendation experience but is not a hard dependency—the system continues to work using a deterministic fallback when AI is unavailable.

## ✨ Features

* User and room management
* Room availability and search
* User preference management
* AI-powered room recommendations
* AI-generated explanations for recommendations
* Deterministic fallback recommendation engine
* Graceful handling of Gemini API failures
* Environment-based API key configuration
* Modular recommendation architecture

## 🤖 AI Room Recommendations

The **User Service** uses Google Gemini to select and explain room recommendations when a Gemini API key is configured.

The recommendation process considers factors such as:

* Number of guests
* Room type
* Budget
* Available amenities
* Bed preferences
* User preferences

Example:

```text
User Preferences
       │
       ▼
   User Service
       │
       ▼
Recommendation Engine
       │
       ├───────────────┐
       ▼               ▼
   Gemini          Fallback
       │               │
       └───────┬───────┘
               ▼
       Room Recommendation
               │
               ▼
        User + Explanation
```

Gemini can provide recommendations such as:

> **Deluxe King Room** — Recommended because it matches your preferred room type, supports two guests, includes Wi-Fi and breakfast, and fits within your budget.

## 🔑 Gemini Configuration

Gemini integration is optional.

Set the `GEMINI_API_KEY` environment variable before starting the User Service:

```bash
export GEMINI_API_KEY="your-api-key"
```

For Windows:

```powershell
$env:GEMINI_API_KEY="your-api-key"
```

**Never commit API keys or other secrets to the repository.**

## 🛡️ Fallback Mechanism

The application does not depend completely on Gemini.

If:

* `GEMINI_API_KEY` is not configured
* Gemini cannot be reached
* The API request fails
* A timeout occurs
* Gemini returns invalid or unusable data

the system automatically uses its **deterministic recommendation fallback**.

```text
Gemini Available?
      │
   ┌──┴──┐
  Yes    No
   │      │
   ▼      ▼
 Gemini  Fallback
   │      │
   └──┬───┘
      ▼
Recommendation
```

This ensures that external AI failures do not affect the core functionality of the Room Management System.

## 🏗️ Design

The recommendation functionality follows a **Strategy Pattern**, keeping AI-specific logic separate from core business logic.

```text
RecommendationStrategy
        │
        ├── GeminiRecommendationStrategy
        │
        └── FallbackRecommendationStrategy
```

This makes the system easier to test, maintain, and extend. Additional recommendation providers or algorithms can be added without modifying the core room-management functionality.

## 🚀 Future Enhancements

* Recommendation caching with Redis
* Personalized recommendations using booking history
* Embedding-based room similarity
* Vector database integration
* AI-based price/value comparison
* Recommendation analytics
* A/B testing of recommendation strategies

## 💡 Key Design Principle

**AI enhances the application but does not control its availability.**

When Gemini is available, users receive intelligent and explainable recommendations. When it is unavailable, the deterministic fallback ensures the system remains reliable and functional.
