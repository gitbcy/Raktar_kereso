# Raktár Kereső (Warehouse Locator) — Android App

A simple, fully offline Android app (Kotlin, MVVM, Room/SQLite) for locating
which warehouse and storage unit a product is stored in. All UI text is in
Hungarian, as required.

## Project structure

```
RaktarKereso/
├── build.gradle                  (project-level)
├── settings.gradle
├── gradle.properties
└── app/
    ├── build.gradle               (module-level, dependencies)
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/example/raktarkereso/
        │   ├── MainActivity.kt            (search screen)
        │   ├── AddItemActivity.kt         (add-product screen)
        │   ├── data/
        │   │   ├── WarehouseConstants.kt  (fixed Hungarian warehouse/unit lists)
        │   │   ├── InventoryItem.kt       (Room entity)
        │   │   ├── InventoryDao.kt        (Room DAO: insert + search)
        │   │   ├── AppDatabase.kt         (Room database singleton)
        │   │   └── InventoryRepository.kt
        │   ├── viewmodel/
        │   │   ├── MainViewModel.kt
        │   │   ├── AddItemViewModel.kt
        │   │   └── ViewModelFactory.kt
        │   └── adapter/
        │       └── SearchResultAdapter.kt (RecyclerView adapter)
        └── res/
            ├── layout/ (activity_main.xml, activity_add_item.xml, item_search_result.xml)
            └── values/ (strings.xml, colors.xml, themes.xml)
```

## How to import and run in Android Studio

1. **Unzip** this project anywhere on your computer.
2. Open **Android Studio** (Giraffe/Koala or newer recommended).
3. Choose **File → Open...** and select the unzipped `RaktarKereso` folder
   (the one containing `settings.gradle`). Do **not** open the `app` subfolder directly.
4. Android Studio will detect it's a Gradle project and generate the Gradle
   Wrapper automatically on first sync (it will prompt you — accept it, or it
   happens silently depending on your Studio version). Let Gradle sync finish;
   this downloads the AndroidX/Material/Room dependencies (internet needed
   only for this one-time setup, not for running the app).
5. Once sync completes, select the `app` run configuration and click **Run ▶**
   on an emulator or a physical device (Android 5.0 / API 21 or higher).
6. The app installs and launches with the "Raktár Kereső" search screen.

No API keys, backend, or network connection are required at runtime — all
data is stored locally on the device via Room (SQLite).

## How it works

- **Search**: type into the search box on the main screen. Matching is a
  live, case-insensitive partial match on the product name. Results show as
  "Raktár - Tároló egység" cards (e.g. "II. raktár - Középső jobb fent").
  If nothing matches, "Nincs találat" is shown.
- **Add product**: tap "Új termék hozzáadása", pick a warehouse and a storage
  unit from the two dropdowns, type the product name/ID, and tap "Mentés".
  Empty fields are rejected with "Kérlek, töltsd ki az összes mezőt!".
  A successful save shows "Termék sikeresen mentve" and returns to the
  search screen.
- Multiple products can be saved under the same warehouse/storage-unit
  combination — a storage unit is not limited to one item.

## Notes / possible extensions

- No app launcher icon is bundled; Android Studio's default icon will be
  used. You can generate a custom one via **Right-click `res` → New → Image Asset**.
- The Gradle/AGP/Kotlin versions used (AGP 8.2.2, Kotlin 1.9.22, Room 2.6.1)
  are stable as of early 2026; if Android Studio suggests an upgrade during
  sync, accepting it is safe.
- To view/edit the raw database file for debugging, use Android Studio's
  **App Inspection → Database Inspector** while the app is running.
