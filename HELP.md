
---

# **Help Documentation: Building and Deploying Spring Boot and Angular Projects**

## **Table of Contents**

1. [Introduction](#introduction)
2. [Pre-requisites](#pre-requisites)
3. [Building the Spring Boot Project](#building-the-spring-boot-project)
4. [Building the Angular Project](#building-the-angular-project)
5. [Running the Spring Boot Application Locally](#running-the-spring-boot-application-locally)
6. [Running the Angular Application Locally](#running-the-angular-application-locally)
7. [Deployment](#deployment)
    - [Spring Boot Deployment](#spring-boot-deployment)
    - [Angular Deployment](#angular-deployment)
8. [Troubleshooting](#troubleshooting)
9. [Conclusion](#conclusion)

---

## **Introduction**

This document provides step-by-step instructions to build, run, and deploy both the **Spring Boot** backend and **Angular** frontend of the project.

The Spring Boot application provides the backend logic, while the Angular application provides the frontend user interface. Both need to be built and deployed separately.

---

## **Pre-requisites**

Before you begin, ensure you have the following tools installed on your system:

1. **Java Development Kit (JDK 17 or above)** – Required for the Spring Boot application.
    - Download it from [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) or [OpenJDK](https://openjdk.java.net/).
2. **Maven** – For building the Spring Boot project.
    - Install Maven from [Maven Official Website](https://maven.apache.org/install.html).
3. **Node.js and npm** – Required for the Angular project.
    - Install Node.js from [Node.js Official Website](https://nodejs.org/).
4. **Angular CLI** – Command-line interface for Angular.
    - Install Angular CLI by running:
      ```bash
      npm install -g @angular/cli
      ```

---

## **Building the Spring Boot Project**

### **1. Clone the Repository**

If you haven't already cloned the project, clone it from GitHub using the following command:

```bash
git clone <repository-url>
cd <repository-name>
```

### **2. Install Dependencies**

Spring Boot uses Maven for managing dependencies. Ensure you have Maven installed on your system. Run the following command to install the required dependencies:

```bash
mvn clean install
```

This will download all necessary dependencies specified in the `pom.xml` file.

### **3. Build the Spring Boot Project**

To build the Spring Boot project, use the following Maven command:

```bash
mvn clean package
```

This will create a `.jar` file (in the `target/` directory), which contains the compiled Spring Boot application.

---

## **Building the Angular Project**

### **1. Navigate to the Angular Project Directory**

The Angular project should be located within the `angular-security/` directory. Navigate to that directory:

```bash
cd angular-security
```

### **2. Install Dependencies**

Angular uses npm to manage dependencies. Install the necessary packages by running:

```bash
npm install
```

This command installs all the dependencies listed in the `package.json` file.

### **3. Build the Angular Project**

To build the Angular application for production, use the following command:

```bash
ng build --prod
```

This will create an optimized build of the Angular app in the `dist/` directory.

---

## **Running the Spring Boot Application Locally**

### **1. Run the Application**

To run the Spring Boot application locally, use the following command:

```bash
mvn spring-boot:run
```

Alternatively, if you want to run the `.jar` file after building it, execute:

```bash
java -jar target/your-project-name.jar
```

This will start the Spring Boot application on the default port `8080`.

### **2. Verify the Application**

Open your browser and visit `http://localhost:8080` to verify that the Spring Boot application is running successfully.

---

## **Running the Angular Application Locally**

### **1. Serve the Application**

To serve the Angular application locally, use the following command:

```bash
ng serve
```

This will start the Angular development server and serve the app on `http://localhost:4200`.

### **2. Verify the Application**

Open your browser and visit `http://localhost:4200` to verify that the Angular frontend is running successfully.

---

## **Deployment**

### **Spring Boot Deployment**

#### **1. Build the Spring Boot Application**

If you haven't already built the `.jar` file, follow the steps mentioned above to run:

```bash
mvn clean package
```

This will generate the `.jar` file in the `target/` directory.

#### **2. Deploy on a Server**

You can deploy the Spring Boot application on various platforms such as AWS, Heroku, or any other server of your choice. For example:

- **Deploying on AWS EC2**:
    - Copy the `.jar` file to your EC2 instance using `scp` or any file transfer method.
    - SSH into your EC2 instance and run:
      ```bash
      java -jar your-project-name.jar
      ```

- **Deploying on Heroku**:
    - Install the [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli).
    - Create a new Heroku app:
      ```bash
      heroku create your-app-name
      ```
    - Push the Spring Boot application to Heroku:
      ```bash
      git push heroku main
      ```
    - The app will be available at `https://your-app-name.herokuapp.com`.

### **Angular Deployment**

#### **1. Build the Angular Application**

To create a production build of the Angular app, use:

```bash
ng build --prod
```

This will generate optimized static files in the `dist/` folder.

#### **2. Deploy on a Web Server**

You can deploy the `dist/` folder to any web server such as Nginx, Apache, or use cloud platforms like AWS S3, Netlify, or Firebase Hosting. For example:

- **Deploy on AWS S3**:
    - Create a new S3 bucket via the AWS console.
    - Upload the contents of the `dist/` folder to the bucket.
    - Enable static website hosting for the bucket.

- **Deploy on Netlify**:
    - Go to [Netlify](https://www.netlify.com) and log in.
    - Click "New Site from Git" and connect your repository.
    - Set the build command to `ng build --prod` and publish directory to `dist/`.

---

## **Troubleshooting**

- **Spring Boot Application Not Starting**:
    - Ensure that you have the correct JDK version installed.
    - Check for any issues in the `application.properties` or `application.yml` configuration files.
    - Review the logs for any specific error messages.

- **Angular Application Not Loading**:
    - Ensure all dependencies are installed by running `npm install`.
    - If there are errors related to missing packages, try deleting the `node_modules/` folder and reinstalling:
      ```bash
      rm -rf node_modules/
      npm install
      ```

---

## **Conclusion**

You have successfully built and deployed both the **Spring Boot** and **Angular** applications. Make sure to follow the deployment guidelines for your chosen hosting platform, and don't forget to check the respective documentation for more detailed configuration options.

Let me know if you need any further assistance! 🚀

--- 
