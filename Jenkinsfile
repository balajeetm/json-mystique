#!/usr/bin/env groovy

pipeline {
    agent any

    tools { 
        maven 'maven-3.5.0' 
        jdk 'jdk8-131' 
    }

    options {
        buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '5'))
        disableConcurrentBuilds()
        timeout(time: 1, unit: 'HOURS')
    }

    stages {
        stage ('Init') {
            steps {
                sh '''
                    echo "PATH = ${PATH}"
                    echo "M2_HOME = ${M2_HOME}"
                '''
            }
        }

        stage ('Build') {
            steps {
                sh "mvn clean"
            }
        }

        stage ('Release') {
            steps {
                sh "mvn -B release:prepare -DdevelopmentVersion=2.0.9-SNAPSHOT -DreleaseVersion=2.0.8"
            }
        }
    }
}
