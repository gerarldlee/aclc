'use strict';

angular.module('aclcrawlerApp')
    .controller('BlacklistController', function ($scope, Blacklist, ParseLinks) {
        $scope.blacklists = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Blacklist.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.blacklists.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.blacklists = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Blacklist.get({blacklist_id: id}, function(result) {
                $scope.blacklist = result;
                $('#deleteBlacklistConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Blacklist.delete({blacklist_id: id},
                function () {
                    $scope.reset();
                    $('#deleteBlacklistConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.blacklist = {
                partial_qualified_name: null,
                blacklist_id: null
            };
        };
    });
