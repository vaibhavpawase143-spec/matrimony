# Gathbandhan Matrimony
# AdminController API Testing Documentation
Version: 1.0
Status: In Progress

------------------------------------------------------------
CONTROLLER
------------------------------------------------------------

Controller Name : AdminController

Base URL

http://localhost:9090/api/admins

------------------------------------------------------------
API-01 ADMIN LOGIN
------------------------------------------------------------

Method

POST

Endpoint

/api/admins/login

Authentication

Not Required

Request Body

{
"email":"admin@gathbandhan.com",
"password":"admin123"
}

Status

✅ PASSED

Test Cases

✅ Valid Login

Response

200 OK

JWT Generated

Refresh Token Generated

Admin Details Returned

Role Returned

Production Status

✅ PRODUCTION READY

------------------------------------------------------------
API-02 CREATE ADMIN
------------------------------------------------------------

Method

POST

Endpoint

/ api/admins

Authentication

SUPER_ADMIN

Headers

Authorization : Bearer <SUPER_ADMIN_TOKEN>

Content-Type : application/json

Request

{
"name":"Test Admin",
"username":"testadmin",
"email":"testadmin@gathbandhan.com",
"phone":"9876543210",
"password":"Admin@123"
}

Result

✅ PASSED

Test Cases

✅ Valid Create

✅ Duplicate Email

Expected

Email already registered

PASS

✅ Duplicate Phone

Expected

Phone already exists

PASS

✅ Without JWT

Expected

401 Unauthorized

PASS

✅ ROLE_ADMIN Create Admin

Expected

403 Forbidden

PASS

Production Status

✅ PRODUCTION READY

------------------------------------------------------------
API-03 GET ALL ADMINS
------------------------------------------------------------

Method

GET

Endpoint

/api/admins

Authentication

ADMIN

SUPER_ADMIN

Test Cases

✅ SUPER_ADMIN

PASS

200 OK

✅ ADMIN

PASS

200 OK

✅ Without JWT

PASS

401 Unauthorized

✅ Invalid JWT

PASS

401 Unauthorized

Production Status

✅ PRODUCTION READY

------------------------------------------------------------
API-04 GET ADMIN BY ID
------------------------------------------------------------

Method

GET

Endpoint

/api/admins/{id}

Authentication

ADMIN

SUPER_ADMIN

Test Cases

✅ ADMIN Own Profile

GET /api/admins/2

PASS

200 OK

------------------------------------------------

✅ ADMIN Another Admin

GET /api/admins/1

PASS

403 Forbidden

------------------------------------------------

✅ SUPER_ADMIN Own Profile

GET /api/admins/1

PASS

200 OK

------------------------------------------------

✅ SUPER_ADMIN Another Admin

GET /api/admins/2

PASS

403 Forbidden

------------------------------------------------

Production Status

✅ PRODUCTION READY

------------------------------------------------------------
OVERALL CONTROLLER STATUS
------------------------------------------------------------

API                         STATUS

Login                       ✅

Create Admin                ✅

Get All Admins              ✅

Get Admin By ID             ✅

Update Admin                ⬜

Delete Admin                ⬜

Logout                      ⬜

Refresh Token               ⬜

Statistics                  ⬜

Manage Admin                ⬜

Update Manage               ⬜

Activate Admin              ⬜

Deactivate Admin            ⬜

Reset Password              ⬜

------------------------------------------------------------
PROGRESS
------------------------------------------------------------

Completed

4 / 14 APIs

Production Ready

4 APIs

Pending

10 APIs

------------------------------------------------------------
NEXT API
------------------------------------------------------------

PUT /api/admins/{id}

Tests

□ Update Own Profile

□ Update Another Admin

□ Without JWT

□ Invalid Phone

□ Validation

□ Password Update

□ Audit Log

------------------------------------------------------------
NOTES
------------------------------------------------------------

All completed APIs have been verified using Postman.

JWT Authentication verified.

Role Based Authorization verified.

Audit Logging implemented.

DTO Validation verified.

Response Structure verified.

Security verified.
