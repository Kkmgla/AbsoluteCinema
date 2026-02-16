# Empty State Messages in Orange Rectangles

## Current State

File: [feature/details/src/main/java/com/example/details/ui/DetailsScreen.kt](feature/details/src/main/java/com/example/details/ui/DetailsScreen.kt)

- **Interesting facts section** (lines 831-883): Uses a `Column` with `.clip(CARD_BORDER_SHAPE)`, `.border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE)`, and `.padding(12.dp)` to create an orange rectangle. When empty, shows "Нет информации о фактах" inside this styled container.

- **Reviews section** (lines 885-916): When `reviews.isEmpty()`, shows "Нет рецензий" as plain `Text` without any container (lines 906-914).

- **Images section** (lines 918-958): When `images.isEmpty()`, shows "Нет изображений" as plain `Text` without any container (lines 948-956).

---

## Changes Required

### 1. Wrap Reviews Empty State in Orange Rectangle

**Current:** Plain `Text` component when `reviews.isEmpty()` (lines 906-914).

**Change:** Wrap the empty state `Text` in a `Column` with the same styling as "Interesting facts":
- Replace lines 906-914 with:
  ```kotlin
  Column(
      modifier = Modifier
          .fillMaxWidth()
          .clip(CARD_BORDER_SHAPE)
          .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE)
          .padding(12.dp)
  ) {
      Text(
          text = "Нет рецензий",
          color = MaterialTheme.colorScheme.secondary,
          fontSize = 16.sp,
          modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
          textAlign = TextAlign.Center
      )
  }
  ```

**Result:** "Нет рецензий" appears inside an orange rectangle matching "Interesting facts" style.

---

### 2. Wrap Images Empty State in Orange Rectangle

**Current:** Plain `Text` component when `images.isEmpty()` (lines 948-956).

**Change:** Wrap the empty state `Text` in a `Column` with the same styling as "Interesting facts":
- Replace lines 948-956 with:
  ```kotlin
  Column(
      modifier = Modifier
          .fillMaxWidth()
          .clip(CARD_BORDER_SHAPE)
          .border(CARD_BORDER_WIDTH, colorResource(R.color.accent), CARD_BORDER_SHAPE)
          .padding(12.dp)
  ) {
      Text(
          text = "Нет изображений",
          color = MaterialTheme.colorScheme.secondary,
          fontSize = 16.sp,
          modifier = Modifier
              .fillMaxWidth()
              .padding(20.dp),
          textAlign = TextAlign.Center
      )
  }
  ```

**Result:** "Нет изображений" appears inside an orange rectangle matching "Interesting facts" style.

---

## Summary of Changes

| Location | Current | New |
|----------|---------|-----|
| Reviews empty state (lines 906-914) | Plain `Text` | `Column` with orange border + `Text` inside |
| Images empty state (lines 948-956) | Plain `Text` | `Column` with orange border + `Text` inside |

---

## Files to Modify

**Single file:** [feature/details/src/main/java/com/example/details/ui/DetailsScreen.kt](feature/details/src/main/java/com/example/details/ui/DetailsScreen.kt)

- Lines 906-914: Wrap "Нет рецензий" `Text` in a styled `Column` matching "Interesting facts" container.
- Lines 948-956: Wrap "Нет изображений" `Text` in a styled `Column` matching "Interesting facts" container.

---

## Verification

- When a movie has no reviews, "Нет рецензий" appears in an orange rectangle (same style as "Interesting facts").
- When a movie has no images, "Нет изображений" appears in an orange rectangle (same style as "Interesting facts").
- The rectangle uses `CARD_BORDER_SHAPE`, `CARD_BORDER_WIDTH`, and `colorResource(R.color.accent)` for consistent styling.
- Text remains centered and properly padded inside the rectangle.

---

## Note

The constants `CARD_BORDER_SHAPE`, `CARD_BORDER_WIDTH`, and `R.color.accent` are already defined/imported in the file, so no additional imports or constants are needed. The styling matches exactly how "Interesting facts" section displays its empty state.
