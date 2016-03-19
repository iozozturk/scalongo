# Scalongo  [![Build Status](https://travis-ci.org/iozozturk/scalongo.svg)](https://travis-ci.org/iozozturk/scalongo)

This is a sample seed project showcasing new scala driver for MongoDb on Play Framework 2.5.0

You may also check **scalongo-android** project (seed android application client) compatible with scalongo

### What is this repository for? ###

* Bootstrap your Play Application with MongoDb
* Play Framework 2.5.0
* No frontend implemented
* REST backend for all kind of your SPA or mobile applications

### How do I get set up? ###

* Download the project
* Start your local MongoDb instance
* Make changes at application.conf if necessary
* run "./activator run" command at root directory
* navigate to localhost:9000 on your browser

### Sample Requests ###

* **POST** localhost:9000/signup
* **Content-Type:**application/json
```
{
  "username" : "ismet",
  "password" : "123456",
  "email" : "ismet@ismet.com",
  "name" : "ismet"
}
```

* **POST** localhost:9000/login
* **Content-Type:**application/json
```
{
  "username" : "ismet",
  "password" : "123456"
}
```

* **GET** localhost:9000/secure

* **POST** localhost:9000/logout

### License ###

Copyright 2015 İsmet Özöztürk

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
