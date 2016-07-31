'use strict';

angular.module('aclcrawlerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


