'use strict';

/* Services */
var myAppServices = angular.module('myApp.services', []);

myAppServices.value('version', '0.1');

myAppServices.factory('emailAddressCheckService', function() {
  var emailAddressCheckServiceImpl = {};

  emailAddressCheckServiceImpl.values = ["foo@bar.baz.com", "zork@mit.edu"];
  emailAddressCheckServiceImpl.add = function(emailAddress) {
    this.values.push(name);
  };

  emailAddressCheckServiceImpl.search = function(value) {
    console.log("searching for", value);
    return (emailAddressCheckServiceImpl.values.indexOf(value) != -1);
  };
  return emailAddressCheckServiceImpl;
});

myAppServices.factory('nickNameCheckService', function() {
    var nickNameServiceImpl = {};

    /** our service state - stubbed until we use a web service against a live engine */
    nickNameServiceImpl.values = ['dave', 'chuck', 'sal'];

    /** Used to register a name once joining the game */
    nickNameServiceImpl.add = function(name) {
      this.values.push(name);
    };

    /** returns true if a value exists, false otherwise */
    nickNameServiceImpl.search = function(value) {
      console.log("searching for", value);
      return (nickNameServiceImpl.values.indexOf(value) != -1);
    };

    return nickNameServiceImpl;
});


