
# Refactoring Demo — 2-Session Plan (Testing First, Then Refactor)

This workshop is split into **two 60‑minute sessions**.  
**Session 1** focuses on **unit tests and characterization**.  
**Session 2** focuses on **refactoring safely** using those tests as a safety net.

---

## What is Refactoring?

**Refactoring** is the process of improving the **internal design** of code—naming, structure, clarity, duplication, separation of concerns—**without changing its externally observable behavior**. The goal is to make the code **easier to understand, maintain, and extend**, while keeping functionality intact. In practice, we:
- Keep the **inputs/outputs** the same.
- Change **how** the result is produced (structure), not **what** is produced (behavior).
- Move in **small, reversible steps** with tests running all the time.

---

## Why Unit Tests (and “Characterization” Tests)?

Before touching legacy code, we lock down current behavior with **characterization tests**:
- They **capture the current behavior** (including quirks and edge cases).
- They allow us to **refactor with confidence**—if a test breaks, we immediately know a change altered behavior.
- Once the tests are green, we can do small refactorings repeatedly.

**Key idea:** *No green tests → No refactor.*

---

## Tools Used

- **Java 8+**
- **Gradle** (build, test, coverage)
- **JUnit 5** (unit testing)
- **JaCoCo** (coverage reports)
- **VS Code** with the **Extension Pack for Java** (Test Explorer, debugger, Gradle panel)

> Alternative IDEs (IntelliJ/Eclipse) will work too, but the steps here assume VS Code.

---

## Getting Started

### Prerequisites
- JDK 8+ installed and on your PATH.
- Gradle Wrapper in the project (`./gradlew`).
- VS Code + Extension Pack for Java.

### Install & Verify
```bash
./gradlew --version
java -version
```

### Running Tests & Coverage
#### Command Line
```bash
# Run tests
./gradlew clean test

# Generate coverage report (HTML in build/jacocoHtml/index.html)
./gradlew jacocoTestReport
``
```

### Business Rules for `LegacyPriceCalculator.calculate(...)`

1. **Price and Quantity Validation**
   - If the base price is **less than or equal to zero**, the process stops and an error is raised: *"Invalid base price"*.
   - If the quantity is **less than or equal to zero**, the calculation returns **0** (no charge).

2. **Base Price Calculation**
   - The starting point is `basePrice × quantity`.

3. **Quantity-Based Discounts**
   - Orders of **10 or more units** receive a **5% discount**.
   - Orders of **51 to 100 units** receive an **additional 10% discount**.
   - Orders of **more than 100 units** are **not allowed** and trigger an error: *"Too many products!"*.

4. **Tax Application (only for orders with 10 or more units)**
   - If a country code is provided:
     - **Mexico (MX)** → apply **16% tax**.
     - **United States (US)** → apply **7% tax**.
     - Any other country → apply **20% tax**.
   - If no country code is provided → apply **30% tax**.

5. **Membership Discounts and Shipping (only for orders with fewer than 10 units)**
   - **Gold members**:
     - Receive a **\$20 discount**.
     - If the order is placed on **Friday**, apply an additional **5% discount**.
   - **Silver members**:
     - Receive a **\$10 discount**.
   - **Unknown membership**:
     - Shipping cost applies:
       - **Expedited shipping** → \$30.
       - **Standard shipping** → \$10.
   - **No membership**:
     - Apply a flat shipping fee of **\$50**.

6. **Final Adjustment**
   - If the calculated total is **negative**, it is adjusted to **0** before returning.

7. **Result**
   - The method returns the **final total price** after applying all discounts, taxes, and shipping rules.

## Session 1 — Freezing Behavior with Tests (Characterization)

### Objective
Show how to **lock down the current behavior** of a legacy method using **unit tests**, so we can refactor it safely in the next session.

---

