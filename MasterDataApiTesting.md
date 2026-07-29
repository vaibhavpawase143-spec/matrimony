# Gathbandhan Matrimony
# Master Data API Testing Documentation

Version : 1.0
Status  : Production Ready

============================================================
MASTER DATA CONTROLLERS
============================================================

Documentation Type

API Testing Documentation

Project

Gathbandhan Matrimony

Base URL

http://localhost:9090/api

Authentication

JWT (Where Required)

Documentation Covers

BloodGroupController
BodyTypeController
BrotherTypeController
CasteController
CityController
ComplexionController
CountryController
DietController
DisabilityStatusController
DrinkingController
EducationLevelController
EmployedController
FamilyController
FamilyDetailsController
FamilyStatusController
FamilyTypeController
FamilyValueController
FieldOfStudyController
GenderController
HeightController
IncomeController
ManglikStatusController
MaritalStatusController
MotherTongueController
OccupationController
ProfileTypeController
QualificationController
ReligionController
RoleController
SisterTypeController
SmokingController
StateController
SubCasteController
SubscriptionPlanController
WeightController

============================================================
MASTER-01 BLOOD GROUP CONTROLLER
============================================================

Controller Name

BloodGroupController

Base URL

http://localhost:9090/api/blood-groups

Module

Master Data

Authentication

JWT Required (Except Public APIs)

Production Status

✅ PRODUCTION READY

============================================================
API-01 CREATE BLOOD GROUP
============================================================

Method

POST

Endpoint

/api/blood-groups

Authentication

JWT Required

Headers

Authorization : Bearer <ADMIN_TOKEN>

Content-Type : application/json

Request Body

{
"type":"A+",
"isActive":true
}

Status

✅ PASSED

Test Cases

✅ Valid Blood Group

PASS

201 Created

--------------------------------------------------

✅ Duplicate Blood Group

PASS

400 Bad Request

"Blood group already exists"

--------------------------------------------------

✅ Blank Type

PASS

400 Validation Error

--------------------------------------------------

✅ Without JWT

PASS

401 Unauthorized

--------------------------------------------------

✅ Invalid JWT

PASS

401 Unauthorized

--------------------------------------------------

Audit Log

Generated Successfully

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
API-02 GET BLOOD GROUP BY ID
============================================================

Method

GET

Endpoint

/api/blood-groups/{id}

Authentication

JWT Required

Status

✅ PASSED

Test Cases

✅ Valid Blood Group ID

PASS

200 OK

--------------------------------------------------

✅ Invalid Blood Group ID

PASS

404 Not Found

--------------------------------------------------

✅ Unauthorized Admin

PASS

400 Bad Request

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
API-03 GET ALL BLOOD GROUPS
============================================================

Method

GET

Endpoint

/ api/blood-groups

Authentication

Not Required

Status

✅ PASSED

Test Cases

✅ Get All Blood Groups

PASS

200 OK

--------------------------------------------------

Soft Deleted Records Hidden

PASS

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
API-04 GET ACTIVE BLOOD GROUPS
============================================================

Method

GET

Endpoint

/ api/blood-groups/active

Authentication

Not Required

Status

✅ PASSED

Test Cases

✅ Only Active Records Returned

PASS

200 OK

--------------------------------------------------

Deleted Records Hidden

PASS

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
API-05 UPDATE BLOOD GROUP
============================================================

Method

PUT

Endpoint

/ api/blood-groups/{id}

Authentication

JWT Required

Status

✅ PASSED

Test Cases

✅ Valid Update

PASS

--------------------------------------------------

✅ Duplicate Blood Group

PASS

400 Bad Request

--------------------------------------------------

✅ Invalid Id

PASS

404 Not Found

--------------------------------------------------

Audit Log Generated

PASS

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
API-06 DELETE BLOOD GROUP
============================================================

Method

DELETE

Endpoint

/ api/blood-groups/{id}

Authentication

JWT Required

Status

✅ PASSED

Test Cases

✅ Soft Delete

PASS

--------------------------------------------------

✅ Invalid Id

PASS

404 Not Found

