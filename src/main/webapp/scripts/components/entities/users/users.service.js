'use strict';

angular.module('aclcrawlerApp')
    .factory('Users', function ($resource, DateUtils) {
        return $resource('api/userss/:user_id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.create_date = DateUtils.convertDateTimeFromServer(data.create_date);
                    data.modify_date = DateUtils.convertDateTimeFromServer(data.modify_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