### Agenda
1. **Context & legacy code**: what the method does and why it’s hard to maintain.
2. **Run an empty test file**: what a test run looks like with no cases.
3. **Baseline coverage**: how to read the report and what the metrics mean.
4. **Exploration strategy**: start with the **least nested branch**.
5. **Incremental test design**: scenarios, **relevant vs. irrelevant parameters**.
6. **Testability for the GOLD+Friday case**: introduce a **code seam**.
7. **Close at 100% coverage**: and demonstrate **mutation** or small changes to prove test strength.

---

### 1) The legacy method and its pain points
- **Deep nesting** that obscures the flow.
- **Magic numbers** for rates, discounts, and fees.
- **Mixed responsibilities**: validation, discounts, taxes, and shipping all together.
- **Implicit time dependency** (`LocalDate.now()`), making some paths **non‑deterministic**.

> Key message: today we **don’t change the rules**; we only **freeze** the observable behavior.

---

### 2) Starting point: empty tests
- Run a test class with **no cases** to illustrate:
  - The execution pipeline.
  - The **green** state that means nothing yet (no assertions).
- Establish the **before** state to compare progress.

---

### 3) Baseline coverage
- Open the coverage report to see:
  - **Lines and branches** (almost none executed at first).
  - Where complexity concentrates.
- Clarify: **coverage isn’t the goal**, it’s a **signal** to decide where to invest in tests.

---

### 4) Choose the first branch: the least nested path
- Start with the most direct path for **quick wins**:
  - Example: early returns for invalid inputs (quantity ≤ 0, invalid base price).
- Benefit: boosts coverage and confidence before entering the complex areas.

---

### 5) Build scenarios incrementally
Design cases that exercise **every decision** in the method, stating explicitly which parameters are **relevant** and which are **irrelevant** to each scenario.

**Example scenario blocks:**
- **Validations**: `basePrice <= 0` (error), `quantity <= 0` (total 0).
- **Quantity discounts**:
  - Thresholds: 9 → none; 10 → 5%; 51–100 → 5% + 10%; >100 → error.
- **Taxes (only when applicable)**:
  - `MX` 16%, `US` 7%, others 20%, `null` 30%.
- **Membership (when quantity < 10)**:
  - GOLD: -20 (and **Friday** rule).
  - SILVER: -10.
  - Unknown: shipping fee (expedited vs standard).
  - No membership: flat shipping.
- **Final adjustment**: clamp to 0 if the total becomes negative.

> In each test, make **irrelevant parameters explicit** (e.g., country is irrelevant in membership scenarios with `quantity < 10`) to improve readability and reduce noise.

---

### 6) GOLD on Friday: testability first
- Problem: the rule depends on **`LocalDate.now()`**, making the test **non‑deterministic**.
- Solutions presented:
  - Introduce a **code seam** (e.g., inject a `Clock` or date provider) to control time in tests.
  - Meanwhile, use **assumptions** or mark the case as conditional, and document the limitation.
- Key message: **sometimes you must adjust design for testability**, without changing business rules.

---

### 7) Close with 100% coverage and strong tests
- Add the remaining cases to reach **~100% lines/branches**.
- Show the updated coverage report.
- **Strength demo**:
  - Make small **intentional tweaks** (manually) or use **mutation testing (PIT)** to prove tests **catch changes**.
  - Conclusion: if a mutation “survives”, add the missing test.

---

### Conclusion
- We have **frozen the behavior** of the legacy method with a clear, repeatable test suite.
- High coverage at critical thresholds provides a **safety harness**.
- In **Session 2**, we will refactor in **micro‑steps**, keeping tests **green at every step**.

## Commands You’ll Use Frequently

```bash
# Run tests verbosely and show stacktraces
./gradlew clean test -i --stacktrace

# Run one test class / method
./gradlew test --tests "demo.legacy.LegacyPriceCalculatorTest"
./gradlew test --tests "demo.legacy.LegacyPriceCalculatorTest.someSpecificCase"

# Coverage
./gradlew jacocoTestReport
open build/jacocoHtml/index.html  # (macOS) or use your file explorer
```

