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
                    // Build Docker image with specified Image name and tag, from Dockerfile
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
                    // Login to Docker Hub (set up Docker credentials in Jenkins)
                    //docker.withRegistry('https://docker.io', 'dockerhub-credentials') {
                        // Login happens here, but no further commands inside the block
                        bat "docker login -u 'nisar10' -p 'Nisar@039'"
                    }
                }
            }
        }

        stage('Push Docker Image') {
         options {
                timeout(time: 30, unit: "MINUTES")
              }
            steps {
                script {
                    // Push the image to Docker Hub

                  //  docker.withRegistry('https://docker.io', 'dockerhub-credentials') {
                        bat "docker push ${DOCKER_IMAGE}:${DOCKER_TAG}"
                    }
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    // Pull the Docker image from the registry
                    bat "docker pull ${DOCKER_IMAGE}:${DOCKER_TAG}"

                    // Ensure Docker Compose file exists
                    def composeFile = 'docker-compose.yml'

                    // Always pull the latest Docker images
                    bat "docker-compose -f ${composeFile} pull"

                    // Start the services with Docker Compose
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
