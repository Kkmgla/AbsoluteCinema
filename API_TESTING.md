# Kinopoisk API Testing

## Setup

```bash
export KINOPOISK_API_KEY=$(grep KINOPOISK_API_KEY local.properties | cut -d'=' -f2)
```

---

## Automated Test Script

```bash
#!/bin/bash

if [ -z "$KINOPOISK_API_KEY" ]; then
    if [ -f "local.properties" ]; then
        KINOPOISK_API_KEY=$(grep KINOPOISK_API_KEY local.properties | cut -d'=' -f2)
    fi
fi

if [ -z "$KINOPOISK_API_KEY" ]; then
    echo "Error: KINOPOISK_API_KEY is not set."
    exit 1
fi

API_KEY="$KINOPOISK_API_KEY"
BASE_URL="https://api.kinopoisk.dev"

echo "=== Testing Kinopoisk API ==="

echo "1. Search by name..."
curl -X GET "${BASE_URL}/v1.4/movie/search?query=матрица&page=1&limit=1" \
  -H "X-API-KEY: ${API_KEY}" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | head -20
echo "---"

echo "2. Get movie by ID (301)..."
curl -X GET "${BASE_URL}/v1.4/movie/301" \
  -H "X-API-KEY: ${API_KEY}" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | head -30
echo "---"

echo "3. Filtered search..."
curl -X GET "${BASE_URL}/v1.4/movie?rating.kp=8-10&sortField=rating.kp&sortType=-1&page=1&limit=2" \
  -H "X-API-KEY: ${API_KEY}" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}\n" \
  -s | head -30
echo "---"

echo "4. API availability..."
curl -X GET "${BASE_URL}/v1.4/movie?page=1&limit=1" \
  -H "X-API-KEY: ${API_KEY}" \
  -H "Content-Type: application/json" \
  -w "\nHTTP Status: %{http_code}\n" \
  -s -o /dev/null

echo "=== Tests completed ==="
```

---

## Manual Commands

### Search Movie
```bash
curl -X GET "https://api.kinopoisk.dev/v1.4/movie/search?query=матрица&page=1&limit=5" \
  -H "X-API-KEY: ${KINOPOISK_API_KEY}" \
  -H "Content-Type: application/json"
```

### Get Movie by ID
```bash
curl -X GET "https://api.kinopoisk.dev/v1.4/movie/301" \
  -H "X-API-KEY: ${KINOPOISK_API_KEY}" \
  -H "Content-Type: application/json"
```

### Filtered Search
```bash
# Top rated
curl -X GET "https://api.kinopoisk.dev/v1.4/movie?rating.kp=8-10&sortField=rating.kp&sortType=-1&page=1&limit=5" \
  -H "X-API-KEY: ${KINOPOISK_API_KEY}" \
  -H "Content-Type: application/json"

# By year
curl -X GET "https://api.kinopoisk.dev/v1.4/movie?year=2020-2024&page=1&limit=5" \
  -H "X-API-KEY: ${KINOPOISK_API_KEY}" \
  -H "Content-Type: application/json"

# By genre
curl -X GET "https://api.kinopoisk.dev/v1.4/movie?genres.name=драма&page=1&limit=5" \
  -H "X-API-KEY: ${KINOPOISK_API_KEY}" \
  -H "Content-Type: application/json"
```

### Reviews
```bash
curl -X GET "https://api.kinopoisk.dev/v1.4/review?movieId=301&page=1&limit=5" \
  -H "X-API-KEY: ${KINOPOISK_API_KEY}" \
  -H "Content-Type: application/json"
```

### Images
```bash
curl -X GET "https://api.kinopoisk.dev/v1.4/image?movieId=301&page=1&limit=5" \
  -H "X-API-KEY: ${KINOPOISK_API_KEY}" \
  -H "Content-Type: application/json"
```

### Filters
```bash
curl -X GET "https://api.kinopoisk.dev/v1/movie/possible-values-by-field?field=genres.name" \
  -H "X-API-KEY: ${KINOPOISK_API_KEY}" \
  -H "Content-Type: application/json"
```

---

## PowerShell

```powershell
$apiKey = $env:KINOPOISK_API_KEY
if (-not $apiKey) {
    $props = Get-Content local.properties | Where-Object { $_ -match "KINOPOISK_API_KEY" }
    if ($props) { $apiKey = ($props -split '=')[1] }
}

Invoke-RestMethod -Uri "https://api.kinopoisk.dev/v1.4/movie/search?query=матрица&page=1&limit=5" `
  -Headers @{"X-API-KEY"=$apiKey; "Content-Type"="application/json"} `
  -Method Get | ConvertTo-Json -Depth 10
```

---

## Status Codes

- **200** - Success
- **401** - Invalid API key
- **429** - Rate limit
- **500** - Server error
