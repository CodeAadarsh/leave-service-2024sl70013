pipeline {
    agent any

    environment {
        IMAGE_NAME = "leave-service-2024sl70013"
        IMAGE_TAG  = "1.0"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/CodeAadarsh/leave-service-2024sl70013.git'
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Code Inspection') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                        sh '''
                            mvn sonar:sonar \
                              -Dsonar.projectKey=leave-service-2024sl70013 \
                              -Dsonar.login=$SONAR_TOKEN
                        '''
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('Deploy to Staging') {
            steps {
                sh """
                    docker stop leave-staging 2>/dev/null || true
                    docker rm   leave-staging 2>/dev/null || true
                    docker run -d \
                        --name leave-staging \
                        -e APP_ENV=staging \
                        -p 8085:8080 \
                        ${IMAGE_NAME}:${IMAGE_TAG}
                """
                sh 'sleep 3 && curl -f http://localhost:8085/health'
            }
        }

        stage('Approval') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    input message: 'Staging verified. Promote to Production?',
                          ok: 'Deploy to Production'
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                sh """
                    docker stop leave-prod 2>/dev/null || true
                    docker rm   leave-prod 2>/dev/null || true
                    docker run -d \
                        --name leave-prod \
                        -e APP_ENV=production \
                        -p 8086:8080 \
                        ${IMAGE_NAME}:${IMAGE_TAG}
                """
                sh 'sleep 3 && curl -f http://localhost:8086/health'
                sh 'docker ps --filter name=leave-'
            }
        }
    }

    post {
        success {
            echo "Done. Staging: http://localhost:8085 | Production: http://localhost:8086"
        }
        failure {
            echo "Pipeline failed."
        }
    }
}
