pipeline{
    agent any
    tools{
        maven 'maven'
    }
    stages{
        stage('Build Maven'){
            steps{
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/ajaikamble10/clgportal_api']])
                bat 'mvn clean install'
            }
        }
        stage('Build docker image'){
            steps{
                script{
                    bat 'docker build -t clgportal-api .'
                }
            }
        }
        stage('Push image to hub'){
            steps{
                withCredentials([string(credentialsId: 'docker_jenkins_pwd', variable: 'docker_jenkins_pwd')]) {
                 powershell '''
                docker login -u ajaykamble10 --password $env:docker_jenkins_pwd
                docker tag clgportal-api ajaykamble10/clgportal-api:latest
                docker push ajaykamble10/clgportal-api:latest
            '''
}
                bat 'docker push ajaykamble10/clgportal-api'
            }
        }
        stage('Deploy to kubernetes'){
            steps{
                kubernetesDeploy (configs: 'deploymentservice.yaml', kubeconfigId: 'k8sconfigpwd')
            }
        }
    }
}