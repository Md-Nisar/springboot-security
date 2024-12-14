pipeline {
    agent any

     tools {
            maven 'Maven_3.6.3' // Use the Maven tool configured in Jenkins (adjust the name accordingly)
            jdk 'JDK_17'        // Use the JDK configured in Jenkins
        }

    stages {
        stage('Checkout Code') {
            steps {
                echo "Checking out code from GitHub..."
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Building the application using Maven..."
                sh "mvn clean install -DskipTests=true" // Adjust to your project's build tool (Maven/Gradle)
            }
        }

        stage('Package Application') {
            steps {
                echo "Packaging the application as a JAR file..."
                sh "mvn package"
            }
        }

    }

    post {
        success {
            echo "Pipeline completed successfully!"
        }
        failure {
            echo "Pipeline failed. Check logs for more details."
        }
    }
}
