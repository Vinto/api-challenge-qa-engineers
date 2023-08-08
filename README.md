# Software Quality Engineer Api Challenge

# Local Development
To be able to add new tests and run the tests on local machine, the following tools have to be installed:
* JDK-8
* Java IDE (Intelli J)

#### Clone the repository
```
git clone git@github.com:Vinto/mobiquity-api-challenge.git
```

#### Test Data
The test data is stored in data properties files: `src/main/resources/data.properties`

# Running Test(s)

## On Intelli J
To run the tests through Intelli J do the following:
* Open `PostTest` or `UserTest` in the test directory.
* Click on the green arrow next to the @Test method and Run from the popup menu. The same should happen if you want to run the test cases from the entire test suite by running `api_testng.xml` file.

# Running Test Suite
Execute API test suite

```
mvn clean test -Papi
```

# CI/CD
* Circle CI yaml file can be found here: `.circleci/config.yml`

# Surefire Reports 
* Can be found here: `target/surefire-reports/index.html`

# Postman files 
* Can be found in the `postman` directory inside the root directory.
