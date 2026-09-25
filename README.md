# UwaSocial - A Social Media app

An Android social feed application built following [**Google's Recommended Architecture**](https://developer.android.com/topic/architecture), modern Jetpack Compose UI, offline caching, and Paging 3 integration.

<p align="center">
  <img src="app/art/screenshot1.png" width="45%" alt="UwaSocial Feed Screenshot 1">
  &nbsp;
  <img src="app/art/screenshot2.png" width="45%" alt="UwaSocial Feed Screenshot 2">
</p>

---

## 🏛️ Architecture & Module Structure

The project follows Google's recommended architecture principles with strict data encapsulation and multi-module separation:

```
                               ┌─────────────────────────┐
                               │          :app           │
                               └────────────┬────────────┘
                                            │
                               ┌────────────▼────────────┐
                               │     :features:posts     │ (UI / Presentation Layer)
                               └────────────┬────────────┘
                                            │
                               ┌────────────▼────────────┐
                               │         :domain         │ (Use Cases & Domain Models)
                               └──────┬─────────────┬────┘
                                      │             │
                        ┌─────────────▼──┐       ┌──▼──────────────┐
                        │   :data:user   │       │   :data:posts   │ (Data Layer)
                        └─────────────┬──┘       └──┬──────────────┘
                                      │             │
                               ┌──────▼─────────────▼────┐
                               │          :core          │ (General Helpers & Utils)
                               └─────────────────────────┘
```

### Modules Description
* **`:app`**: Application entry point, dependency injection initialization, main activity container, and baseline profile rules.
* **`:features:posts`**: Presentation and UI layer for rendering feed posts, loading indicators, and handling screen user interactions.
* **`:domain`**: Business logic layer containing use cases and domain models for posts and users.
* **`:data:posts`**: Data layer handling post network requests, database storage, and offline caching.
* **`:data:user`**: Data layer handling user profile fetching, database storage, and caching.
* **`:core`**: Shared utilities, network connectivity tracking, coroutine dispatchers, and common UI helpers.
* **`:benchmark`**: Performance macrobenchmarks for measuring startup time, frame timing, and generating baseline profiles.

---

## ⚡ Baseline Profile & Performance Optimization

A **Baseline Profile** is a set of Ahead-Of-Time (AOT) compilation rules for the Android Runtime (ART) that pre-compiles critical code paths during app installation, eliminating initial JIT compilation overhead and runtime execution delays.

I added Baseline Profiles to this project to eliminate first-scroll jank and guarantee a butter-smooth **60fps / 120fps scrolling** experience across the feed:

- **Baseline Profile Rules ([`baseline-prof.txt`](app/src/main/baseline-prof.txt))**: Pre-compiles critical execution paths including [`PostsFeedScreen`](features/posts/src/main/java/com/bellogate_caliphate/uwasocial/features/posts/ui/screen/PostsFeedScreen.kt), [`PostCard`](features/posts/src/main/java/com/bellogate_caliphate/uwasocial/features/posts/ui/components/PostCard.kt), Coil image decoding, Room database transactions, and Compose `LazyColumn` item measurement.
- **Profile Generator ([`BaselineProfileGenerator.kt`](benchmark/src/main/java/com/bellogate_caliphate/uwasocial/benchmark/BaselineProfileGenerator.kt))**: Automates profile collection using Jetpack `BaselineProfileRule` in the `:benchmark` module.

---

## 🧪 Unit Testing & UI Testing Strategy

I implemented unit and Compose UI tests to verify business logic, state transitions, and component rendering in isolation:

- **Testing Libraries Used**: `MockK` for mock creation, `kotlinx-coroutines-test` for coroutines, and `Turbine` for reactive `Flow` testing.
- **Unit Tests**:
  - [`UserRepositoryImplTest`](data/user/src/test/java/com/bellogate_caliphate/uwasocial/data/user/repository/UserRepositoryImplTest.kt): Verifies cached user retrieval and deduplicated remote fetching.
  - [`PostRepositoryImplTest`](data/posts/src/test/java/com/bellogate_caliphate/uwasocial/data/posts/repository/PostRepositoryImplTest.kt): Verifies domain mapping (`mapToDomainModel`) and like toggle functionality.
  - [`PostsViewModelTest`](features/posts/src/test/java/com/bellogate_caliphate/uwasocial/features/posts/ui/viewmodel/PostsViewModelTest.kt): Verifies `PostsViewModel` state management and like action dispatch.
  - [`GetFeedPostsUseCaseTest`](domain/src/test/java/com/bellogate_caliphate/uwasocial/domain/usecase/GetFeedPostsUseCaseTest.kt): Verifies UseCase delegation to repository flow.
  - [`ToggleLikePostUseCaseTest`](domain/src/test/java/com/bellogate_caliphate/uwasocial/domain/usecase/ToggleLikePostUseCaseTest.kt): Verifies UseCase delegation for post likes.
- **Compose UI Tests**:
  - [`PostCardTest`](features/posts/src/androidTest/java/com/bellogate_caliphate/uwasocial/features/posts/ui/components/PostCardTest.kt): Validates UI node rendering of user name, post body, and like count.
  - [`PostsFeedScreenTest`](features/posts/src/androidTest/java/com/bellogate_caliphate/uwasocial/features/posts/ui/components/PostsFeedScreenTest.kt): Validates full feed screen rendering and scroll behavior.

---

## 🤖 UIAutomation End-to-End Testing

**UIAutomation** is an Android testing framework that interacts directly with device UI nodes at the system level, simulating real user actions across application processes.

I implemented UIAutomation end-to-end tests to validate real device interactions and complete app navigation flows from launch through feed interaction:

- [`FeedUiAutomationTest`](app/src/androidTest/java/com/bellogate_caliphate/uwasocial/FeedUiAutomationTest.kt): Verifies launching the application, fetching remote feed posts, scrolling through items, and interacting with post components on-device.

---

## 📊 Macrobenchmark & Microbenchmark Testing

- **Macrobenchmark** measures high-level, end-to-end performance metrics on real devices, such as app startup time (COLD/WARM/HOT) and UI frame rendering/jank during scrolling.
- **Microbenchmark** isolates and measures CPU-bound algorithms and utility functions in nanosecond precision to prevent performance regressions.

I set up performance benchmarks in the `:benchmark` module to quantitatively measure and track performance metrics:

- [`StartupBenchmark`](benchmark/src/main/java/com/bellogate_caliphate/uwasocial/benchmark/StartupBenchmark.kt): Measures COLD, WARM, and HOT application startup timing.
- [`FrameTimingBenchmark`](benchmark/src/main/java/com/bellogate_caliphate/uwasocial/benchmark/FrameTimingBenchmark.kt): Measures frame rendering duration and jank count during fast list scrolling.
- [`TimeUtilsMicrobenchmark`](benchmark/src/main/java/com/bellogate_caliphate/uwasocial/benchmark/TimeUtilsMicrobenchmark.kt): Measures execution time and CPU efficiency of relative timestamp calculations.