--------------------------------------------------

Audit Log Generated

PASS

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
API-07 GET DELETED BLOOD GROUPS
============================================================

Method

GET

Endpoint

/ api/blood-groups/deleted

Authentication

JWT Required

Status

✅ PASSED

Test Cases

✅ Deleted Records Retrieved

PASS

200 OK

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
API-08 RESTORE BLOOD GROUP
============================================================

Method

PUT

Endpoint

/ api/blood-groups/restore/{id}

Authentication

JWT Required

Status

✅ PASSED

Test Cases

✅ Restore Deleted Blood Group

PASS

--------------------------------------------------

✅ Invalid Id

PASS

404 Not Found

--------------------------------------------------

Audit Log Generated

PASS

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
API-09 HARD DELETE BLOOD GROUP
============================================================

Method

DELETE

Endpoint

/ api/blood-groups/hard-delete/{id}

Authentication

JWT Required

Status

✅ PASSED

Test Cases

✅ Permanent Delete

PASS

--------------------------------------------------

✅ Invalid Id

PASS

404 Not Found

--------------------------------------------------

Audit Log Generated

PASS

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
BLOOD GROUP CONTROLLER STATUS
============================================================

API                                     STATUS

Create Blood Group                      ✅

Get Blood Group By ID                   ✅

Get All Blood Groups                    ✅

Get Active Blood Groups                 ✅

Update Blood Group                      ✅

Soft Delete Blood Group                 ✅

Get Deleted Blood Groups                ✅

Restore Blood Group                     ✅

Hard Delete Blood Group                 ✅

============================================================

Completed APIs

9 / 9

Passed

9 / 9

Production Ready

9 / 9

Failed

0

============================================================
MASTER-02 BODY TYPE CONTROLLER
============================================================

Controller Name

BodyTypeController

Public Base URL

http://localhost:9090/api/body-types

Admin Base URL

http://localhost:9090/api/admins/{adminId}/body-types

Module

Master Data

Authentication

Public APIs : Not Required

Admin APIs : JWT Required

Production Status

✅ PRODUCTION READY

============================================================
API-01 GET PUBLIC BODY TYPES
============================================================

Method

GET

Endpoint

/api/body-types

Authentication

Not Required

Status

✅ PASSED

Test Cases

✅ Get Active Body Types

PASS

200 OK

--------------------------------------------------

Only Active Records Returned

PASS

--------------------------------------------------

Production Status

✅ PRODUCTION READY

============================================================
API-02 CREATE BODY TYPE
============================================================

Method

POST

Endpoint

/api/admins/{adminId}/body-types

Authentication

JWT Required

Headers

Authorization : Bearer <ADMIN_TOKEN>

Content-Type : application/json

Request Body

{
"value":"Athletic",
"isActive":true
}

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-03 GET BODY TYPE BY ID
============================================================

Method

GET

Endpoint

/api/admins/{adminId}/body-types/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-04 GET ALL BODY TYPES
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/body-types

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-05 GET ACTIVE BODY TYPES
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/body-types/active

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-06 UPDATE BODY TYPE
============================================================

Method

PUT

Endpoint

/ api/admins/{adminId}/body-types/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-07 DELETE BODY TYPE
============================================================

Method

DELETE

Endpoint

/ api/admins/{adminId}/body-types/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-08 GET DELETED BODY TYPES
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/body-types/deleted

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-09 RESTORE BODY TYPE
============================================================

Method

PUT

Endpoint

/ api/admins/{adminId}/body-types/restore/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-10 HARD DELETE BODY TYPE
============================================================

Method

DELETE

Endpoint

/ api/admins/{adminId}/body-types/hard-delete/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
BODY TYPE CONTROLLER STATUS
============================================================

API                                     STATUS

Get Public Body Types                   ✅

Create Body Type                        ✅

Get Body Type By ID                     ✅

Get All Body Types                      ✅

Get Active Body Types                   ✅

Update Body Type                        ✅

Soft Delete Body Type                   ✅

Get Deleted Body Types                  ✅

Restore Body Type                       ✅

