# Selenium Data-Driven Testing Framework

A Selenium 4 + TestNG UI automation framework in Java, with a Page Object Model and CSV-driven test data, targeting the public [Sauce Labs demo site](https://www.saucedemo.com). Built as an SDET/automation portfolio project.

**[Live Extent report](https://himanshupawar26.github.io/Selenium-AutomationTesting-DDF-Framework/)** — published automatically from the latest `master` build.
[![UI Test Automation CI](https://github.com/himanshupawar26/Selenium-AutomationTesting-DDF-Framework/actions/workflows/ci.yml/badge.svg)](https://github.com/himanshupawar26/Selenium-AutomationTesting-DDF-Framework/actions/workflows/ci.yml)

> This repo previously held a 2022 course-exercise version of this framework (Java 7, Selenium 3, hardcoded Windows driver paths and a hardcoded test-data path, a committed Excel file plus committed `target/`, `test-output/` and screenshot binaries). It has been fully rewritten; history is preserved in `git log`.

## What this demonstrates

- **Page Object Model**: `LoginPage` / `InventoryPage` / `CartPage` own their locators and waits; test classes contain assertions and flow, not raw `driver.findElement` calls.
- **Explicit waits only**: every page object method waits for the exact condition it needs (`WebDriverWait` + `ExpectedConditions`). No implicit wait and no `Thread.sleep` anywhere in the framework — the old `BaseUI` had neither a real wait strategy nor a parallel-safe one.
- **Data-driven testing without a binary test-data file**: login coverage is driven by `testdata/login-data.csv` — one `@DataProvider` row per case, each an independent pass/fail in the report. The old framework read the same kind of data from a committed `.xlsx` via Apache POI; the switch to CSV keeps the data-driven pattern but makes the test data itself readable and diffable in a PR.
- **Thread-safe parallel execution**: `DriverFactory` holds one `WebDriver` per thread (`ThreadLocal`), and `testng.xml` runs with `parallel="methods"`. Building this surfaced a real concurrency bug worth calling out: an early version of `InventoryTest` cached its `InventoryPage` in a shared instance field written by `@BeforeMethod` and read by `@Test` — under parallel execution, one thread's `@BeforeMethod` could overwrite that field before the other thread's `@Test` read it, so each thread ended up driving the wrong browser session. Fixed by making the page object a stack-local variable per test method instead of a shared field. (See `InventoryTest.java` for the comment and fix in place.)
- **No hardcoded environment config or driver binaries**: base URL / browser / headless resolve via system property → environment variable → classpath `config.properties`, in that order. Selenium Manager (built into Selenium since 4.6) resolves the matching browser driver at runtime — no committed `.exe` files, no path that only works on one machine.
- **ExtentReports**, with a base64 screenshot embedded directly in the report on failure — nothing written to disk, so there's no `screenshot/` directory to manage or accidentally commit.
- **CI on GitHub Actions**, running the suite headless on every push/PR and publishing the Extent report to GitHub Pages on `master`.

## About the target site

[Sauce Labs' demo site](https://www.saucedemo.com) is a free, publicly hosted site built specifically for Selenium/automation practice, with built-in test accounts covering different login outcomes (`standard_user`, `locked_out_user`, invalid credentials). That's what makes it a better fit here than a real production site: it's stable, has no ToS concerns around automated testing, and its intentionally varied login behavior is exactly what a data-driven login suite wants to exercise.

## Stack

| Layer | Tool |
|---|---|
| Language | Java 17 |
| Build | Maven |
| UI automation | Selenium 4 (Selenium Manager, no driver binaries) |
| Test runner | TestNG 7 |
| Test data | CSV (`commons-csv`) |
| Assertions | AssertJ |
| Reporting | ExtentReports |
| CI | GitHub Actions |

## Project structure

```
src/test/java/com/himanshupawar/ui/
├── config/      # Config.java — env resolution, no hardcoded paths
├── driver/      # DriverFactory — ThreadLocal WebDriver, Selenium Manager
├── pages/       # BasePage, LoginPage, InventoryPage, CartPage
├── utils/       # CsvDataReader, ScreenshotUtil
├── listeners/   # ExtentManager, TestListener (ITestListener)
└── tests/       # BaseTest, LoginTest, InventoryTest

src/test/resources/
├── config.properties
└── testdata/
    └── login-data.csv

testng.xml   # suite definition, parallel="methods"
```

## Running locally

```bash
mvn test                                    # headless Chrome (default)
mvn test -Dheadless=false                   # watch it run
mvn test -Dbrowser=firefox                  # Firefox instead of Chrome
```

The Extent report is written to `target/extent-report/index.html` after every run.

## Test coverage

- **Login** (`LoginTest`, data-driven from `login-data.csv`): valid login, locked-out user, wrong password, unknown user, empty username, empty password.
- **Inventory** (`InventoryTest`): sorting products by price orders them correctly; adding products updates the cart badge count and the cart page's contents.
