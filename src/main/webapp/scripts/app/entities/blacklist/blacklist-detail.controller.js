'use strict';

angular.module('aclcrawlerApp')
    .controller('BlacklistDetailController', function ($scope, $rootScope, $stateParams, entity, Blacklist) {
        $scope.blacklist = entity;
        $scope.load = function (id) {
            Blacklist.get({blacklist_id: id}, function(result) {
                $scope.blacklist = result;
            });
        };
        var unsubscribe = $rootScope.$on('aclcrawlerApp:blacklistUpdate', function(event, result) {
            $scope.blacklist = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
