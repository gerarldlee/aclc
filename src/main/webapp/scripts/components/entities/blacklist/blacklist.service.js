'use strict';

angular.module('aclcrawlerApp')
    .factory('Blacklist', function ($resource, DateUtils) {
        return $resource('api/blacklists/:blacklist_id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
