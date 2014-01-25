Grader-Merger
=============

# About

Tool for merging the results of multiple people grading assignments using the Grader application

**Table of Contents**

* <a href="#quick-start-with-eclipse">Quick Start with Eclipse</a>
* <a href="#project-setup">Project Setup</a>
* <a href="#configuration">Configuration</a>

# Quick Start with Eclipse

The following is a guide to getting up an running as quickly as possible. The only assumption this quick start guide
makes is that you have Eclipse installed.

## Step 1: Get Maven

Download Maven from http://maven.apache.org, extract it, and add the binaries folder to your system path. Make sure the
`JAVA_HOME` environment variable is set.

## Step 2: Setup Eclipse with Maven

From the **Help** menu select **Eclipse Marketplace**.

Install the plugin called *Maven Integration for Eclipse*. This will require Eclipse to restart.

After restarting, go to **Window->Preferences** in Eclipse, and then go to **Maven->Installations**.  Add the install of Maven from Step 1.

## Step 3: Get and Initialize the Repository

Clone the repository.

```
git clone https://github.com/camman3d/Grader.git
```

From a command line (if using Windows, use command prompt not powershell), navigate to the folder you just cloned and
run the following command.

```
mvn install:install-file -Dfile=grader-0.2.jar -DgroupId=edu.unc -DartifactId=grader -Dversion=0.2 -Dpackaging=jar
```

This adds the Grader jar file to your local Maven repository so that the dependency can be resolved.

## Step 4: Add the Project to Eclipse

In Eclipse, import a Maven project. Select the project you just cloned.

*Note:* The compliance level may be set to **1.5** so be sure to change this to **1.7**.

## Step 5: Run the Program

That's it, you're all set up. The default entry point is `mergingTools.Driver`. You can run this file to run the grading
tool.

# Project Setup

## Testing, Building, and Executing

Because this is a Maven project, you can run the tests and build it using Maven commands.

### Testing

To run all the tests in the `test` folder, just run the following command:

```
mvn test
```

### Building

To compile:

```
mvn compile
```

To build the .jar (which does the testing and compilation as well):

```
mvn package
```

### Executing

The name of the jar depends on the `version` defined in `pom.xml`. Run the jar:

```
java -jar target/grader-merger-X-jar-with-dependencies.jar
```

If you have the project set up in an IDE you can run it there as well.

# Configuration

The entry point in the program (the one which Maven is configured to use) looks at the configuration file to determine
what and how to run. There are the following settings that you can set:

* `project.name`: The name of the project. Something like "Assignment4".
* `grader.logger`: This setting allows you to set how results will be saved. You can choose which loggers are used by selecting any of the following concatenated with '+':
 * `feedback-txt`: This saves a text file in the students' feedback folder
 * `feedback-json`: This saves a json file in the students' feedback folder
 * `feedback`: Equivalent to "feedback-txt + feedback-json"
 * `local-txt`: This saves a text file in a local log folder
 * `local-json`: This saves a json file in the local log folder
 * `local`: Equivalent to "local-txt + local-json"
 * `spreadsheet`: This saves all the scores in a spreadsheet
