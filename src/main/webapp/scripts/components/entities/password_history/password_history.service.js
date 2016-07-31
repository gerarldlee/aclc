'use strict';

angular.module('aclcrawlerApp')
    .factory('Password_history', function ($resource, DateUtils) {
        return $resource('api/password_historys/:password_history_id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.create_date = DateUtils.convertDateTimeFromServer(data.create_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
