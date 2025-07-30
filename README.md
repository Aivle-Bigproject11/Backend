# 

## Model
www.msaez.io/#/111743282/storming/bc4bdec9b33c8b1a04447ae600a8c2b7

## Before Running Services
### Make sure there is a Kafka server running
```
cd kafka
docker-compose up
```
- Check the Kafka messages:
```
cd infra
docker-compose exec -it kafka /bin/bash
cd /bin
./kafka-console-consumer --bootstrap-server localhost:9092 --topic
```

## Run the backend micro-services
See the README.md files inside the each microservices directory:

- servicecontext
- funeralcontext
- usercontext
- memorialcontext
- dataanalysis
- funeralcontextai


## Run API Gateway (Spring Gateway)
```
cd gateway
mvn spring-boot:run
```

## Test by API
- servicecontext
```
 http :8088/conversionServices serviceId="serviceId"serviceName="serviceName"imageUrl="imageURL"detailedUrl="detailedURL"
 http :8088/recommendMessages messageId="messageId"comment="comment"serviceId1="serviceId1"serviceId2="serviceId2"customerId="customerId"createMessageDate="createMessageDate"
```
- funeralcontext
```
 http :8088/funeralInfos funeralInfoId="funeralInfoId"customerId="customerId"customerName="customerName"customerRrn="customerRrn"customerPhone="customerPhone"deceasedName="deceasedName"deceasedAge="deceasedAge"deceasedDate="deceasedDate"deceasedBirthOfDate="deceasedBirthOfDate"deceasedGender="deceasedGender"deceasedNameHanja="deceasedNameHanja"deceasedRrn="deceasedRrn"deceasedReligion="deceasedReligion"deceasedRelationToHouseholdHead="deceasedRelationToHouseholdHead"reportRegistrationDate="reportRegistrationDate"reporterName="reporterName"reporterRrn="reporterRrn"reporterRelationToDeceased="reporterRelationToDeceased"reporterAddress="reporterAddress"reporterPhone="reporterPhone"reporterEmail="reporterEmail"submitterName="submitterName"submitterRrn="submitterRrn"funeralCompanyName="funeralCompanyName"directorName="directorName"directorPhone="directorPhone"funeralHomeName="funeralHomeName"mortuaryInfo="mortuaryInfo"funeralHomeAddress="funeralHomeAddress"funeralDuration="funeralDuration"processionDateTime="processionDateTime"burialSiteInfo="burialSiteInfo"chiefMourners="chiefMourners"templateKeyword="templateKeyword"
 http :8088/obituaries obituaryId="obituaryId"funeralInfoId="funeralInfoId"obituaryTemplateId="obituaryTemplateId"obituaryFilePath="obituaryFilePath"obituaryFileUrl="obituaryFileUrl"obituaryStatus="obituaryStatus"obituaryCreatedAt="obituaryCreatedAt"customerId="customerId"customerName="customerName"customerRrn="customerRrn"customerPhone="customerPhone"deceasedName="deceasedName"deceasedAge="deceasedAge"deceasedDate="deceasedDate"deceasedBirthOfDate="deceasedBirthOfDate"deceasedGender="deceasedGender"deceasedNameHanja="deceasedNameHanja"deceasedRrn="deceasedRrn"deceasedReligion="deceasedReligion"deceasedRelationToHouseholdHead="deceasedRelationToHouseholdHead"reportRegistrationDate="reportRegistrationDate"reporterName="reporterName"reporterRrn="reporterRrn"reporterRelationToDeceased="reporterRelationToDeceased"reporterAddress="reporterAddress"reporterPhone="reporterPhone"reporterEmail="reporterEmail"submitterName="submitterName"submitterRrn="submitterRrn"funeralCompanyName="funeralCompanyName"directorName="directorName"directorPhone="directorPhone"funeralHomeName="funeralHomeName"mortuaryInfo="mortuaryInfo"funeralHomeAddress="funeralHomeAddress"funeralDuration="funeralDuration"processionDateTime="processionDateTime"burialSiteInfo="burialSiteInfo"chiefMourners="chiefMourners"templateKeyword="templateKeyword"
 http :8088/deathReports deathReportId="deathReportId"funeralInfoId="funeralInfoId"deathReportTemplateId="deathReportTemplateId"deathReportFilePath="deathReportFilePath"deathReportFileUrl="deathReportFileUrl"deathReportStatus="deathReportStatus"deathReportCreatedAt="deathReportCreatedAt"customerId="customerId"customerName="customerName"customerRrn="customerRrn"customerPhone="customerPhone"deceasedName="deceasedName"deceasedAge="deceasedAge"deceasedDate="deceasedDate"deceasedBirthOfDate="deceasedBirthOfDate"deceasedGender="deceasedGender"deceasedNameHanja="deceasedNameHanja"deceasedRrn="deceasedRrn"deceasedReligion="deceasedReligion"deceasedRelationToHouseholdHead="deceasedRelationToHouseholdHead"reportRegistrationDate="reportRegistrationDate"reporterName="reporterName"reporterRrn="reporterRrn"reporterRelationToDeceased="reporterRelationToDeceased"reporterAddress="reporterAddress"reporterPhone="reporterPhone"reporterEmail="reporterEmail"submitterName="submitterName"submitterRrn="submitterRrn"funeralCompanyName="funeralCompanyName"directorName="directorName"directorPhone="directorPhone"funeralHomeName="funeralHomeName"mortuaryInfo="mortuaryInfo"funeralHomeAddress="funeralHomeAddress"funeralDuration="funeralDuration"processionDateTime="processionDateTime"burialSiteInfo="burialSiteInfo"chiefMourners="chiefMourners"templateKeyword="templateKeyword"
 http :8088/schedules scheduleId="scheduleId"funeralInfoId="funeralInfoId"scheduleTemplateId="scheduleTemplateId"scheduleFilePath="scheduleFilePath"scheduleFileUrl="scheduleFileUrl"scheduleStatus="scheduleStatus"scheduleCreatedAt="scheduleCreatedAt"customerId="customerId"customerName="customerName"customerRrn="customerRrn"customerPhone="customerPhone"deceasedName="deceasedName"deceasedAge="deceasedAge"deceasedDate="deceasedDate"deceasedBirthOfDate="deceasedBirthOfDate"deceasedGender="deceasedGender"deceasedNameHanja="deceasedNameHanja"deceasedRrn="deceasedRrn"deceasedReligion="deceasedReligion"deceasedRelationToHouseholdHead="deceasedRelationToHouseholdHead"reportRegistrationDate="reportRegistrationDate"reporterName="reporterName"reporterRrn="reporterRrn"reporterRelationToDeceased="reporterRelationToDeceased"reporterAddress="reporterAddress"reporterPhone="reporterPhone"reporterEmail="reporterEmail"submitterName="submitterName"submitterRrn="submitterRrn"funeralCompanyName="funeralCompanyName"directorName="directorName"directorPhone="directorPhone"funeralHomeName="funeralHomeName"mortuaryInfo="mortuaryInfo"funeralHomeAddress="funeralHomeAddress"funeralDuration="funeralDuration"processionDateTime="processionDateTime"burialSiteInfo="burialSiteInfo"chiefMourners="chiefMourners"templateKeyword="templateKeyword"
```
- usercontext
```
 http :8088/managers id="id"loginId="loginId"loginPassword="loginPassword"name="name"phone="phone"
 http :8088/customerProfiles customerId="customerId"name="name"age="age"phone="phone"job="job"address="address"gender="gender"birthOfDate="birthOfDate"hasChildren="hasChildren"isMarried="isMarried"rrn="rrn"
 http :8088/families id="id"loginId="loginId"loginPassword="loginPassword"name="name"email="email"phone="phone"status="status"memorialId="memorialId"createdAt="createdAt"updatedAt="updatedAt"
```
- memorialcontext
```
 http :8088/memorials memorialId="memorialId"customerId="customerId"imageUrl="imageUrl"name="name"age="age"birthOfDate="birthOfDate"deceasedDate="deceasedDate"gender="gender"
 http :8088/videos videoId="videoId"memorialId="memorialId"videoTitle="videoTitle"videoUrl="videoUrl"keywords="keywords"status="status"createdAt="createdAt"completedAt="completedAt"
 http :8088/photos photoId="photoId"memorialId="memorialId"title="title"imageUrl="imageUrl"uploadedAt="uploadedAt"
 http :8088/comments commentId="commentId"memorialId="memorialId"name="name"relationship="relationship"content="content"createdAt="createdAt"
 http :8088/tributes tributeId="tributeId"memorialId="memorialId"content="content"keywords="keywords"createdAt="createdAt"updatedAt="updatedAt"
```
- dataanalysis
```
 http :8088/deathPredictions date="date"region="region"predictedDeath="predicted_death"
```
- funeralcontextai
```
 http :8088/funeralPythons id="id"
```


## Run the frontend
```
cd frontend
npm i
npm run serve
```

## Test by UI
Open a browser to localhost:8088

## Required Utilities

- httpie (alternative for curl / POSTMAN) and network utils
```
sudo apt-get update
sudo apt-get install net-tools
sudo apt install iputils-ping
pip install httpie
```

- kubernetes utilities (kubectl)
```
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
```

- aws cli (aws)
```
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
```

- eksctl 
```
curl --silent --location "https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz" | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
```