## Tips & Good Practices

- **One change at a time**  
  Make small, incremental refactors and run tests after every step.

- **Run tests frequently**  
  Keep the safety net active. If tests fail, stop and fix before continuing.

- **Name by intent, not by mechanics**  
  Use descriptive names that explain *why* something exists, not just *what* it does.

- **Prefer pure functions**  
  Isolate logic without side effects for easier testing and reuse.

- **Centralize constants**  
  Replace magic numbers with named constants (e.g., `TAX_MX = 0.16`).

- **Remove duplication**  
  Consolidate repeated logic (e.g., tax calculation) into a single method.

- **Document legacy quirks**  
  If a rule is odd but intentional, capture it in tests and comments.

- **Consistent rounding**  
  Apply rounding rules in one place to avoid discrepancies.

- **Commit small and reversible changes**  
  Use clear commit messages like `refactor: extract tax calculation`.

- **Don’t mix refactor with new features**  
  Keep refactoring commits separate from behavior changes.

## What’s In/Out of Scope

**In scope**
- Refactorings that improve internal structure **without changing externally observable behavior**.
- Adding or refining **characterization tests** to lock current behavior before refactoring.
- Introducing **guard clauses**, extracting methods, replacing magic numbers with constants, improving naming, and removing duplication.
- Incremental improvements to test coverage (e.g., branch/line coverage) to increase refactoring safety.

**Out of scope**
- Redesigning or redefining **business rules** (e.g., changing discounts/taxes/shipping logic) unless explicitly agreed and covered by updated tests.
- Large architectural changes (modules, multi-service extraction) that alter public APIs.
- Performance tuning or non-functional optimizations beyond minor wins achieved by refactoring.
- Toolchain changes beyond the provided Gradle + JUnit + JaCoCo setup.

---

## FAQ (Short)

**Q: Why not rewrite from scratch?**  
A: Rewrites risk regressions and delays. Refactoring with tests preserves behavior while improving maintainability and enabling safer change.

**Q: Why so many small commits?**  
A: Small, focused commits are easier to review, revert, and learn from; they also keep you in a green feedback loop.

**Q: What if I must change behavior?**  
A: First, update or add tests to express the **new intended behavior**; then implement the change. Use explicit messages (e.g., `feat(rule): adjust threshold from 10 to 12 (tests updated)`).

**Q: Is 100% coverage required?**  
A: Not always. Aim for **meaningful coverage** of risk areas (complex branches, edge cases). Use coverage to guide where to add tests, not as an end in itself.

**Q: How do I deal with floating‑point rounding differences?**  
A: Centralize rounding rules (e.g., `HALF_UP` to 2 decimals) and apply them consistently in one place to avoid drift.

---

## Appendix: Where to Look

- **Core logic:** `src/main/java/demo/legacy/LegacyPriceCalculator.java`  
- **Unit tests:** `src/test/java/demo/legacy/LegacyPriceCalculatorTest.java`  
- **Coverage report (HTML):** `build/jacocoHtml/index.html`  
- **Build & test config:** `build.gradle`  

---

## References

- Beck, K. (2003). *Test-Driven Development: By Example*. Addison-Wesley.
- Feathers, M. C. (2004). *Working Effectively with Legacy Code*. Prentice Hall.
- Fowler, M. (2018). *Refactoring: Improving the Design of Existing Code* (2nd ed.). Addison-Wesley.
- Humble, J., & Farley, D. (2010). *Continuous Delivery: Reliable Software Releases through Build, Test, and Deployment Automation*. Addison-Wesley.
- Martin, R. C. (2008). *Clean Code: A Handbook of Agile Software Craftsmanship*. Prentice Hall.
- Meszaros, G. (2007). *xUnit Test Patterns: Refactoring Test Code*. Addison-Wesley.