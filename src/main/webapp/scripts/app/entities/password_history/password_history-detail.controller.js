'use strict';

angular.module('aclcrawlerApp')
    .controller('Password_historyDetailController', function ($scope, $rootScope, $stateParams, entity, Password_history, Users) {
        $scope.password_history = entity;
        $scope.load = function (id) {
            Password_history.get({password_history_id: id}, function(result) {
                $scope.password_history = result;
            });
        };
        var unsubscribe = $rootScope.$on('aclcrawlerApp:password_historyUpdate', function(event, result) {
            $scope.password_history = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
