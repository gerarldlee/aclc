 'use strict';

angular.module('aclcrawlerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-aclcrawlerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-aclcrawlerApp-params')});
                }
                return response;
            }
        };
    });
