print("START")

// Connecting to the 'springsocial' database
db = db.getSiblingDB('springsocial');

// Creating a new user 'mongoadmin' with readWrite role for 'springsocial' database
db.createUser(
    {
        user: 'mongoadmin',
        pwd: 'password',
        roles: [{role: 'readWrite', db: 'springsocial'}]
    }
);

//Creating a collection named "comment"
db.createCollection("comment");
//Creating a collection named "post"
db.createCollection("post");


print("END");
