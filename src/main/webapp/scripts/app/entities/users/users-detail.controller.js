'use strict';

angular.module('aclcrawlerApp')
    .controller('UsersDetailController', function ($scope, $rootScope, $stateParams, entity, Users, Password_history) {
        $scope.users = entity;
        $scope.load = function (id) {
            Users.get({user_id: id}, function(result) {
                $scope.users = result;
            });
        };
        var unsubscribe = $rootScope.$on('aclcrawlerApp:usersUpdate', function(event, result) {
            $scope.users = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
