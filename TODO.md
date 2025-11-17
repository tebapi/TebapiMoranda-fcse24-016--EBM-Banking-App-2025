# TODO: Convert Java Project to Maven

## Step 1: Create pom.xml ✅
- Created a new pom.xml file in the root directory with Maven configuration, including JavaFX and SQLite JDBC dependencies.

## Step 2: Move Source Code ✅
- Moved the entire src/ directory to src/main/java/ to follow Maven standard directory structure.

## Step 3: Remove Manual Dependency Management ✅
- Deleted the lib/ directory as Maven will handle dependencies.
- Deleted the out/ directory as Maven will manage compilation output.
- Removed IntelliJ IDEA project file (.iml).

## Step 4: Update Project Structure ✅
- Ensured the project structure aligns with Maven conventions.
- Kept .gitignore as is since it already excludes appropriate files.

## Step 5: Test the Conversion
- Maven is not installed on the system. Need to install Maven first.
- After Maven installation, run `mvn clean compile` to verify compilation.
- Run the application using Maven exec plugin to ensure it works.
