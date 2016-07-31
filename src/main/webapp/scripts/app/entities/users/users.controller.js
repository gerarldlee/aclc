'use strict';

angular.module('aclcrawlerApp')
    .controller('UsersController', function ($scope, Users, ParseLinks) {
        $scope.userss = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Users.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.userss.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.userss = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Users.get({id: id}, function(result) {
                $scope.users = result;
                $('#deleteUsersConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Users.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteUsersConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.users = {
                name: null,
                surname: null,
                email: null,
                password: null,
                status: null,
                create_by: null,
                create_date: null,
                modify_by: null,
                modify_date: null,
                user_id: null
            };
        };
    });
