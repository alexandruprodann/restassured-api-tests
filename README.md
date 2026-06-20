# Rest Assured API Tests

Automated API checks for ReqRes using Java, Rest Assured, TestNG, Maven, and Hamcrest.

The suite covers user lookup/list/create/update/delete flows and login authentication scenarios against the ReqRes API.

## Tech Stack

- Java 21
- Maven
- Rest Assured
- TestNG
- Hamcrest
- GitHub Actions

## Project Structure

```text
src/test/java/com/apiautomation/base      Shared Rest Assured setup
src/test/java/com/apiautomation/tests     API test classes
src/test/java/com/apiautomation/utils     Configuration helpers
src/test/resources                        Local config template
```

## Configuration

ReqRes requests require an API key. Keep real values out of git.

For local runs, copy the template:

```bash
cp src/test/resources/config.properties.template src/test/resources/config.properties
```

PowerShell:

```powershell
Copy-Item src/test/resources/config.properties.template src/test/resources/config.properties
```

Then update `src/test/resources/config.properties`:

```properties
base.uri=https://reqres.in
base.path=/api
reqres.env=prod
user.agent=restassured-api-tests/1.0
request.delay.ms=500
api.key=YOUR_REQRES_API_KEY
login.email=YOUR_LOGIN_EMAIL
login.password=YOUR_LOGIN_PASSWORD
```

Configuration can also be provided as Java system properties or environment variables:

| Property | Environment variable |
| --- | --- |
| `api.key` | `API_KEY` |
| `login.email` | `LOGIN_EMAIL` |
| `login.password` | `LOGIN_PASSWORD` |
| `base.uri` | `BASE_URI` |
| `base.path` | `BASE_PATH` |
| `reqres.env` | `REQRES_ENV` |
| `user.agent` | `USER_AGENT` |
| `request.delay.ms` | `REQUEST_DELAY_MS` |

## Run Tests Locally

```bash
./mvnw test
```

PowerShell:

```powershell
.\mvnw.cmd test
```

Or without a local config file:

```bash
./mvnw test -Dapi.key=YOUR_REQRES_API_KEY -Dlogin.email=YOUR_LOGIN_EMAIL -Dlogin.password=YOUR_LOGIN_PASSWORD
```

## GitHub Actions

The CI workflow runs on pushes, pull requests, and manual dispatch.

Add these repository secrets before enabling CI:

| Secret | Used as |
| --- | --- |
| `API_KEY` | `API_KEY` |
| `LOGIN_EMAIL` | `LOGIN_EMAIL` |
| `LOGIN_PASSWORD` | `LOGIN_PASSWORD` |

Workflow file: `.github/workflows/api-tests.yml`

## Notes

- `src/test/resources/config.properties` is ignored so local credentials are not committed.
- Tests intentionally exercise a third-party hosted API, so CI failures can also indicate network, rate-limit, or service-side issues.
- ReqRes documents `x-api-key` as required for requests: https://reqres.in/docs