Hard Delete Body Type                   ✅

============================================================

Completed APIs

10 / 10

Passed

10 / 10

Production Ready

10 / 10

Failed

0

============================================================
MASTER-03 BROTHER TYPE CONTROLLER
============================================================

Controller Name

BrotherTypeController

Admin Base URL

http://localhost:9090/api/admins/{adminId}/brother-types

Module

Master Data

Authentication

JWT Required

Production Status

✅ PRODUCTION READY

============================================================
API-01 CREATE BROTHER TYPE
============================================================

Method

POST

Endpoint

/api/admins/{adminId}/brother-types

Authentication

JWT Required

Headers

Authorization : Bearer <ADMIN_TOKEN>

Content-Type : application/json

Request Body

{
"value":"2 Brothers",
"isActive":true
}

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-02 GET BROTHER TYPE BY ID
============================================================

Method

GET

Endpoint

/api/admins/{adminId}/brother-types/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-03 GET ALL BROTHER TYPES
============================================================

Method

GET

Endpoint

/api/admins/{adminId}/brother-types

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-04 GET ACTIVE BROTHER TYPES
============================================================

Method

GET

Endpoint

/api/admins/{adminId}/brother-types/active

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-05 UPDATE BROTHER TYPE
============================================================

Method

PUT

Endpoint

/api/admins/{adminId}/brother-types/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-06 DELETE BROTHER TYPE
============================================================

Method

DELETE

Endpoint

/api/admins/{adminId}/brother-types/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-07 GET DELETED BROTHER TYPES
============================================================

Method

GET

Endpoint

/api/admins/{adminId}/brother-types/deleted

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-08 RESTORE BROTHER TYPE
============================================================

Method

PUT

Endpoint

/api/admins/{adminId}/brother-types/restore/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-09 HARD DELETE BROTHER TYPE
============================================================

Method

DELETE

Endpoint

/api/admins/{adminId}/brother-types/hard-delete/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
BROTHER TYPE CONTROLLER STATUS
============================================================

API                                     STATUS

Create Brother Type                     ✅

Get Brother Type By ID                  ✅

Get All Brother Types                   ✅

Get Active Brother Types                ✅

Update Brother Type                     ✅

Soft Delete Brother Type                ✅

Get Deleted Brother Types               ✅

Restore Brother Type                    ✅

Hard Delete Brother Type                ✅

============================================================

Completed APIs

9 / 9

Passed

9 / 9

Production Ready

9 / 9

Failed

0

============================================================
MASTER-04 CASTE CONTROLLER
============================================================

Controller Name

CasteController

Admin Base URL

http://localhost:9090/api/admins/{adminId}/castes

Module

Master Data

Authentication

JWT Required

Production Status

✅ PRODUCTION READY

============================================================
API-01 CREATE CASTE
============================================================

Method

POST

Endpoint

/ api/admins/{adminId}/castes

Authentication

JWT Required

Headers

Authorization : Bearer <ADMIN_TOKEN>

Content-Type : application/json

Request Body

{
"name":"Maratha",
"religionId":1,
"isActive":true
}

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-02 GET CASTE BY ID
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/castes/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-03 GET ALL CASTES
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/castes

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-04 GET ACTIVE CASTES
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/castes/active

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-05 GET CASTES BY RELIGION
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/castes/religion/{religionId}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-06 GET ACTIVE CASTES BY RELIGION
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/castes/religion/{religionId}/active

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-07 SEARCH CASTES
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/castes/search?keyword={keyword}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-08 UPDATE CASTE
============================================================

Method

PUT

Endpoint

/ api/admins/{adminId}/castes/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-09 DELETE CASTE
============================================================

Method

DELETE

Endpoint

/ api/admins/{adminId}/castes/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-10 GET DELETED CASTES
============================================================

Method

GET

Endpoint

/ api/admins/{adminId}/castes/deleted

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-11 RESTORE CASTE
============================================================

Method

PUT

Endpoint

/ api/admins/{adminId}/castes/restore/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-12 HARD DELETE CASTE
============================================================

Method

DELETE

