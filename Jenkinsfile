pipeline {
    agent any

    tools {
        maven 'Maven_3.6.3' // Use the Maven tool configured in Jenkins (adjust the name accordingly)
        jdk 'JDK_17'        // Use the JDK configured in Jenkins
    }

    environment {
        DOCKER_IMAGE = "nisar10/app"
        DOCKER_TAG = "latest"
        DOCKER_REGISTRY = "https://docker.io"
        PORT = 8089
    }

    stages {
        stage('Checkout Code') {
            steps {
                echo "Checking out code from GitHub..."
                checkout scm
            }
        }

        stage('Build Application') {
            steps {
                echo "Building the application using Maven..."
                bat "mvn clean install -DskipTests=true"
            }
        }

        stage('Package Application') {
            steps {
                echo "Packaging the application as a JAR file..."
                bat "mvn package -DskipTests=true"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image..."
                    bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }

        stage('Login to Docker Hub') {
            options {
                timeout(time: 30, unit: "MINUTES")
            }
            steps {
                script {
                    echo "Logging in to Docker Hub..."
                    bat "docker login -u ${credentials('dockerhub_credentials').username} -p ${credentials('dockerhub_credentials').password}"
                }
            }
        }

        stage('Push Docker Image') {
            options {
                timeout(time: 30, unit: "MINUTES")
            }
            steps {
                script {
                    echo "Pushing Docker image to Docker Hub..."
                    bat "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    echo "Deploying application using Docker Compose..."

                    def composeFile = 'docker-compose.yml'

                    // Pull the latest Docker images
                    bat "docker-compose -f ${composeFile} pull"

                    // Start services with Docker Compose
                    bat "docker-compose -f ${composeFile} up -d"
                }
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
