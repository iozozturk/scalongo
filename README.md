# Scalongo [![Build Status](https://travis-ci.org/iozozturk/scalongo.svg)](https://travis-ci.org/iozozturk/scalongo) [![works badge](https://cdn.rawgit.com/nikku/works-on-my-machine/v0.2.0/badge.svg)](https://github.com/nikku/works-on-my-machine)


This is a sample seed project showcasing new scala driver for MongoDb on Play Framework 2.6.13

Best suitable for your REST backends, Single Page Application(SPA) backends

### What is this repository for? ###

* Bootstrap your Play Application with MongoDb
* Play Framework 2.6.X
* No frontend implemented
* REST backend for all kind of your SPA or mobile applications
* Sample MailGun api used for automated emails for signup, reset pass etc.

### Implemented routines/endpoints ###

* signup
* activate account
* login
* logout
* forgot password
* set new password
* secure endpoint

### How do I get set up? ###

* Download the project
* Start your local MongoDB instance
* Make changes at application.conf if necessary
* run "./activator run" command at root directory
* navigate to localhost:9000 on your browser
* in order for MailService to work fill in your MailGun apiKey in application.conf

### Sample Requests ###

* **POST** localhost:9000/api/signup
* **Content-Type:**application/json
```
{
  "password" : "123456",
  "email" : "ismet@ismet.com",
  "name" : "ismet"
}
```

* **POST** localhost:9000/api/login
* **Content-Type:**application/json
```
{
  "email" : "ismet@ismet.com",
  "password" : "123456"
}
```

* **GET** localhost:9000/api/secure

* **POST** localhost:9000/api/logout

### License ###

Copyright 2018 İsmet Özöztürk

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