Endpoint

/ api/admins/{adminId}/castes/hard-delete/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
CASTE CONTROLLER STATUS
============================================================

API                                     STATUS

Create Caste                            ✅

Get Caste By Id                         ✅

Get All Castes                          ✅

Get Active Castes                       ✅

Get Castes By Religion                  ✅

Get Active Castes By Religion           ✅

Search Castes                           ✅

Update Caste                            ✅

Soft Delete Caste                       ✅

Get Deleted Castes                      ✅

Restore Caste                           ✅

Hard Delete Caste                       ✅

============================================================

Completed APIs

12 / 12

Passed

12 / 12

Production Ready

12 / 12

Failed

0

============================================================
MASTER-05 CITY CONTROLLER
============================================================

Controller Name

CityController

Base URL

http://localhost:9090/api/cities

Module

Master Data

Authentication

JWT Required (Create / Update / Delete Operations)

Public APIs

Get All
Get By ID
Get Active
Get By State
Search

Production Status

✅ PRODUCTION READY

============================================================
API-01 CREATE CITY
============================================================

Method

POST

Endpoint

/api/cities

Authentication

JWT Required

Headers

Authorization : Bearer <ADMIN_TOKEN>

Content-Type : application/json

Request Body

{
"name":"Pune",
"stateId":21,
"adminId":1,
"isActive":true
}

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-02 GET CITY BY ID
============================================================

Method

GET

Endpoint

/api/cities/{id}

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-03 GET ALL CITIES
============================================================

Method

GET

Endpoint

/api/cities

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-04 GET ACTIVE CITIES
============================================================

Method

GET

Endpoint

/api/cities/active

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-05 GET CITIES BY STATE
============================================================

Method

GET

Endpoint

/api/cities/state/{stateId}

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-06 GET CITIES BY ADMIN
============================================================

Method

GET

Endpoint

/api/cities/admin/{adminId}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-07 SEARCH CITY
============================================================

Method

GET

Endpoint

/api/cities/search?keyword={keyword}

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-08 UPDATE CITY
============================================================

Method

PUT

Endpoint

/api/cities/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-09 DELETE CITY
============================================================

Method

DELETE

Endpoint

/api/cities/{id}?deletedBy={adminId}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-10 GET DELETED CITIES
============================================================

Method

GET

Endpoint

/api/cities/deleted

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-11 RESTORE CITY
============================================================

Method

PUT

Endpoint

/api/cities/restore/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-12 HARD DELETE CITY
============================================================

Method

DELETE

Endpoint

/api/cities/hard-delete/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
CITY CONTROLLER STATUS
============================================================

API                                     STATUS

Create City                             ✅

Get City By ID                          ✅

Get All Cities                          ✅

Get Active Cities                       ✅

Get Cities By State                     ✅

Get Cities By Admin                     ✅

Search Cities                           ✅

Update City                             ✅

Soft Delete City                        ✅

Get Deleted Cities                      ✅

Restore City                            ✅

Hard Delete City                        ✅

============================================================

Completed APIs

12 / 12

Passed

12 / 12

Production Ready

12 / 12

Failed

0

============================================================
MASTER-06 COMPLEXION CONTROLLER
============================================================

Controller Name

ComplexionController

Base URL

http://localhost:9090/api/complexions

Module

Master Data

Authentication

JWT Required (Create / Update / Delete Operations)

Public APIs

Get All
Get By ID
Get Active
Search

Production Status

✅ PRODUCTION READY

============================================================
API-01 CREATE COMPLEXION
============================================================

Method

POST

Endpoint

/api/complexions

Authentication

JWT Required

Headers

Authorization : Bearer <ADMIN_TOKEN>

Content-Type : application/json

Request Body

{
"value":"Fair",
"adminId":1,
"isActive":true
}

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-02 GET COMPLEXION BY ID
============================================================

Method

GET

Endpoint

/api/complexions/{id}

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-03 GET ALL COMPLEXIONS
============================================================

Method

GET

Endpoint

/api/complexions

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-04 GET ACTIVE COMPLEXIONS
============================================================

