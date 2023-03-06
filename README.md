## Objective

Create a standalone java application which allows users to manage their favourite recipes. It should
allow adding, updating, removing and fetching recipes. Additionally users should be able to filter
available recipes based on one or more of the following criteria:
1. Whether or not the dish is vegetarian
2. The number of servings
3. Specific ingredients (either include or exclude)
4. Text search within the instructions.


For example, the API should be able to handle the following search requests:
1. All vegetarian recipes
2. Recipes that can serve 4 persons and have “potatoes” as an ingredient
3. Recipes without “salmon” as an ingredient that has “oven” in the instructions.

## Requirements
Please ensure that we have some documentation about the architectural choices and also how to
run the application. The project is expected to be delivered as a GitHub (or any other public git
hosting) repository URL.

All these requirements needs to be satisfied:

1. It must be a REST application implemented using Java (use a framework of your choice)
2. Your code should be production-ready.
3. REST API must be documented
4. Data must be persisted in a database
5. Unit tests must be present
6. Integration tests must be present

-----------------------------------------

## Setup guide

#### Minimum Requirements

- Java 11
- Gradle 7.x

#### Install the application

1. Make sure you have [Java](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html) and [Gradle](https://gradle.org/install/) installed

2. Open the command line in the source code folder

3. Build project

  ```
  ./gradlew clean build
  ```
Or

  ```
  gradle clean build
  ```

Run the tests
  ```
  gradle api:test
  ```
  ```
  gradle data:test
  ```
  ```
  gradle domain:test
  ```
  ```
 gradle test
  ```

Run the project
 ```
 gradlew clean bootRun
 ```
Or

```
java -jar build/libs/recipe.jar
```

4. Open the swagger-ui with the link below

```
http://localhost:3000/swagger-ui.html#/
```

-----------------------------------------
## The solution
I have built the project with a clean architected, and also I use the Gradle multi-module on the code structure.

One of the primary benefits of using CleanArchitecture is improved maintainability. By separating the concerns of the various components and enforcing the dependency rule, it becomes much easier to understand and modify the code.

Also, I used the H2 as a Database because it can be run in memory, and it is effective in small projects, with simple relations.

Besides, I added the swagger, so I can test the APIs.

On the other hand, I added test cases as much as I could, separately, for each layer, API layer, Data layer, Domain layer, and integrations.