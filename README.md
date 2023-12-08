# sysc-4806-project

# Background

This project is a simplified survey application, based on SurveyMonkey, allowing surveyors to create surveys with diverse question types—open-ended, numeric within a range, and multiple-choice. The application dynamically generates user-friendly survey forms corresponding to the question types. Participants can fill out these surveys, and surveyors have the flexibility to close surveys at their discretion. Upon closure, the application generates a comprehensive result report, presenting open-ended responses as-is, visualizing numeric answers through histograms, and displaying multiple-choice responses in pie charts. With features for survey creation, participant engagement, and result compilation, the application serves as a versatile tool for conducting and analyzing surveys in various formats.
  
# Getting Started

Prerequisites
  - Azure Account
  - Azure CLI installed
  - Java Development Kit (JDK)
  - Azure Spring Cloud Plugin

# Deployment Steps

  1. Clone the repository:
      git clone https://github.com/your-username/mini-survey-monkey.git
      cd mini-survey-monkey
  2. Create Azure Spring Cloud Service Instance:
       Create an Azure Spring Cloud instance using Azure CLI. Replace <your-resource-group> and <your-service-instance> with your desired values.
       az spring-cloud create --name <your-service-instance> --resource-group <your-resource-group> --sku standard
  3. Configure Database:
       Update the application.properties file to use an Azure-compatible database or configure an Azure Database service.
  5. Build and Deploy:
     ./mvnw clean install
      az spring-cloud app deploy --name mini-survey-monkey --jar target/mini-survey-monkey-0.0.1-SNAPSHOT.jar

  6. Access the application: Access the application in your web browser.
       The application will be accessible at the URL provided by Azure Spring Cloud. You can find the URL using the following command:
           az spring-cloud app show --name mini-survey-monkey --resource-group <your-resource-group> --query hostName

# Continuous Integration

This project also has continuous integration set up, so that each successful push to master deploys to Azure. The deploy location is: https://sysc4806projectgroup23.azurewebsites.net/.

# Usage

  1. Create a new survey by navigating to http://<your-app-url>.
  2. Add questions to the survey, specifying the question type.
  3. Save the survey and share the link with users.
  4. Users can respond to the survey by accessing the dynamically generated form at http://<your-app-url>/addRemoveQuestions/{surveyId}.
  5. Surveyors can close the survey when desired.
  6. View the survey results at http://<your-app-url>/surveyCreated, including answers and visualizations.

# Schema and UML
Schema:

![image](https://github.com/Andrei486/sysc-4806-project/assets/78574494/7c5f8843-0bff-4b79-8b82-1cf32a3dc8ad)

UML:

![image](https://github.com/Andrei486/sysc-4806-project/assets/78574494/446897a8-7dee-4ba1-b1fd-275d6274de68)


# Contribution

  - Daniah Mohammed
  - Andrei Popescu
  - Cam Sommerville
  - Rayat Md Kibria
  - David Birinberg