Method

GET

Endpoint

/api/complexions/active

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-05 GET COMPLEXIONS BY ADMIN
============================================================

Method

GET

Endpoint

/api/complexions/admin/{adminId}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-06 SEARCH COMPLEXION
============================================================

Method

GET

Endpoint

/api/complexions/search?keyword={keyword}

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-07 UPDATE COMPLEXION
============================================================

Method

PUT

Endpoint

/api/complexions/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-08 DELETE COMPLEXION
============================================================

Method

DELETE

Endpoint

/api/complexions/{id}?deletedBy={adminId}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-09 GET DELETED COMPLEXIONS
============================================================

Method

GET

Endpoint

/api/complexions/deleted

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-10 RESTORE COMPLEXION
============================================================

Method

PUT

Endpoint

/api/complexions/restore/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-11 HARD DELETE COMPLEXION
============================================================

Method

DELETE

Endpoint

/api/complexions/hard-delete/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
COMPLEXION CONTROLLER STATUS
============================================================

API                                     STATUS

Create Complexion                       ✅

Get Complexion By ID                    ✅

Get All Complexions                     ✅

Get Active Complexions                  ✅

Get Complexions By Admin                ✅

Search Complexions                      ✅

Update Complexion                       ✅

Soft Delete Complexion                  ✅

Get Deleted Complexions                 ✅

Restore Complexion                      ✅

Hard Delete Complexion                  ✅

============================================================

Completed APIs

11 / 11

Passed

11 / 11

Production Ready

11 / 11

Failed

0

============================================================
MASTER-07 COUNTRY CONTROLLER
============================================================

Controller Name

CountryController

Base URL

http://localhost:9090/api/countries

Module

Master Data

Authentication

JWT Required (Create / Update / Delete Operations)

Public APIs

Get All
Get By ID
Get Active
Search

Production Status

✅ PRODUCTION READY

============================================================
API-01 CREATE COUNTRY
============================================================

Method

POST

Endpoint

/ api/countries

Authentication

JWT Required

Headers

Authorization : Bearer <ADMIN_TOKEN>

Content-Type : application/json

Request Body

{
"name":"India",
"adminId":1,
"isActive":true
}

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-02 GET COUNTRY BY ID
============================================================

Method

GET

Endpoint

/ api/countries/{id}

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-03 GET ALL COUNTRIES
============================================================

Method

GET

Endpoint

/ api/countries

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-04 GET ACTIVE COUNTRIES
============================================================

Method

GET

Endpoint

/ api/countries/active

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-05 GET COUNTRIES BY ADMIN
============================================================

Method

GET

Endpoint

/ api/countries/admin/{adminId}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-06 SEARCH COUNTRIES
============================================================

Method

GET

Endpoint

/ api/countries/search?keyword={keyword}

Authentication

Not Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-07 UPDATE COUNTRY
============================================================

Method

PUT

Endpoint

/ api/countries/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-08 DELETE COUNTRY
============================================================

Method

DELETE

Endpoint

/ api/countries/{id}?deletedBy={adminId}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-09 GET DELETED COUNTRIES
============================================================

Method

GET

Endpoint

/ api/countries/deleted

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-10 RESTORE COUNTRY
============================================================

Method

PUT

Endpoint

/ api/countries/restore/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
API-11 HARD DELETE COUNTRY
============================================================

Method

DELETE

Endpoint

/ api/countries/hard-delete/{id}

Authentication

JWT Required

Status

✅ PASSED

Production Status

✅ PRODUCTION READY

============================================================
COUNTRY CONTROLLER STATUS
============================================================

API                                     STATUS

Create Country                          ✅

Get Country By ID                       ✅

Get All Countries                       ✅

Get Active Countries                    ✅

Get Countries By Admin                  ✅

Search Countries                        ✅

Update Country                          ✅

Soft Delete Country                     ✅

Get Deleted Countries                   ✅

Restore Country                         ✅

Hard Delete Country                     ✅

============================================================

Completed APIs

11 / 11

Passed

11 / 11

Production Ready

11 / 11

Failed

0

