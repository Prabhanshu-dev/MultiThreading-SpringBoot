pipeline {
    agent any
    
    tools {
        maven "maven"
        jdk "jdk" // Specify the JDK configured in Jenkins
    }
    
    stages {
        stage("SCM checkout") {
            steps {
                script {
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], userRemoteConfigs: [[url: 'https://github.com/Prabhanshu-dev/MultiThreading-SpringBoot.git']]])
                }
            }
        }
        stage("Build Process") {
            steps {
                script {
                    bat 'mvn clean install'
                }
            }
        }
    }
}
